package com.example.cat_and_mise.game

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.cat_and_mise.R
import com.example.cat_and_mise.game_objects.Mouse
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameFragment : Fragment() {

    private lateinit var gameView: GameView
    private val viewModel: GameViewModel by viewModels()

    private var backgroundMusic: MediaPlayer? = null
    private var eatSound: MediaPlayer? = null

    private lateinit var playerMouse: Mouse

    private lateinit var levelNameText: TextView
    private lateinit var playerNameText: TextView
    private lateinit var scoreText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        levelNameText = view.findViewById(R.id.levelNameText)
        playerNameText = view.findViewById(R.id.playerNameText)
        scoreText = view.findViewById(R.id.scoreText)
        gameView = view.findViewById(R.id.gameView)

        // Получаем данные игрока из аргументов
        val playerName = arguments?.getString("playerName") ?: "Игрок"
        val speed = arguments?.getInt("speed") ?: 5
        val levelName = arguments?.getString("levelName") ?: "Уровень"

        // Создаем мышь игрока
        playerMouse = Mouse(playerName, speed)

        initGameView()
        setupObservers()
        startGameLoop()
        initSounds()

        // Устанавливаем название уровня
        levelNameText.text = levelName
        playerNameText.text = playerMouse.getMouseInfo()
    }

    private fun initGameView() {
        gameView.setPlayerMouse(playerMouse)
        gameView.testMode = false

        gameView.onScoreUpdate = { score ->
            viewModel.updateScore(score)
            GameService.updateNotification(requireContext(), score)
        }

        gameView.onGameOver = { score ->
            showGameOverDialog(score)
        }

        gameView.onCheeseEaten = {
            playEatSound()
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.score.collect { score ->
                scoreText.text = "Счет: $score"
            }
        }
    }

    private fun startGameLoop() {
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                try {
                    gameView.update()
                    delay(20)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initSounds() {
        backgroundMusic = MediaPlayer.create(requireContext(), R.raw.background_music)
        backgroundMusic?.isLooping = true

        eatSound = MediaPlayer.create(requireContext(), R.raw.eat_sound)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true)

        if (isMusicEnabled) {
            backgroundMusic?.start()
        }
    }

    private fun playEatSound() {
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true)

        if (isMusicEnabled) {
            eatSound?.start()
        }
    }

    private fun showGameOverDialog(score: Int) {
        val dialog = GameOverDialogFragment.newInstance(score, playerMouse.name)
        dialog.show(parentFragmentManager, "game_over")
    }

    override fun onPause() {
        super.onPause()
        gameView.pauseGame()
        backgroundMusic?.pause()
        GameService.stop(requireContext())
    }

    override fun onResume() {
        super.onResume()
        gameView.resumeGame()

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isMusicEnabled = sharedPreferences.getBoolean("music_enabled", true)

        if (isMusicEnabled) {
            backgroundMusic?.start()
        }

        GameService.start(requireContext(), viewModel.score.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backgroundMusic?.release()
        eatSound?.release()
    }
}