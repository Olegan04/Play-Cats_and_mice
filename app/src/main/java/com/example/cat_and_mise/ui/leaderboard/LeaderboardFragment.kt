package com.example.cat_and_mise.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cat_and_mise.R
import com.example.cat_and_mise.data.AppDatabase
import com.example.cat_and_mise.data.LeaderboardApi
import com.example.cat_and_mise.data.LeaderboardRepository
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LeaderboardFragment : Fragment() {

    private val viewModel: LeaderboardViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(LeaderboardApi::class.java)
        val repository = LeaderboardRepository(database.scoreRecordDao(), api)
        LeaderboardViewModelFactory(repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LeaderboardAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var btLoadTop: Button
    private lateinit var btBack: Button
    private lateinit var ibSettings: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.leaderboardRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        tvError = view.findViewById(R.id.errorTextView)
        btLoadTop = view.findViewById(R.id.loadTopButton)
        btBack = view.findViewById(R.id.backButton)
        ibSettings = view.findViewById(R.id.imageSetting)
    }

    private fun setupRecyclerView() {
        adapter = LeaderboardAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.records.collect { records ->
                val uniqueRecords = removeDuplicateRecords(records)
                adapter.submitList(uniqueRecords)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                btLoadTop.isEnabled = !isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                tvError.visibility = if (errorMessage != null) View.VISIBLE else View.GONE
                tvError.text = errorMessage
            }
        }
    }

    private fun removeDuplicateRecords(records: List<com.example.cat_and_mise.data.ScoreRecord>): List<com.example.cat_and_mise.data.ScoreRecord> {
        val seen = mutableSetOf<String>()
        return records.filter { record ->
            val key = "${record.playerName}_${record.score}"
            if (seen.contains(key)) {
                false
            } else {
                seen.add(key)
                true
            }
        }.sortedByDescending { it.score }
    }

    private fun setupClickListeners() {
        btBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btLoadTop.setOnClickListener {
            viewModel.loadTopRecords()
        }

        ibSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        tvError.setOnClickListener {
            viewModel.clearError()
        }
    }
}