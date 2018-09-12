package com.ebay.manualapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_search.*
import java.io.File
import com.ebaykorea.hackathon.finder.IterateFinder
import com.ebaykorea.hackathon.finder.MatchLine
import com.ebaykorea.hackathon.reader.InfoReader
import com.ebaykorea.hackathon.result.ManualInfo
import com.ebaykorea.hackathon.result.ManualMatchResult

class SearchActivity : Activity() {
    val manualInfo = ManualInfo(InfoReader(), IterateFinder())
    val searchItems = ArrayList<SearchItem>()
    val searchAdapter = ItemsAdapter(this, searchItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val dirPath = File(Environment.getExternalStorageDirectory().toString() + "/Manuals/info")
        dirPath.listFiles { f ->
            Log.i("shin", f.absolutePath)
            return@listFiles false
        }

        manualInfo.init(dirPath.absolutePath)

        lvSearchItems.adapter = searchAdapter

        etSearch.afterTextChanged {
            Log.i("shin", etSearch.text.toString())

            val searchTextArray = etSearch.text.toString().split(" ").toTypedArray()
            val matchResults = manualInfo.getMatchResult(searchTextArray, 3)
            Log.i("shin", "searchTextArray = $searchTextArray")
            Log.i("shin", "result size = " + matchResults.size.toString())

            searchItems.clear()

            for (result in matchResults) {
                Log.i("shin", "\n<<< result >>>\nitemName:" + result.itemName + "\nline count:" + result.matchLines.size.toString())
                val texts = ArrayList<SearchItemText>()
                for (line in result.matchLines) {
                    texts.add(SearchItemText(line.line, line.page-1))
                }
                if (texts.size > 0) {
                    searchItems.add(SearchItem(result.itemName, texts))
                }
            }

            searchAdapter.notifyDataSetChanged()

        }
    }

    inner class ItemsAdapter(context: Context, itemList: ArrayList<SearchItem>): BaseAdapter() {
        var ctx = context
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
            vh.page1 = items[position].texts[0].page

            if(items[position].texts.size >= 2) {
                vh.text2.visibility = View.VISIBLE
                vh.text2.text = items[position].texts[1].itemName
                vh.page2 = items[position].texts[1].page
            }
            else {
                vh.text2.visibility = View.GONE
            }

            if(items[position].texts.size >= 3) {
                vh.text2.visibility = View.VISIBLE
                vh.text3.text = items[position].texts[2].itemName
                vh.page3 = items[position].texts[2].page
            }
            else {
                vh.text3.visibility = View.GONE
            }

            vh.setListeners(ctx)

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
        var page1: Int = 0
        var page2: Int = 0
        var page3: Int = 0

        init {
            this.itemName = view?.findViewById(R.id.searchResultItem) as TextView
            this.text1 = view?.findViewById(R.id.searchResultText1) as TextView
            this.text2 = view?.findViewById(R.id.searchResultText2) as TextView
            this.text3 = view?.findViewById(R.id.searchResultText3) as TextView
        }

        fun setListeners(context: Context) {
            text1.setOnClickListener { v ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                val intent = Intent(context, PDFActivity::class.java)
                intent.putExtra("pdfName", itemName.text)
                intent.putExtra("pdfPage", page1)
                context.startActivity(intent)
            }

            text2.setOnClickListener { v ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                val intent = Intent(context, PDFActivity::class.java)
                intent.putExtra("pdfName", itemName.text)
                intent.putExtra("pdfPage", page2)
                context.startActivity(intent)
            }

            text3.setOnClickListener { v ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                val intent = Intent(context, PDFActivity::class.java)
                intent.putExtra("pdfName", itemName.text)
                intent.putExtra("pdfPage", page3)
                context.startActivity(intent)
            }
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    inner class SearchItem (val itemName: String, val texts: ArrayList<SearchItemText>)
    inner class SearchItemText (val itemName: String, val page: Int)
}

