package com.example.garimabedi.mykotlin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.garimabedi.mykotlin.api.PhotoRetriever
import com.example.garimabedi.mykotlin.models.Photo
import com.example.garimabedi.mykotlin.models.PhotoList

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), View.OnClickListener{

    var photos : List<Photo>? = null
    var mainAdapter : MainAdapter? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // retrieve photos
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = (LinearLayoutManager(this))

        var retriever = PhotoRetriever()

        val callback = object : Callback<PhotoList> {
            override fun onResponse(call: Call<PhotoList>?, response: Response<PhotoList>?) {
                response?.isSuccessful.let {
                    Log.e("MainActivity", "Photos retrieved successfully")
                    this@MainActivity.photos = response?.body()?.hits
                    mainAdapter = MainAdapter(this@MainActivity.photos!!,
                        this@MainActivity)
                    recyclerView.adapter = mainAdapter
                }
            }
            override fun onFailure(call: Call<PhotoList>?, t: Throwable?) {
                Log.e("MainActivity", "Failure to retrieve photos")
            }
        }

        retriever.getPhotos(callback)

    }


    override fun onClick(view : View?) {
        val intent = Intent(this, DetailActivity::class.java)
        val holder = view?.tag as MainAdapter.PhotoViewHolder
        intent.putExtra(DetailActivity.PHOTO, mainAdapter?.getPhoto(holder.adapterPosition))
        startActivity(intent)
    }
}
