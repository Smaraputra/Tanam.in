package id.capstone.tanamin.data.di

import android.content.Context
import id.capstone.tanamin.data.TanaminRepository
import id.capstone.tanamin.data.local.database.TanaminRoomDatabase
import id.capstone.tanamin.data.remote.retrofit.ConfigAPI

object Injection {
    fun provideRepository(context: Context, token: String): TanaminRepository {
        val database = TanaminRoomDatabase.getDatabase(context)
        val apiService = ConfigAPI.getApiService(token)
        val detectionService = ConfigAPI.getApiDetectionService(token)
        return TanaminRepository(database, apiService, detectionService)
    }
}