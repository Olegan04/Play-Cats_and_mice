package com.example.cat_and_mise.ui.level

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cat_and_mise.R

class LevelsAdapter(
    private val levels: List<Level>,
    private val onLevelClick: (Level) -> Unit
) : RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>() {

    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.levelNameTextView)
        private val difficultyTextView: TextView = itemView.findViewById(R.id.levelDifficultyTextView)

        fun bind(level: Level, onClick: (Level) -> Unit) {
            nameTextView.text = level.name
            difficultyTextView.text = "Сложность: ${level.difficulty}"

            itemView.setOnClickListener { onClick(level) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position], onLevelClick)
    }

    override fun getItemCount(): Int = levels.size
}