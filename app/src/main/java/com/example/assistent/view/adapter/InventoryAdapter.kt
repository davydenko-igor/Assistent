package com.example.assistent.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assistent.databinding.RecyclerInventoryItemBinding
import com.example.assistent.entity.Inventory

class InventoryAdapter : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {
    var list = mutableListOf<Inventory>()
    private var binding: RecyclerInventoryItemBinding? = null
    private var findInventory: Inventory? = null

    class InventoryViewHolder(itemView: RecyclerInventoryItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val tvNameInventory = itemView.tvNameInventory
        private val tvCodeInventory = itemView.tvCodeInventory
        fun bind(item: Inventory) {
            if (item.state == 1) {
                tvNameInventory.setBackgroundColor(Color.GREEN)
                tvCodeInventory.setBackgroundColor(Color.GREEN)
            }
            tvNameInventory.text = item.name_inventory
            tvCodeInventory.text = item.code_inventory.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        binding =
            RecyclerInventoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size

    fun setFindInventory(inventory: Inventory?) {
        this.findInventory = inventory
        notifyDataSetChanged()
    }
}