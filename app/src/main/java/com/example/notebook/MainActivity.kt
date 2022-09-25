package com.example.notebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.adpter.MyAdapter
import com.example.notebook.databinding.ActivityMainBinding
import com.example.notebook.db.DBManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myManager = DBManager(this)
    private val myAdapter = MyAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addBut.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
        init()
    }

    override fun onResume() {
        super.onResume()
        myManager.open()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myManager.close()
    }

    private fun init() {
        binding.recItems.layoutManager = LinearLayoutManager(this)
        binding.recItems.adapter = myAdapter
    }

    fun fillAdapter() {
        myAdapter.updateAdapter(myManager.read())
    }
}