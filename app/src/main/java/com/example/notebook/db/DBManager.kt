package com.example.notebook.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notebook.db.FeedEntry.COLUMN_NAME_CONTENT
import com.example.notebook.db.FeedEntry.COLUMN_NAME_IMAGE_URI
import com.example.notebook.db.FeedEntry.COLUMN_NAME_TITLE
import com.example.notebook.db.FeedEntry.TABLE_NAME
import com.example.notebook.item.ListItem

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

    fun remove(id: String) {
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf("$id")
        db?.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun read(arg: String): List<ListItem> {
        val res = arrayListOf<ListItem>()
        val selection = "$COLUMN_NAME_TITLE LIKE ?"
        val cursor = db?.query(TABLE_NAME, null, selection, arrayOf("%$arg%"),
            null, null, null)!!
        with(cursor) {
            while (moveToNext()) {
                val item1 = getString(getColumnIndexOrThrow(COLUMN_NAME_TITLE))
                val item2 = getString(getColumnIndexOrThrow(COLUMN_NAME_CONTENT))
                val item3 = getString(getColumnIndexOrThrow(COLUMN_NAME_IMAGE_URI))
                val item4 = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                res.add(ListItem(item1, item2, item3, item4))
            }
        }
        cursor.close()
        return res
    }

    fun close() {
        myHelper.close()
    }
}