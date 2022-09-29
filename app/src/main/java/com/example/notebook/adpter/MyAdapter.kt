package com.example.notebook.adpter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook.R
import com.example.notebook.activity.EditActivity
import com.example.notebook.db.DBManager
import com.example.notebook.item.ListItem
import com.example.notebook.item.MyConstants.CONST_CONTENT
import com.example.notebook.item.MyConstants.CONST_ID
import com.example.notebook.item.MyConstants.CONST_TITLE
import com.example.notebook.item.MyConstants.CONST_URI

class MyAdapter(private val listMain: ArrayList<ListItem>, val context: Context)
    : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    class MyHolder(itemView: View, val cont: Context) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvTime: TextView = itemView.findViewById(R.id.time)

        fun setData(item: ListItem) {
            tvTitle.text = item.title
            tvTime.text = item.time
            itemView.setOnClickListener {
                val intent = Intent(cont, EditActivity::class.java).apply {
                    putExtra(CONST_TITLE, item.title)
                    putExtra(CONST_CONTENT, item.content)
                    putExtra(CONST_URI, item.uri)
                    putExtra(CONST_ID, item.id)
                }
                cont.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.activity_rc, parent, false), context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listMain[position])
    }

    override fun getItemCount() = listMain.size

    fun updateAdapter(newArray: List<ListItem>) {
        listMain.clear()
        listMain.addAll(newArray)
        notifyDataSetChanged()
    }

    fun removeItem(pos: Int, dbManager: DBManager) {
        dbManager.remove(listMain[pos].id.toString())
        listMain.removeAt(pos)
        notifyItemRangeChanged(0, listMain.size)
        notifyItemRemoved(pos)
    }
}