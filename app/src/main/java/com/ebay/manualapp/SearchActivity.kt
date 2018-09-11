package com.ebay.manualapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.list_searchitem.view.*

class SearchActivity : Activity() {
    var searchItems = ArrayList<SearchItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch.afterTextChanged {  }

        val texts1 = ArrayList<SearchItemText>()
        texts1.add(SearchItemText("이상한 냄새가 나요", 1))
        texts1.add(SearchItemText("필터오염이 심하거나 냄새날 경우", 2))
        texts1.add(SearchItemText("세균을 제거하고 냄새를 방지하는 기능", 3))
        val item1 = SearchItem("삼성 블루스카이 공기청정기", texts1)
        addSearchResult(item1)

        val texts2 = ArrayList<SearchItemText>()
        texts2.add(SearchItemText("구입 초기에는 약간의 새필터 냄새", 1))
        texts2.add(SearchItemText("냄새가 날 경우 일체형", 2))
        texts2.add(SearchItemText("세균을 제거하고 냄새를 방지하는 기능", 3))
        val item2 = SearchItem("위닉스뽀송 제습기", texts2)
        addSearchResult(item2)
    }

    fun addSearchResult(item: SearchItem) {
        searchItems.add(item)
        var itemsAdapter = ItemsAdapter(this, searchItems)
        lvSearchItems.adapter = itemsAdapter
        setListViewHeightBasedOnChildren(lvSearchItems)
    }

    inner class ItemsAdapter(context: Context, itemList: ArrayList<SearchItem>): BaseAdapter() {
        var items = itemList

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.list_searchitem, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            vh.itemName.text = items[position].itemName
            vh.text1.text = items[position].texts[0].itemName
            if(items[position].texts.size >= 2) vh.text2.text = items[position].texts[1].itemName
            else vh.text2.height = 0
            if(items[position].texts.size >= 3) vh.text3.text = items[position].texts[2].itemName
            else vh.text3.height = 0

            return view
        }

        override fun getItem(position: Int): Any = items[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getCount(): Int = items.size
    }

    private class ViewHolder(view: View?) {
        val itemName: TextView
        val text1: TextView
        val text2: TextView
        val text3: TextView

        init {
            this.itemName = view?.findViewById(R.id.searchResultItem) as TextView
            this.text1 = view?.findViewById(R.id.searchResultText1) as TextView
            this.text2 = view?.findViewById(R.id.searchResultText2) as TextView
            this.text3 = view?.findViewById(R.id.searchResultText3) as TextView
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    /**** Method for Setting the Height of the ListView dynamically.
     * Hack to fix the issue of not showing all the items of the ListView
     * when placed inside a ScrollView   */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.getAdapter() ?: return

        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED)
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.getCount()) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            view!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += view.measuredHeight
        }
        val params = listView.getLayoutParams()
        params.height = totalHeight + listView.getDividerHeight() * (listAdapter.getCount() - 1)
        listView.setLayoutParams(params)
    }

    inner class SearchItem (val itemName: String, val texts: ArrayList<SearchItemText>)
    inner class SearchItemText (val itemName: String, val page: Int)
}

