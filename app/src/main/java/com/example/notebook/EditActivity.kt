package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.notebook.databinding.ActivityEditBinding
import com.example.notebook.db.DBManager

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
            if (it.resultCode == RESULT_OK) {
                binding.imageFromGallery.setImageURI(it.data?.data)
                tempImage = it.data?.data.toString()
            }
        }
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageLauncher?.launch(intent)
    }

    fun onClickSave(view: View) {
        val title = binding.editTitle.text.toString()
        val content = binding.editCont.text.toString()
        if (title != "" && content != "") {
            myDBManager.insert(title, content, tempImage)
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
}