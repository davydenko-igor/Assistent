package com.example.assistent.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assistent.R
import com.example.assistent.databinding.FragmentInventoryBinding
import com.example.assistent.entity.Inventory
import com.example.assistent.util.replaceFragment
import com.example.assistent.view.MoleActivity
import com.example.assistent.view.ReportFragment
import com.example.assistent.viewmodel.InventoryViewModel
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InventoryFragment : Fragment() {

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this.context,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = false

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var inventoryAdapter: InventoryAdapter

    private val inventoryViewModel: InventoryViewModel by viewModel { parametersOf(id_Mol) }

    private val listOfInventory = mutableListOf<Inventory?>()
    private val listOfTrashCode = ArrayList<Int?>()

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

        listOfInventory.clear()
        listOfTrashCode.clear()

        inventoryViewModel.getInventory()
        binding.fabAdd.setOnClickListener {
            onAddButtonClicked()
        }
        binding.fabScanner.setOnClickListener {
            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }
        binding.fabSave.setOnClickListener {
            listOfInventory.forEach {
                if (it?.state == 0) {
                    it.state = 4
                }
            }
            arguments = Bundle()
            arguments!!.putParcelableArray("inventoryData", listOfInventory.toTypedArray())
            arguments!!.putIntegerArrayList("trashList", listOfTrashCode)
            (this.activity as MoleActivity).replaceFragment(
                R.id.fragment_container,
                ReportFragment.newInstance(arguments!!),
                "report"
            )
        }
        inventoryViewModel.inventoryLiveData.observe(this.viewLifecycleOwner, Observer {
            inventoryAdapter.list.clear()
            inventoryAdapter.list.addAll(it)
            it.forEach {
                listOfInventory.add(it)
            }
            inventoryAdapter.notifyDataSetChanged()
        })
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabScanner.visibility = View.VISIBLE
            binding.fabSave.visibility = View.VISIBLE
        } else {
            binding.fabScanner.visibility = View.INVISIBLE
            binding.fabSave.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fabSave.startAnimation(fromBottom)
            binding.fabScanner.startAnimation(fromBottom)
            binding.fabAdd.startAnimation(rotateOpen)
        } else {
            binding.fabSave.startAnimation(toBottom)
            binding.fabScanner.startAnimation(toBottom)
            binding.fabAdd.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.fabScanner.isClickable = true
            binding.fabSave.isClickable = true
        } else {
            binding.fabScanner.isClickable = false
            binding.fabSave.isClickable = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this.context, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                var trash = false

                for (index in listOfInventory.indices) {
                    if (listOfInventory[index]?.code_inventory == result.contents.toInt()) {
                        trash = false
                        if (listOfInventory[index]?.state == 0) {
                            listOfInventory[index]?.state = 1
                            inventoryAdapter.setFindInventory(listOfInventory[index])
                        } else if (listOfInventory[index]?.state == 1) {
                            listOfInventory[index]?.state = 2
                        }
                        break
                    } else {
                        trash = true
                    }
                }
                if (trash) {
                    listOfTrashCode.add(result.contents.toInt())
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