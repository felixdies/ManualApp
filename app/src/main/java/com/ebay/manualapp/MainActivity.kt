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
import java.util.*
import android.widget.Toast



class MainActivity : Activity(), Observer {
    var userItems = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ObservableObject.instance.addObserver(this)

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
            //addItemList(Item("위닉스 공기청정기\n앱솔루트 플러스", "위닉스 공기청정기 WACU150", "B470817114", "B402730548", "B531060785", R.drawable.item1, 26, 180))
        }

        // 역순으로 추가됨
        addItemList(Item("삼성 레이저프린터\nSL-M2027", "삼성 레이저프린터 SL-M2027", "", "", "", R.drawable.item4, 200, 365))
        addItemList(Item("삼성 빌트인 냉장고\nHX6711", "삼성 빌트인 냉장고 HX6711", "", "", "", R.drawable.item3, 143, 180))
        addItemList(Item("삼성 블루스카이\n공기청정기", "삼성 블루스카이 공기청정기", "gBBys6b", "gBM6NNs", "B531060785", R.drawable.item2, 326, 365))

        /*
        lvNotes.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listNotes[position].title, Toast.LENGTH_SHORT).show()
        }
        */
    }

    override fun update(observable: Observable, data: Any) {
        Log.i("SMSReceiver", "MainActivity get - " + data.toString())
        if(data.toString().indexOf("다이슨") >= 0) {
            addItemList(Item("다이슨 V7 플러피\n무선청소기", "다이슨 V7 플러피 무선청소기", "", "", "", R.drawable.item1, 730, 180))
        }
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
            vh.pdfName = items[position].pdfName
            vh.sup1 = items[position].sup1
            vh.sup2 = items[position].sup2
            vh.sup3 = items[position].sup3

            vh.setListeners(ctx)

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
        val ivPdf: ImageView
        var pdfName = ""
        var sup1 = ""
        var sup2 = ""
        var sup3 = ""

        init {
            this.imgItem = view?.findViewById(R.id.imgItem) as ImageView
            this.tvItemName= view?.findViewById(R.id.tvItemName) as TextView
            this.tvRemain= view?.findViewById(R.id.tvRemain) as TextView
            this.ivPdf = view?.findViewById(R.id.ivPdf) as ImageView

            ivPdf.setImageResource(R.drawable.pdficon)
        }

        fun setListeners(context: Context) {
            imgItem.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("sup1", sup1)
                intent.putExtra("sup2", sup2)
                intent.putExtra("sup3", sup3)
                context.startActivity(intent)
            }

            ivPdf.setOnClickListener {
                val intent = Intent(context, PDFActivity::class.java)
                intent.putExtra("pdfName", pdfName)
                intent.putExtra("pdfPage", 0)
                context.startActivity(intent)
            }
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

    inner class Item (val name: String, val pdfName: String, val sup1: String, val sup2: String, val sup3: String, val img: Int, val remain: Int, val total: Int)
}
