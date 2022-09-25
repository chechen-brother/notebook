package com.example.notebook.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.R

class MyAdapter(private val listMain: ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        fun setData(title: String) {
            tvTitle.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.activity_rc, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listMain[position])
    }

    override fun getItemCount() = listMain.size

    fun updateAdapter(newArray: List<String>) {
        listMain.clear()
        listMain.addAll(newArray)
        notifyDataSetChanged()
    }
}