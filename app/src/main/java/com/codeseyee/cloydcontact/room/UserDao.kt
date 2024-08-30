package com.codeseyee.cloydcontact.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codeseyee.cloydcontact.Model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM users WHERE isSynced = 0 LIMIT :limit")
    suspend fun getUnsyncedUsers(limit: Int): List<User>

    @Query("SELECT * FROM users WHERE email LIKE :searchKey")
    suspend fun searchUsersByEmail(searchKey: String): List<User>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE lastUpdatedAt < :timestamp")
    suspend fun deleteOldUsers(timestamp: Long)

    @Query("SELECT MAX(lastUpdatedAt) FROM users")
    suspend fun getMaxLastUpdatedAtTime(): Long?
}
