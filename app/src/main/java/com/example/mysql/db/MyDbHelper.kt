package com.example.myapplicationsql.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mysql.db.MyDbNameClass

class MyDbHelper (context: Context) : SQLiteOpenHelper(
    context,
    MyDbNameClass.MyDb.DATABASE_NAME,
    null,
    MyDbNameClass.MyDb.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyDbNameClass.MyDb.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    db?.execSQL(MyDbNameClass.MyDb.SQL_DELETE_TABLE)
        onCreate(db)
    }

}