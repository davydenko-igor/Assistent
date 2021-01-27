package com.example.assistent.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
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
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.sql.*


class MainActivity : AppCompatActivity() {
    val port = "1433"
    val Classes = "net.sourceforge.jtds.jdbc.Driver"
    val db = "assistdb"
    var connect: Connection? = null

    val dao by inject<AssistentDatabase>()

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PackageManager.PERMISSION_GRANTED
        )
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding!!.btnLogin.setOnClickListener {
            try {
                Class.forName(Classes)
                val login = binding!!.edLogin.text.toString()
                val pass = binding!!.edPassword.text.toString()
                val server = binding!!.edServer.text.toString()
                val url = "jdbc:jtds:sqlserver://"+server+":"+port+"/"+db
                connect = DriverManager.getConnection(url, login, pass)

                coroutineScope.launch {
                    openMoleActivity()
                }

            }catch (e: ClassNotFoundException){
                e.printStackTrace()
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }catch (e: SQLException){
                e.printStackTrace()
                Toast.makeText(this, "FAILURE", Toast.LENGTH_SHORT).show()
            }

        }



    }

    suspend fun openMoleActivity(){
        val listMol = mutableListOf<MOL>()
        val listInventory = mutableListOf<Inventory>()
        if(connect!=null){
            var statementMol: Statement?
            var statementInventory:Statement?
            try {
                statementMol = connect?.createStatement()
                val moleTable = statementMol?.executeQuery("Select * from MOL_TABLE;")
                while (moleTable?.next()!!){
                    val id_Mol = moleTable.getInt(1)
                    val FIO = moleTable.getString(2)
                    val mole = MOL(id_Mol,FIO)
                    listMol.add(mole)
                }

                dao.getAssistentDAO.addMOL(listMol)
                statementInventory = connect?.createStatement()
                val inventoryTable = statementInventory?.executeQuery("Select * from INVENTORY_TABLE;")
                while (inventoryTable?.next()!!){
                    val id_inventory = inventoryTable.getInt(1)
                    val id_Mol = inventoryTable.getInt(2)
                    val name_inventory = inventoryTable.getString(3)
                    val code_inventory = inventoryTable.getInt(4)
                    val inventory = Inventory(id_inventory,id_Mol,name_inventory,code_inventory)
                    listInventory.add(inventory)
                }
                dao.getAssistentDAO.addInventory(listInventory)
                val intent = Intent(this, MoleActivity::class.java)
                startActivity(intent)
            }catch (e: SQLException){
                e.printStackTrace()
            }
        }

    }
}