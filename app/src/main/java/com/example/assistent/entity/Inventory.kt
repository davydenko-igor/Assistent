package com.example.assistent.entity

import androidx.room.*

@Entity(tableName = "INVENTORY_TABLE", foreignKeys = [ForeignKey(
    entity = MOL::class,
    parentColumns = arrayOf("id_Mol"),
    childColumns = arrayOf("id_Mol")
)]
)
data class Inventory(
    @PrimaryKey val id_inventory: Int?,
    val id_Mol: Int?,
    val name_inventory : String?,
    val code_inventory : Int?,
    //@Ignore val state:Int
)
