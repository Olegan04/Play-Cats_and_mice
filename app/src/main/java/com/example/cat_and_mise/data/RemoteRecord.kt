package com.example.cat_and_mise.data

data class RemoteRecord(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
) {
    fun toScoreRecord(): ScoreRecord {
        return ScoreRecord(
            playerName = "Player ${title.take(10)}",
            score = userId * 1000, // Преобразуем в очки
            date = java.util.Date() // Используем текущую дату для примера
        )
    }
}
