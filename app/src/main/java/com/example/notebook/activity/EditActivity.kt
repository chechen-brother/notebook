package com.example.notebook.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.notebook.databinding.ActivityEditBinding
import com.example.notebook.db.DBManager
import com.example.notebook.item.MyConstants.CONST_CONTENT
import com.example.notebook.item.MyConstants.CONST_ID
import com.example.notebook.item.MyConstants.CONST_TITLE
import com.example.notebook.item.MyConstants.CONST_URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private var imageLauncher: ActivityResultLauncher<Intent>? = null
    var tempImage = "empty"
    private val myDBManager = DBManager(this)
    private var id = 0
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            this.contentResolver.takePersistableUriPermission(it.data?.data!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (it.resultCode == RESULT_OK) {
                binding.imageFromGallery.setImageURI(it.data?.data)
                tempImage = it.data?.data.toString()
            }
        }
        getItems()
    }

    fun onClickAddImage(view: View) {
        binding.imageLayout.visibility = View.VISIBLE
        binding.searchImage.visibility = View.GONE
    }

    fun onClickDelete(view: View) {
        tempImage = "empty"
        binding.imageFromGallery.setImageURI(null)
        binding.imageLayout.visibility = View.GONE
        binding.searchImage.visibility = View.VISIBLE
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        imageLauncher?.launch(intent)
    }

    fun onClickSave(view: View) {
        val title = binding.editTitle.text.toString()
        val content = binding.editCont.text.toString()
        if (title != "" && content != "") {
            CoroutineScope(Dispatchers.Main).launch {
                if (isEdit) {
                    myDBManager.update(id, title, content, tempImage, getTime())
                } else {
                    myDBManager.insert(title, content, tempImage, getTime())
                }
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myDBManager.open()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.close()
    }

    private fun getItems() = with(binding) {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(CONST_TITLE) != null) {
                binding.editTitle.isEnabled = false
                binding.editCont.isEnabled = false
                searchImage.visibility = View.GONE
                editTitle.setText(i.getStringExtra(CONST_TITLE))
                editCont.setText(i.getStringExtra(CONST_CONTENT))
                id = i.getIntExtra(CONST_ID, 0)
                isEdit = true
                if (i.getStringExtra(CONST_URI) != "empty") {
                    imageLayout.visibility = View.VISIBLE
                    deleteButtin.visibility = View.GONE
                    editButton.visibility = View.GONE
                    tempImage = i.getStringExtra(CONST_URI)!!
                    imageFromGallery.setImageURI(tempImage.toUri())
                }
            }
        }
        else binding.floatingEditButton.visibility = View.GONE
    }

    fun onClickEnable(view: View) {
        if (tempImage != "empty") {
            binding.deleteButtin.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
        }
        else {
            binding.searchImage.visibility = View.VISIBLE
        }
        binding.editTitle.isEnabled = true
        binding.editCont.isEnabled = true
    }

    private fun getTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}