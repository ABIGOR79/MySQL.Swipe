package com.example.myapplicationsql.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.mysql.db.ListItem
import com.example.mysql.db.MyDbNameClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null

        fun openDb(){
            db= myDbHelper.writableDatabase
        }

        suspend fun insertToDb(title: String, content: String, uri: String, time:String) = withContext(Dispatchers.IO){
            val values = ContentValues().apply {
                put(MyDbNameClass.MyDb.COLUMN_NAME_TITLE, title)
                put(MyDbNameClass.MyDb.COLUMN_NAME_CONTENT, content)
                put(MyDbNameClass.MyDb.COLUMN_NAME_IMAGE_URI, uri)
                put(MyDbNameClass.MyDb.COLUMN_NAME_TIME, time)

            }
            db?.insert(MyDbNameClass.MyDb.TABLE_NAME, null, values)
        }

    suspend fun updateItemInDb(title: String, content: String, uri: String, id: Int, time: String) = withContext(Dispatchers.IO){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.MyDb.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.MyDb.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.MyDb.COLUMN_NAME_IMAGE_URI, uri)
            put(MyDbNameClass.MyDb.COLUMN_NAME_TIME, time)
        }
        db?.update(MyDbNameClass.MyDb.TABLE_NAME, values, selection, null)
    }

    fun removeItemFromDb(id: String){
        var selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.MyDb.TABLE_NAME,selection, null)
    }
    @SuppressLint("Range")
    suspend fun readDbData(searchText:String): ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.MyDb.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(MyDbNameClass.MyDb.TABLE_NAME, null, selection, arrayOf("%$searchText%"), null, null, null)


            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.MyDb.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndex(MyDbNameClass.MyDb.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndex(MyDbNameClass.MyDb.COLUMN_NAME_IMAGE_URI))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val time = cursor.getString(cursor.getColumnIndex(MyDbNameClass.MyDb.COLUMN_NAME_TIME))

                val item = ListItem()
                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri
                item.id = dataId
                item.time = time
                dataList.add(item)
            }
        cursor.close()
        return@withContext dataList
    }

    fun closeDb(){
        myDbHelper.close()
    }

}