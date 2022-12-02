package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currenturl: String? = null
    var prevurl: String? = null

    var flag:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    lateinit var temp2 : Button

    private fun loadMeme(){

        temp2 = findViewById(R.id.previd)
        temp2.visibility=View.VISIBLE

        lateinit var temp : ProgressBar
        temp = findViewById(R.id.progressbar)
        temp.visibility=View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                currenturl=response.getString("url")
                Glide.with(this).load(currenturl).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        temp.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        temp.visibility=View.GONE
                        return false
                    }

                }).into(findViewById(R.id.imageid))

                //Glide.with(this).load(url).into(findViewById(R.id.imageid))
            },
            {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

        //val url = "https://i.redd.it/8rst6n5ro23a1.jpg"
        //Glide.with(this).load(url).into(findViewById(R.id.imageid))
    }

    fun prevbutton(view: View) {

        Glide.with(this).load(prevurl).into(findViewById(R.id.imageid))
        temp2.visibility=View.GONE
        flag=1

    }

    fun nextmeme(view: View) {

        if(flag==1){

            Glide.with(this).load(currenturl).into(findViewById(R.id.imageid))
            flag=0
        }
        else{
            prevurl=currenturl
            loadMeme()
        }

    }
    fun sharememe(view: View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this cool meme $currenturl")

        val chooser = Intent.createChooser(intent,"Share with..")

        startActivity(chooser)
    }


}