package com.example.cat_and_mise.data

import retrofit2.http.GET

interface LeaderboardApi {
    @GET("posts")
    suspend fun getTopRecords(): List<RemoteRecord>
}