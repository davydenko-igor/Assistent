package com.example.assistent.view

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.assistent.databinding.FragmentReportBinding
import com.example.assistent.entity.Inventory

import org.apache.poi.hssf.usermodel.HSSFWorkbook

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(data: Bundle) = ReportFragment().apply {
            arguments = data.apply {
                putParcelableArray("inventoryData", data.getParcelableArray("inventoryData"))
                putIntegerArrayList("trashList", data.getIntegerArrayList("trashList"))
            }
        }
    }

    private var listOfInventory: List<Inventory>? = null
    private var listOfTrashCode = ArrayList<Int?>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listOfInventory =
            arguments?.getParcelableArray("inventoryData")?.toList() as? List<Inventory>
        listOfTrashCode = arguments?.getIntegerArrayList("trashList") as ArrayList<Int?>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var find = 0
    private var notfind = 0
    private var repeat = 0
    private var trash = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOfInventory?.forEach {
            when (it.state) {
                1 -> find += 1
                4 -> notfind += 1
                2 -> repeat += 1
            }
        }
        trash = listOfTrashCode.size.toString()
        binding.tvFind.text = (find + repeat).toString()
        binding.tvNotFind.text = notfind.toString()
        binding.tvRepeat.text = repeat.toString()
        binding.tvNotFromList.text = trash

        binding.btExcelReport.setOnClickListener {

            this.activity?.let { activity ->
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ), PackageManager.PERMISSION_GRANTED
                )
            }
            createExcelReport(find, notfind, repeat, trash)
        }

    }

    private fun createExcelReport(find: Int, notFind: Int, repeat: Int, trash: String) {
        val wb = HSSFWorkbook()

        val sheet = wb.createSheet("REPORT")
        val row = sheet.createRow(0)

        var cell = row.createCell(0)
        cell.setCellValue(binding.tvTitleFind.text.toString())

        cell = row.createCell(1)
        cell.setCellValue(binding.tvTitleNotFind.text.toString())

        cell = row.createCell(2)
        cell.setCellValue(binding.tvTitleNotFromList.text.toString())

        cell = row.createCell(3)
        cell.setCellValue(binding.tvTitleRepeat.text.toString())

        val dataRow = sheet.createRow(1)
        var data = dataRow.createCell(0)
        data.setCellValue(find.toString())
        data = dataRow.createCell(1)
        data.setCellValue(notFind.toString())
        data = dataRow.createCell(2)
        data.setCellValue(trash)
        data = dataRow.createCell(3)
        data.setCellValue(repeat.toString())

        sheet.setColumnWidth(0, 10 * 200)
        sheet.setColumnWidth(1, 10 * 200)
        var fileUri: Uri? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context?.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "report.xls")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
                val filesUri = resolver?.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                fileUri = filesUri
                if (filesUri != null) {
                    resolver.openOutputStream(filesUri).use { output ->
                        wb.write(output)
                    }
                }
            } else {
                val filePath = File(context?.getExternalFilesDir(null), "Report.xls")
                val outputStream = FileOutputStream(filePath)
                fileUri = filePath.absolutePath.toUri()
                wb.write(outputStream)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileUri,"*/*")
            startActivity(intent)
            Toast.makeText(context, "OK", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "NO OK", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}