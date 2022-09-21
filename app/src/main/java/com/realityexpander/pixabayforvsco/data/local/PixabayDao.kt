package com.realityexpander.pixabayforvsco.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertPixabayImages(pixabayImageEntity: List<PixabayImageEntity>)

    @Query("DELETE FROM pixabay_image_entity")
    suspend fun clearPixabayImages()

    // "||" is like string "+" (concatenation) in kotlin
    // tEs -> name LIKE %tes% OR TES == symbol
    @Query(
        """
            SELECT * 
            FROM pixabay_image_entity 
            WHERE LOWER(tags) LIKE '%' || LOWER(:searchString) || '%'
        """
    )
    suspend fun searchPixabay(searchString: String): List<PixabayImageEntity>

}