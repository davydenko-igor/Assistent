package com.example.assistent.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assistent.dao.AssistentDAO
import com.example.assistent.entity.MOL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoleViewModel(private val dao: AssistentDAO) : ViewModel() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val molLiveData = MutableLiveData<List<MOL>>()
    fun getMol() {
        coroutineScope.launch {
            val allMOL = dao.getAllMOL()
            molLiveData.postValue(allMOL)
        }
    }
}