package id.capstone.tanamin.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TanaminDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertClasses(classes: List<Classes>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertModuls(classes: List<Moduls>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertInformations(classes: List<Informations>)

    @Query("SELECT * FROM classes")
    fun getAllClasses(classes: LiveData<List<Classes>>)

    @Query("SELECT * FROM moduls")
    fun getAllModuls(classes: LiveData<List<Moduls>>)

    @Query("SELECT * FROM informations")
    fun getAllInformations(classes: LiveData<List<Informations>>)

}