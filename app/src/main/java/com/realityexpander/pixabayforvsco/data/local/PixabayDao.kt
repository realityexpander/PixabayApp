package com.realityexpander.pixabayforvsco.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PixabayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPixabayImages(pixabayImageEntity: List<PixabayImageEntity>)

    @Query("DELETE FROM pixabay_image_entity")
    suspend fun clearPixabayImages()

    // "||" is like string "+" (concatenation) in kotlin
    // tEs -> name LIKE %tes% OR TES == symbol
    @Query(
        """
            SELECT * 
            FROM pixabay_image_entity 
            WHERE LOWER(tags) LIKE '%' || LOWER(:searchTag) || '%'
        """
    )
    suspend fun searchImagesByTag(searchTag: String): List<PixabayImageEntity>

    @Query(
        """
           SELECT * FROM pixabay_image_entity 
           WHERE LOWER(originalSearchTerm) LIKE '%' || LOWER(:originalSearchTerm) || '%'
        """
    )
    suspend fun searchImagesByOriginalSearchTerm(originalSearchTerm: String): List<PixabayImageEntity>


    @Query("SELECT * FROM pixabay_image_entity WHERE id = :id")
    suspend fun getPixabayImage(id: String): PixabayImageEntity?
}