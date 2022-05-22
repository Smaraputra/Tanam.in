package id.capstone.tanamin.data

import id.capstone.tanamin.data.local.database.TanaminRoomDatabase
import id.capstone.tanamin.data.remote.retrofit.ServicesAPI

class TanaminRepository(
    private val tanaminRoomDatabase: TanaminRoomDatabase,
    private val apiService: ServicesAPI
    ) {
}