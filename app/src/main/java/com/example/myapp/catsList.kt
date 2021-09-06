package com.example.myapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapp.model.CatsResponse
import kotlinx.android.synthetic.main.activity_cats_list.*
import kotlinx.android.synthetic.main.item_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class catsList : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: CatsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var page = 0
    private var totalpage = 3
    private var isLoading = false

    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cats_list)

        token = intent.getStringExtra("token").toString()

        layoutManager = LinearLayoutManager(this)
        //swipeRefresh.setOnRefreshListener(this)
        setupRecyclerView()
        getCats(false)
        catsRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = adapter.itemCount
                if(!isLoading && page<totalpage){
                    if(visibleItemCount + pastVisibleItem >= total){
                        page++
                        getCats(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }



    private fun getCats(isOnRefresh: Boolean) {
        isLoading = true
        if(!isOnRefresh) progressBar.visibility = View.VISIBLE
        val parameters= HashMap<String, String>()
        parameters["page"] = page.toString()
        RetrofitClient.setToken(token)
        Handler().postDelayed({
            RetrofitClient.instance.getCats(parameters).enqueue(object: Callback<ArrayList<CatsResponse>>{
                override fun onResponse(call: Call<ArrayList<CatsResponse>>, response: Response<ArrayList<CatsResponse>>) {
                    val listResponse = response.body()
                    if(listResponse != null){
                        adapter.addList(listResponse)
                    }
                    if (page == totalpage)
                    {
                        progressBar.visibility = View.GONE
                    }else{
                        progressBar.visibility = View.INVISIBLE
                    }

                    isLoading = false
                    swipeRefresh.isRefreshing = false
                }

                override fun onFailure(call: Call<ArrayList<CatsResponse>>, t: Throwable) {
                    Toast.makeText(this@catsList, "${t.message}", Toast.LENGTH_SHORT).show()
                    Log.i("RespAPI", "${t.message}")
                }

            })
        }, 1000)

    }

    private fun setupRecyclerView() {
        catsRV.setHasFixedSize(true)
        catsRV.layoutManager = layoutManager
        adapter = CatsAdapter()
        catsRV.adapter = adapter
    }

    override fun onRefresh() {
        adapter.clear()
        page=1
        getCats(true)
    }

}