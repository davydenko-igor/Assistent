package com.example.assistent.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(
    containerId: Int,
    fragment: Fragment,
    backStackTag: String? = null
) {
    supportFragmentManager.beginTransaction().apply {
        replace(containerId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
        commit()
    }
}