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
import com.example.notebook.item.MyConstants.CONST_TITLE
import com.example.notebook.item.MyConstants.CONST_URI

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private var imageLauncher: ActivityResultLauncher<Intent>? = null
    var tempImage = "empty"
    val myDBManager = DBManager(this)

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
            myDBManager.insert(title, content, tempImage)
            finish()
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
                searchImage.visibility = View.GONE
                editTitle.setText(i.getStringExtra(CONST_TITLE))
                editCont.setText(i.getStringExtra(CONST_CONTENT))
                if (i.getStringExtra(CONST_URI) != "empty") {
                    imageLayout.visibility = View.VISIBLE
                    deleteButtin.visibility = View.GONE
                    editButton.visibility = View.GONE
                    imageFromGallery.setImageURI(i.getStringExtra(CONST_URI).toString().toUri())
                }
            }
        }
    }
}