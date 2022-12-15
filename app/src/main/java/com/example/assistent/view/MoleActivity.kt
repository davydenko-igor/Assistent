package com.example.assistent.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assistent.R
import com.example.assistent.databinding.ActivityMoleBinding
import com.example.assistent.util.replaceFragment
import com.example.assistent.view.adapter.MolFragment

class MoleActivity : AppCompatActivity() {

    private var binding: ActivityMoleBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoleBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)
        replaceFragment(R.id.fragment_container, MolFragment())
    }
}