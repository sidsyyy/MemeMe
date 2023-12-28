package com.example.mememe

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mememe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var currentImage:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private  lateinit var binding: ActivityMainBinding
    private fun loadMeme(){
        val progress:ProgressBar=findViewById(R.id.progressBar)
        View.VISIBLE.also { progress.visibility = it }
        val nextButton: Button =binding.nextButton
        nextButton.isEnabled=false
        val shareButton:Button=binding.shareButton
        shareButton.isEnabled=false
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme/2"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                currentImage=response.getString("url")
                val memeImageView:ImageView=findViewById(R.id.memeImageView)
                Glide.with(this).load(currentImage).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility=View.GONE
                        nextButton.isEnabled=true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress.visibility=View.GONE
                        nextButton.isEnabled=true
                        shareButton.isEnabled=true
                        return false
                    }
                }).into(memeImageView)
            },
            {
                Toast.makeText(this,"Something is wrong", Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
    fun nextMeme(view: android.view.View) {
        loadMeme()
    }
    fun shareMeme(view: android.view.View) {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Check this meme $currentImage")
        val chooser=Intent.createChooser(intent,"Share this meme via...")
        startActivity(chooser)
    }
}