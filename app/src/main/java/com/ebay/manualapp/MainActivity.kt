package com.ebay.manualapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View.MeasureSpec
import android.widget.*

class MainActivity : Activity() {
    var userItems = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ListView 가 ScrollView 와 함께 Scroll 되도록 함
        lvItems.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(false)
            return@setOnTouchListener false
        }

        tvSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        tvHello.setOnClickListener {
            addItemList(Item("다이슨 V10\n앱솔루트 플러스", R.drawable.item1, 26, 180))
        }

        // 역순으로 추가됨
        addItemList(Item("삼성 레이저프린터\nSL-M2027", R.drawable.item4, 200, 365))
        addItemList(Item("필립스 소닉케어\nHX6711", R.drawable.item3, 143, 180))
        addItemList(Item("삼성 블루스카이\n공기청정기", R.drawable.item2, 326, 365))
        addItemList(Item("다이슨 V10\n앱솔루트 플러스", R.drawable.item1, 26, 180))

        /*
        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listNotes[position].title, Toast.LENGTH_SHORT).show()
        }
        */
    }

    fun addItemList(item: Item) {
        val newItemList = ArrayList<Item>()
        newItemList.add(item)
        newItemList.addAll(userItems)
        userItems = newItemList
        var itemsAdapter = ItemsAdapter(this, userItems)
        lvItems.adapter = itemsAdapter
        setListViewHeightBasedOnChildren(lvItems)
    }

    inner class ItemsAdapter(context: Context, itemList: ArrayList<Item>): BaseAdapter() {
        var ctx = context
        var items = itemList

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.list_item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.imgItem.setImageResource(items[position].img)
            vh.tvItemName.text = items[position].name
            vh.tvRemain.text = "-" + items[position].remain.toString() + "일"

            vh.tvItemName.setOnClickListener {
                val intent = Intent(ctx, DetailActivity::class.java)
                startActivity(intent)
            }

            return view
        }

        override fun getItem(position: Int): Any = items[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getCount(): Int = items.size
    }

    private class ViewHolder(view: View?) {
        val imgItem: ImageView
        val tvItemName: TextView
        val tvRemain: TextView

        init {
            this.imgItem = view?.findViewById(R.id.imgItem) as ImageView
            this.tvItemName= view?.findViewById(R.id.tvItemName) as TextView
            this.tvRemain= view?.findViewById(R.id.tvRemain) as TextView
        }
    }

    /**** Method for Setting the Height of the ListView dynamically.
     * Hack to fix the issue of not showing all the items of the ListView
     * when placed inside a ScrollView   */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.getAdapter() ?: return

        val desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.getCount()) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view!!.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.getLayoutParams()
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
        listView.setLayoutParams(params)
    }

    inner class Item (val name: String, val img: Int, val remain: Int, val total: Int)
}
