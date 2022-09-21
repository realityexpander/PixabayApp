package com.realityexpander.pixabayforvsco.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PixabayImageEntity::class], version = 1)
abstract class PixabayDatabase: RoomDatabase() {
    abstract val dao: PixabayDao
}