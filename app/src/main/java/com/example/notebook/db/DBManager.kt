package com.example.notebook.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.notebook.db.FeedEntry.COLUMN_NAME_CONTENT
import com.example.notebook.db.FeedEntry.COLUMN_NAME_IMAGE_URI
import com.example.notebook.db.FeedEntry.COLUMN_NAME_TITLE
import com.example.notebook.db.FeedEntry.TABLE_NAME

class DBManager(context: Context) {
    private val myHelper = DBHelper(context)
    var db: SQLiteDatabase? = null

    fun open() {
        db = myHelper.writableDatabase
    }

    fun insert(title: String, content: String, uri: String) {
        val value = ContentValues().apply {
            put(COLUMN_NAME_TITLE, title)
            put(COLUMN_NAME_CONTENT, content)
            put(COLUMN_NAME_IMAGE_URI, uri)
        }
        val newRowID = db?.insert(TABLE_NAME, null, value)
    }

    fun read(): List<Pair<String, String>> {
        val res = arrayListOf<Pair<String, String>>()
        val cursor = db?.query(TABLE_NAME, null, null, null,
            null, null, null)!!
        with(cursor) {
            while (moveToNext()) {
                val item1 = getString(getColumnIndexOrThrow(COLUMN_NAME_TITLE))
                val item2 = getString(getColumnIndexOrThrow(COLUMN_NAME_CONTENT))
                res.add(Pair(item1, item2))
            }
        }
        cursor.close()
        return res
    }

    fun close() {
        myHelper.close()
    }
}