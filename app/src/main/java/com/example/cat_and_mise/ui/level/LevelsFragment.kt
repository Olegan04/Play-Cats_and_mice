package com.example.cat_and_mise.ui.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cat_and_mise.R

class LevelsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedLevelContainer: LinearLayout
    private lateinit var selectedLevelInfo: TextView
    private lateinit var startGameButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_levels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        setupNavigationButtons()
        observeViewModel()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.levelsRecyclerView)
        selectedLevelContainer = view.findViewById(R.id.selectedLevelContainer)
        selectedLevelInfo = view.findViewById(R.id.selectedLevelInfo)
        startGameButton = view.findViewById(R.id.startGameButton)

        startGameButton.setOnClickListener {
            startSelectedLevel()
        }
    }

    private fun setupRecyclerView() {
        val levels = listOf(
            Level(1, "Легкий уровень", "Легкий"),
            Level(2, "Средний уровень", "Средний"),
            Level(3, "Сложный уровень", "Сложный")
        )

        val adapter = LevelsAdapter(levels) { level ->
            sharedViewModel.selectLevel(level)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun setupNavigationButtons() {
        val backButton = requireView().findViewById<Button>(R.id.backButton)
        val settingsButton = requireView().findViewById<ImageButton>(R.id.imageSetting)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.startFragment)
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    private fun observeViewModel() {
        sharedViewModel.selectedLevel.observe(viewLifecycleOwner) { level ->
            level?.let {
                showSelectedLevelInfo(it)
            } ?: hideSelectedLevelInfo()
        }
    }

    private fun showSelectedLevelInfo(level: Level) {
        selectedLevelInfo.text = "${level.name}\nСложность: ${level.difficulty}"
        selectedLevelContainer.visibility = View.VISIBLE
    }

    private fun hideSelectedLevelInfo() {
        selectedLevelContainer.visibility = View.GONE
    }

    private fun startSelectedLevel() {
        val selectedLevel = sharedViewModel.selectedLevel.value
        selectedLevel?.let { level ->
            // Получаем имя игрока из настроек
            val sharedPreferences = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE)
            val playerName = sharedPreferences.getString("player_name", "Игрок") ?: "Игрок"

            // Определяем скорость в зависимости от сложности
            val speed = when (level.difficulty) {
                "Легкий" -> 5
                "Средний" -> 8
                "Сложный" -> 12
                else -> 5
            }

            // Передаем данные в игру через аргументы
            val bundle = bundleOf(
                "playerName" to playerName,
                "speed" to speed,
                "levelName" to level.name
            )

            findNavController().navigate(R.id.gameFragment, bundle)
        } ?: run {
            Toast.makeText(
                requireContext(),
                "Сначала выберите уровень",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}