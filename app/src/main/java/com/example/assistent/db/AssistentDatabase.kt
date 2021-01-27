package com.example.assistent.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.entity.Inventory
import com.example.assistent.entity.MOL

@Database(version = 1, entities = [MOL::class, Inventory::class])
abstract class AssistentDatabase : RoomDatabase() {
    abstract val getAssistentDAO : AssistentDAO
}