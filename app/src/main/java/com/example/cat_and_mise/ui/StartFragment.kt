package com.example.cat_and_mise.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cat_and_mise.R

class StartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btStart = view.findViewById<Button>(R.id.startButton)
        val btLeaderboard= view.findViewById<Button>(R.id.leaderboardButton)
        val ibSettings = view.findViewById<ImageButton>(R.id.imageSetting)

        btStart.setOnClickListener {
            findNavController().navigate(R.id.levelsFragment)
        }

        btLeaderboard.setOnClickListener {
            findNavController().navigate(R.id.leaderboardFragment)
        }

        ibSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }
}