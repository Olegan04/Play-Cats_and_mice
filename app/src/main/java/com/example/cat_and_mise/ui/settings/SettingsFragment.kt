package com.example.cat_and_mise.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cat_and_mise.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText

class SettingsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var swMusic: SwitchMaterial
    private lateinit var etPlayerName: TextInputEditText
    private lateinit var btBack: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        initViews(view)
        loadSettings()
        setupNavigationButtons()
    }

    private fun initViews(view: View) {
        swMusic = view.findViewById(R.id.musicSwitch)
        etPlayerName = view.findViewById(R.id.playerNameEditText)
        btBack = view.findViewById(R.id.backButton)

        setupListeners()
    }

    private fun setupListeners() {
        swMusic.setOnCheckedChangeListener { _, isChecked ->
            saveMusicSetting(isChecked)
        }

        etPlayerName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                savePlayerName(etPlayerName.text.toString())
            }
        }
    }

    private fun setupNavigationButtons() {
        btBack.setOnClickListener {
            savePlayerName(etPlayerName.text.toString())
            findNavController().navigateUp()
        }
    }

    private fun loadSettings() {
        val isMusicEnabled = sharedPreferences.getBoolean(MUSIC_KEY, true)
        val playerName = sharedPreferences.getString(PLAYER_NAME_KEY, "Игрок") ?: "Игрок"

        swMusic.isChecked = isMusicEnabled
        etPlayerName.setText(playerName)
    }

    private fun saveMusicSetting(isEnabled: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(MUSIC_KEY, isEnabled)
            apply()
        }
    }

    private fun savePlayerName(name: String) {
        with(sharedPreferences.edit()) {
            putString(PLAYER_NAME_KEY, name)
            apply()
        }
    }

    override fun onPause() {
        super.onPause()
        savePlayerName(etPlayerName.text.toString())
    }

    companion object {
        private const val MUSIC_KEY = "music_enabled"
        private const val PLAYER_NAME_KEY = "player_name"
    }
}