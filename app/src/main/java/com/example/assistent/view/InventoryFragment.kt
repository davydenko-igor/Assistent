package com.example.assistent.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assistent.databinding.FragmentInventoryBinding
import com.example.assistent.db.AssistentDatabase
import com.example.assistent.entity.Inventory
import com.example.assistent.viewmodel.InventoryViewModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InventoryFragment:Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var inventoryAdapter: InventoryAdapter

    private val inventoryViewModel:InventoryViewModel by viewModel{ parametersOf(id_Mol) }

    private val listOfCode = mutableListOf<String?>()

    companion object {
        @JvmStatic
        fun newInstance(data: Int?) = InventoryFragment().apply {
            arguments = Bundle().apply {
                putInt("inventory", data!!)
            }
        }
    }

    private var id_Mol: Int? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        id_Mol = arguments?.getInt("inventory")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inventoryAdapter = InventoryAdapter()
        binding.rvInventory.layoutManager = LinearLayoutManager(this.context)
        binding.rvInventory.adapter = inventoryAdapter

        inventoryViewModel.getInventory()
        binding.fab.setOnClickListener {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.initiateScan()
        }
        inventoryViewModel.inventoryLiveData.observe(this.viewLifecycleOwner, Observer {
            inventoryAdapter.list.addAll(it)
            it.forEach {
                listOfCode.add(it.code_inventory.toString())
            }
            inventoryAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                if(listOfCode.contains(result.contents)){
                    Toast.makeText(this.context, "Find", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this.context, result.contents , Toast.LENGTH_LONG).show()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}