package com.example.notebook.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.adpter.MyAdapter
import com.example.notebook.databinding.ActivityMainBinding
import com.example.notebook.db.DBManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myManager = DBManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)

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
        val swipeHelper = getSwipeMg()
        swipeHelper.attachToRecyclerView(binding.recItems)
        binding.recItems.adapter = myAdapter
    }

    private fun fillAdapter() {
        val dataFromDB = myManager.read()
        myAdapter.updateAdapter(dataFromDB)
        if (dataFromDB.isNotEmpty()) {
            binding.textEmpty.visibility = View.GONE
        }
        else  binding.textEmpty.visibility = View.VISIBLE
    }

    private fun getSwipeMg() =
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myManager)
            }

        })

}