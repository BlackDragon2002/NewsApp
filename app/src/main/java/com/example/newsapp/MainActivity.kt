package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager=LinearLayoutManager(this)
       fetchData()
        mAdapter=NewsListAdapter(this)
        recyclerView.adapter=mAdapter
    }
    private fun fetchData() {
        val url="https://newsdata.io/api/1/news?apikey=pub_6766e76aa19f0c07f28e4e0221b89141c1e&country=in&language=en"
        val jsonObjectRequest= JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray=it.getJSONArray("results")
                val newsArray=ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("creator"),
                        newsJsonObject.getString("link"),
                        newsJsonObject.getString("image_url")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            }, {

            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(items: News) {
        val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(items.url))
    }
}