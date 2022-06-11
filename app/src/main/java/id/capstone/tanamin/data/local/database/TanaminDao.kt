package id.capstone.tanamin.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TanaminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classes: List<Classes>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModuls(classes: List<Moduls>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInformations(classes: List<Informations>)

    @Query("SELECT * FROM classes")
    fun getAllClasses(): LiveData<List<Classes>>

    @Query("SELECT * FROM classes WHERE title LIKE '%' || :search || '%'")
    fun searchClasses(search: String): LiveData<List<Classes>>

    @Query("SELECT * FROM modules")
    fun getAllModuls(): LiveData<List<Moduls>>

    @Query("SELECT * FROM informations")
    fun getAllInformations(): LiveData<List<Informations>>

    @Query("DELETE FROM classes")
    suspend fun deleteAllClasses()

}