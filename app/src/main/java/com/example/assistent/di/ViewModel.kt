package com.example.assistent.di

import com.example.assistent.viewmodel.InventoryViewModel
import com.example.assistent.viewmodel.MoleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MoleViewModel(get()) }

    viewModel { (id_Mol: Int) -> InventoryViewModel(get(), id_Mol) }
}