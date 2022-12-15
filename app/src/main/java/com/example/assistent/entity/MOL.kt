package com.example.assistent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MOL_TABLE")
data class MOL(
    @PrimaryKey val id_Mol: Int?,
    val FIO: String?
)
