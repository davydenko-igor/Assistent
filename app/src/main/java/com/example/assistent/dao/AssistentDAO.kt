package com.example.assistent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assistent.entity.Inventory
import com.example.assistent.entity.MOL

@Dao
interface AssistentDAO {
    @Query("SELECT * FROM MOL_TABLE")
    fun getAllMOL():List<MOL>

    @Query("SELECT * FROM INVENTORY_TABLE")
    fun getAllInventory():List<Inventory>

    @Query("SELECT * FROM INVENTORY_TABLE WHERE id_Mol = :id_Mol")
    fun getInventoryById(id_Mol: Int?):List<Inventory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMOL(MOL: List<MOL>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInventory(Inventory: List<Inventory>)
}