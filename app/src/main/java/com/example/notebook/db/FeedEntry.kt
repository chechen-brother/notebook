package com.example.notebook.db

import android.provider.BaseColumns

object FeedEntry: BaseColumns {
    const val TABLE_NAME = "note_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_IMAGE_URI = "uri"
}