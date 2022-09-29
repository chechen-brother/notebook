package com.example.notebook.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.notebook.db.FeedEntry.COLUMN_NAME_CONTENT
import com.example.notebook.db.FeedEntry.COLUMN_NAME_IMAGE_URI
import com.example.notebook.db.FeedEntry.COLUMN_NAME_TIME
import com.example.notebook.db.FeedEntry.COLUMN_NAME_TITLE
import com.example.notebook.db.FeedEntry.TABLE_NAME


private const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, " +
        "$COLUMN_NAME_CONTENT TEXT, $COLUMN_NAME_IMAGE_URI TEXT, $COLUMN_NAME_TIME TEXT)"

private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION ) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(SQL_DELETE_TABLE)
        onCreate(p0)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_NAME = "Notebook.db"
        const val DATABASE_VERSION = 2
    }
}