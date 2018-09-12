package com.ebay.manualapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
        lvItems.setOnTouchListener { v, _ ->
            v.parent.requestDisallowInterceptTouchEvent(false)
            return@setOnTouchListener false
        }

        tvSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        tvHello.setOnLongClickListener {
            addItemList(Item("다이슨 V7 플러피\n무선청소기", "다이슨 V7 플러피 무선청소기", "", "", "", R.drawable.item1, 730, 1f))
        }

        // 역순으로 추가됨
        addItemList(Item("삼성 빌트인 냉장고\nHX6711", "삼성 빌트인 냉장고 HX6711", "", "", "", R.drawable.item3, 143, 0.5f))
        addItemList(Item("삼성 레이저프린터\nSL-M2027", "삼성 레이저프린터 SL-M2027", "", "", "", R.drawable.item4, 200, 0.87f))
        addItemList(Item("삼성 블루스카이\n공기청정기", "삼성 블루스카이 공기청정기", "gBBys6b", "gBM6NNs", "B531060785", R.drawable.item2, 225, 0.12f))

        tvTotal.text = "전체 " + userItems.size
    }

    override fun update(observable: Observable, data: Any) {
        Log.i("SMSReceiver", "MainActivity get - " + data.toString())
        if(data.toString().indexOf("다이슨") >= 0) {
            addItemList(Item("다이슨 V7 플러피\n무선청소기", "다이슨 V7 플러피 무선청소기", "", "", "", R.drawable.item1, 730, 1f))
        }
    }

    fun addItemList(item: Item): Boolean {
        val newItemList = ArrayList<Item>()
        newItemList.add(item)
        newItemList.addAll(userItems)
        userItems = newItemList
        var itemsAdapter = ItemsAdapter(this, userItems)
        lvItems.adapter = itemsAdapter
        setListViewHeightBasedOnChildren(lvItems)

        tvTotal.text = "전체 " + userItems.size
        return true
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

            vh.imgBackground.setImageResource(R.drawable.main_list_background)
            vh.imgItem.setImageResource(items[position].img)
            vh.tvItemName.text = items[position].name
            vh.tvRemain.text = "-" + items[position].remain.toString() + "일"
            vh.pdfName = items[position].pdfName
            vh.sup1 = items[position].sup1
            vh.sup2 = items[position].sup2
            vh.sup3 = items[position].sup3

            vh.setListeners(ctx)

            drawArc(vh.imgRemain, items[position].supplyRemain)

            return view
        }

        fun drawArc(view: ImageView, supplyRemain: Float) {
            var bitmap = Bitmap.createBitmap(105, 105, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bitmap)
            var paint = Paint()

            paint.setARGB(0xff, 0xe9, 0xe9,0xe9)
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(52.5f, 52.5f, 51.5f, paint)

            if(supplyRemain < 0.25f) {
                paint.setARGB(0xff, 0xfc, 0x3c,0x59)
            }
            else if(supplyRemain < 0.5f) {
                paint.color = Color.YELLOW
            }
            else {
                paint.color = Color.GREEN
            }
            paint.strokeWidth = 2f
            canvas.drawArc(2f, 2f, 103f, 103f, -90f, 360f * supplyRemain, false, paint)

            view.setImageBitmap(bitmap)
        }

        override fun getItem(position: Int): Any = items[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getCount(): Int = items.size
    }

    private class ViewHolder(view: View) {
        val imgBackground: ImageView
        val imgItem: ImageView
        val imgRemain: ImageView
        val tvItemName: TextView
        val tvRemain: TextView
        val ivPdf: ImageView
        var pdfName = ""
        var sup1 = ""
        var sup2 = ""
        var sup3 = ""

        init {
            this.imgBackground = view.findViewById(R.id.imgListItemBackground) as ImageView
            this.imgItem = view.findViewById(R.id.imgItem) as ImageView
            this.imgRemain = view.findViewById(R.id.imgRemain) as ImageView
            this.tvItemName= view.findViewById(R.id.tvItemName) as TextView
            this.tvRemain= view.findViewById(R.id.tvRemain) as TextView
            this.ivPdf = view.findViewById(R.id.ivPdf) as ImageView

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

    inner class Item (val name: String, val pdfName: String, val sup1: String, val sup2: String, val sup3: String, val img: Int, val remain: Int, val supplyRemain: Float)
}
