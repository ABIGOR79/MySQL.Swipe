package com.example.mysql.db

import android.provider.BaseColumns

object MyDbNameClass {
    object MyDb: BaseColumns{
        const val TABLE_NAME ="my_table"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_CONTENT = "content"
        const val COLUMN_NAME_IMAGE_URI = "uri"
        const val COLUMN_NAME_TIME = "time"


        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MyLessonDb.dB"

        const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_TITLE TEXT," +
                "$COLUMN_NAME_CONTENT TEXT, " +
                "$COLUMN_NAME_IMAGE_URI TEXT, " +
                "$COLUMN_NAME_TIME TEXT)"

        const val SQL_DELETE_TABLE = "DROP TABLE IF EXIST $DATABASE_NAME"
    }

}