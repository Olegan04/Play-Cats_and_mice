package com.example.cat_and_mise.game

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.cat_and_mise.R
import com.example.cat_and_mise.data.AppDatabase
import com.example.cat_and_mise.data.ScoreRecord
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class GameOverDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_SCORE = "score"
        private const val ARG_PLAYER_NAME = "playerName"

        fun newInstance(score: Int, playerName: String): GameOverDialogFragment {
            val args = Bundle().apply {
                putInt(ARG_SCORE, score)
                putString(ARG_PLAYER_NAME, playerName)
            }
            return GameOverDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val score = arguments?.getInt(ARG_SCORE) ?: 0
        val playerName = arguments?.getString(ARG_PLAYER_NAME) ?: "Игрок"

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Игра окончена!")
            .setMessage("$playerName, ваш счет: $score\nХотите сохранить результат?")
            .setView(R.layout.dialog_save_score)
            .setPositiveButton("Сохранить") { dialog, _ ->
                saveScore(score, playerName)
                dialog.dismiss()
                requireActivity().supportFragmentManager.popBackStack()
            }
            .setNegativeButton("Выйти") { dialog, _ ->
                dialog.dismiss()
                requireActivity().supportFragmentManager.popBackStack()
            }
            .setCancelable(false)
            .create()
    }

    private fun saveScore(score: Int, defaultPlayerName: String) {
        val editText = dialog?.findViewById<EditText>(R.id.playerNameEditText)
        val playerName = editText?.text?.toString()?.takeIf { it.isNotBlank() } ?: defaultPlayerName

        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getInstance(requireContext())
            val existingRecords = database.scoreRecordDao().getAllRecordsSync()
            val isDuplicate = existingRecords.any {
                it.playerName == playerName && it.score == score
            }

            if (!isDuplicate) {
                val scoreRecord = ScoreRecord(
                    playerName = playerName,
                    score = score,
                    date = Date()
                )
                database.scoreRecordDao().insert(scoreRecord)
            }
        }
    }
}