package com.example.assistent.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assistent.R
import com.example.assistent.databinding.FragmentMolBinding
import com.example.assistent.entity.MOL
import com.example.assistent.util.replaceFragment
import com.example.assistent.view.MoleActivity
import com.example.assistent.viewmodel.MoleViewModel
import org.koin.android.ext.android.inject

class MolFragment : Fragment(), MolClickListener {
    private var _binding: FragmentMolBinding? = null
    private val binding get() = _binding!!
    private lateinit var molAdapter: MoleAdapter
    private val molViewModel by inject<MoleViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMolBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        molAdapter = MoleAdapter(this)
        binding.rvMol.layoutManager = LinearLayoutManager(this.context)
        binding.rvMol.adapter = molAdapter
        molViewModel.getMol()
        molViewModel.molLiveData.observe(this.viewLifecycleOwner, observer)
    }

    private val observer = Observer<List<MOL>> { list ->
        molAdapter.list.clear()
        molAdapter.list.addAll(list)
        molAdapter.notifyDataSetChanged()
    }

    override fun onItemMolClick(mol_id: Int?) {
        (this.activity as MoleActivity).replaceFragment(
            R.id.fragment_container,
            InventoryFragment.newInstance(mol_id),
            "inventory"
        )
    }
}