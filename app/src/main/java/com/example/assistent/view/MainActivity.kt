package com.example.assistent.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.assistent.R
import com.example.assistent.databinding.ActivityMainBinding
import com.example.assistent.db.AssistentDatabase
import com.example.assistent.entity.Inventory
import com.example.assistent.entity.MOL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.sql.*
import java.util.*

const val port = "3306"
const val Classes = "com.mysql.jdbc.Driver"
const val db = "sys"
const val APP_PREFERENCES = "input_settings"
const val APP_PREFERENCES_LOGIN = "Login"
const val APP_PREFERENCES_PASSWORD = "Password"
const val APP_PREFERENCES_SERVER = "Server"

class MainActivity : AppCompatActivity() {

    var connect: Connection? = null

    lateinit var mSettings: SharedPreferences
    val dao by inject<AssistentDatabase>()

    private var binding: ActivityMainBinding? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PackageManager.PERMISSION_GRANTED
        )
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        //fillDefaultLoginData()

        binding!!.btnLogin.setOnClickListener {
            try {
                Class.forName(Classes).newInstance()
                val login = binding!!.edLogin.text.toString()
                val pass = binding!!.edPassword.text.toString()
                val server = binding!!.edServer.text.toString()

                val url = "jdbc:mysql://$server:$port/$db"
                val result = coroutineScope.async {
                    DriverManager.getConnection(url, login, pass)
                }
                coroutineScope.launch {
                    connect = result.await()
                    if (connect != null) {
                        val editor = mSettings.edit()
                        editor.putString(APP_PREFERENCES_LOGIN, login)
                        editor.putString(APP_PREFERENCES_PASSWORD, pass)
                        editor.putString(APP_PREFERENCES_SERVER, server)
                        editor.apply()
                        openMoleActivity()
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: SQLException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

        }

//        if (mSettings.contains(APP_PREFERENCES_LOGIN)) {
//            val logins = arrayListOf(mSettings.getString(APP_PREFERENCES_LOGIN,""))
//            var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, logins)
//            binding!!.edLogin.threshold = 0
//            binding!!.edLogin.setAdapter(adapter)
//            binding!!.edLogin.setOnFocusChangeListener { v, hasFocus -> if(hasFocus) binding!!.edLogin.showDropDown() }
//        }
    }

//    private fun fillDefaultLoginData(){
//        binding!!.edLogin.setText("andrey")
//        binding!!.edPassword.setText("123456")
//        binding!!.edServer.setText("93.170.237.200")
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openMoleActivity() = coroutineScope.launch {
        val listMol = mutableListOf<MOL>()
        val listInventory = mutableListOf<Inventory>()
        if (connect != null) {
            val statementMol: Statement?
            val statementInventory: Statement?
            try {
                statementMol = connect?.createStatement()
                val moleTable = statementMol?.executeQuery("Select * from MOL_TABLE;")
                while (moleTable?.next()!!) {
                    val id_Mol = moleTable.getInt(1)
                    val FIO = moleTable.getString(2)
                    val mole = MOL(id_Mol, FIO)
                    listMol.add(mole)
                }

                dao.getAssistentDAO.addMOL(listMol)
                statementInventory = connect?.createStatement()
                val inventoryTable =
                    statementInventory?.executeQuery("Select * from INVENTORY_TABLE;")
                while (inventoryTable?.next()!!) {
                    val id_inventory = inventoryTable.getInt(1)
                    val id_Mol = inventoryTable.getInt(2)
                    val name_inventory = inventoryTable.getString(3)
                    val code_inventory = inventoryTable.getInt(4)
                    val inventory = Inventory(id_inventory, id_Mol, name_inventory, code_inventory)
                    listInventory.add(inventory)
                }
                dao.getAssistentDAO.addInventory(listInventory)
                val intent = Intent(this@MainActivity, MoleActivity::class.java)
                startActivity(intent)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

    }
}