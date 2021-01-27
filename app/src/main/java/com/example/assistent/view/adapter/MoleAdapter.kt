package com.example.assistent.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assistent.databinding.RecyclerMoleItemBinding
import com.example.assistent.entity.MOL

class MoleAdapter(private val clickListener: MolClickListener): RecyclerView.Adapter<MoleAdapter.MoleViewHolder>() {
    var list = mutableListOf<MOL>()
    private var binding: RecyclerMoleItemBinding? = null

    class MoleViewHolder (itemView: RecyclerMoleItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val tvMol = itemView.tvMol
        fun bind(item: MOL){
            tvMol.text = item.FIO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoleViewHolder {
        binding = RecyclerMoleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MoleViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: MoleViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            clickListener.onItemMolClick(item.id_Mol)
        }
    }

    override fun getItemCount(): Int = list.size
}