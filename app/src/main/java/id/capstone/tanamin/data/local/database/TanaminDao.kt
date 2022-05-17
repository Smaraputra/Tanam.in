package id.capstone.tanamin.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TanaminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClasses(classes: List<Classes>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertModuls(classes: List<Moduls>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInformations(classes: List<Informations>)

    @Query("SELECT * FROM classes")
    fun getAllClasses(): LiveData<List<Classes>>

    @Query("SELECT * FROM moduls")
    fun getAllModuls(): LiveData<List<Moduls>>

    @Query("SELECT * FROM informations")
    fun getAllInformations(): LiveData<List<Informations>>

}