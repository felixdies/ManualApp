package com.ebay.manualapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userItems: MutableList<Item> = mutableListOf()
        userItems.add(Item("삼성 블루스카이\n공기청정기", "item1.png", 326, 365))
        userItems.add(Item("다이슨 V10\n앱솔루트 플러스", "item2.png", 26, 180))
    }

    inner class ItemsAdapter: BaseAdapter {
        private var context: Context? = null
        private var itemList = ArrayList<Item>()

        constructor(context: Context, itemList: ArrayList<Item>) : super() {
            this.itemList = itemList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View? = null
            val vh: ViewHolder

            return view
        }

        override fun getItem(position: Int): Any {
            return itemList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return itemList.size
        }
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
        }
    }

    class Item (val name: String, val img: String, val remain: Int, val total)
}
