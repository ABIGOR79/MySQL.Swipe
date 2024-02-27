package com.example.mysql.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationsql.db.MyDbManager
import com.example.mysql.EditActivity
import com.example.mysql.R

class MyAdapter(listMain:ArrayList<ListItem>, contextM: Context): RecyclerView.Adapter<MyAdapter.MyHolder>() {

    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View, contextМ: Context) : RecyclerView.ViewHolder(itemView) {
        var context = contextМ
        val tvTitle:TextView= itemView.findViewById(R.id.tv_tittle)
        val tvTime:TextView= itemView.findViewById(R.id.tvTime)
        fun setData(item: ListItem){

            tvTitle.text = item.title
            tvTime.text = item.time
            itemView.setOnClickListener{
                val intent = Intent(context, EditActivity::class.java).apply {
                    putExtra(MyIntentConstants.I_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.I_DESK_KEY, item.desc)
                    putExtra(MyIntentConstants.I_URI_KEY, item.uri)
                    putExtra(MyIntentConstants.I_ID_KEY, item.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater= LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item, parent, false), context)
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItem:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItem)
        notifyDataSetChanged()

    }
    fun removeItem(position: Int, dbManager: MyDbManager){
        dbManager.removeItemFromDb(listArray[position].id.toString())
        listArray.removeAt(position)
        notifyItemRangeChanged(0, listArray.size)
        notifyItemRemoved(position)


    }
}