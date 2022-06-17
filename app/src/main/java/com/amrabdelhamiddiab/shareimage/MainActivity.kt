package com.amrabdelhamiddiab.shareimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.ShareActionProvider
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuItemCompat
import com.amrabdelhamiddiab.shareimage.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
  //  private var imageBitmap: Bitmap? = null
    private var shareIntent: Intent? = null
    private lateinit var shareActionProvider: androidx.appcompat.widget.ShareActionProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageView = binding.imageView
        val url = "https://via.placeholder.com/600/771796"
        val rightImage = GlideUrl(url, LazyHeaders.Builder().addHeader("User-Agent", "5").build())
        Glide.with(this).asBitmap().load(rightImage).into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                imageView.setImageBitmap(resource)
                  prepareShareIntent(resource)
                attachShareIntentAction()
               // imageBitmap = resource

            }

        })
        binding.button.setOnClickListener {
            startActivity(Intent.createChooser(shareIntent, "Share image"))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val shareItem = menu.findItem(R.id.menu_item_share)
        shareActionProvider =
            MenuItemCompat.getActionProvider(shareItem) as androidx.appcompat.widget.ShareActionProvider
        //.getActionProvider(shareItem) as ShareActionProvider
            attachShareIntentAction()
       // imageBitmap?.let { prepareShareIntent(it) }

        return super.onCreateOptionsMenu(menu)
    }

    private fun prepareShareIntent(bitmap: Bitmap) {
        val uri = getContentUri(bitmap)
        if (uri != null) {
            shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent!!.putExtra(Intent.EXTRA_STREAM, uri).type = "image/*"
          /*  if (shareActionProvider != null)
                shareActionProvider.setShareIntent(intent)*/
            //  startActivity(Intent.createChooser(intent, "Share image"))

        } else {
            Toast.makeText(this, "Error111111111111111", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getContentUri(bitmap: Bitmap): Uri? {
        val imagesFolder = File(cacheDir, "images")
        var contentUri: Uri? = null
        try {
            imagesFolder.mkdirs()// create folder if not exist
            val file = File(imagesFolder, "share_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
            stream.flush()
            stream.close()
            contentUri =
                FileProvider.getUriForFile(this, "com.amrabdelhamiddiab.fileprovider", file)

        } catch (e: Exception) {
            Toast.makeText(this, "Error222222222222222", Toast.LENGTH_SHORT).show()
            Log.d("MA", e.message.toString() + "2222222222222222222222222222222222222222222")
        }
        return contentUri
    }


    private fun attachShareIntentAction() {
        if (shareIntent != null)
            shareActionProvider.setShareIntent(shareIntent)

    }
}