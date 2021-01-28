package com.example.assistent.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "INVENTORY_TABLE", foreignKeys = [ForeignKey(
    entity = MOL::class,
    parentColumns = arrayOf("id_Mol"),
    childColumns = arrayOf("id_Mol")
)]
)
@Parcelize
data class Inventory(
    @PrimaryKey val id_inventory: Int?,
    val id_Mol: Int?,
    val name_inventory : String?,
    val code_inventory : Int?,
    var state:Int? = 0
):Parcelable
