package com.example.cat_and_mise.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cat_and_mise.R
import com.example.cat_and_mise.data.ScoreRecord
import com.example.cat_and_mise.extensions.formatToDisplay

class LeaderboardAdapter : ListAdapter<ScoreRecord, LeaderboardAdapter.ScoreRecordViewHolder>(DiffCallback) {

    class ScoreRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
        private val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameTextView)
        private val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(scoreRecord: ScoreRecord, position: Int) {
            rankTextView.text = (position + 1).toString()
            playerNameTextView.text = scoreRecord.playerName
            scoreTextView.text = scoreRecord.score.toString()
            dateTextView.text = scoreRecord.date.formatToDisplay()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ScoreRecord>() {
        override fun areItemsTheSame(oldItem: ScoreRecord, newItem: ScoreRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScoreRecord, newItem: ScoreRecord): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score_record, parent, false)
        return ScoreRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreRecordViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}