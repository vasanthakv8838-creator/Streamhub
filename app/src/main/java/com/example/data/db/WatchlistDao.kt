package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
  @Query("SELECT * FROM watchlist_items ORDER BY addedAt DESC")
  fun getAllWatchlist(): Flow<List<WatchlistEntity>>

  @Query("SELECT * FROM watchlist_items WHERE mediaId = :mediaId LIMIT 1")
  suspend fun getWatchlistById(mediaId: String): WatchlistEntity?

  @Query("SELECT mediaId FROM watchlist_items")
  fun getAllWatchlistIds(): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: WatchlistEntity)

  @Update
  suspend fun update(item: WatchlistEntity)

  @Query("DELETE FROM watchlist_items WHERE mediaId = :mediaId")
  suspend fun deleteById(mediaId: String)

  @Query("UPDATE watchlist_items SET isWatched = :isWatched WHERE mediaId = :mediaId")
  suspend fun toggleWatched(mediaId: String, isWatched: Boolean)
}
