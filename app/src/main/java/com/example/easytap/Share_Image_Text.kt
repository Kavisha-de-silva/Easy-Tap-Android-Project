package com.example.easytap

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.easytap.databinding.ActivityCustomerProfileBinding
import com.example.easytap.databinding.ActivityShareImageTextBinding
import java.io.File
import java.io.FileOutputStream

class Share_Image_Text : AppCompatActivity() {

    private lateinit var binding: ActivityShareImageTextBinding

    //uri- image to share,pick from galery
    private var imageUri: Uri? = null

    //text to share
    private var textToShare ="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareImageTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //pick iamge
        binding.imageIv.setOnClickListener {
            pickImage()
        }

        //share Text
        binding.shareTextBtn.setOnClickListener {
            textToShare = binding.textEt.text.toString().trim() //get text from edit text
                if (textToShare.isEmpty()){
                    showToast("Enter Text")
                }
                else {
                shareText()
                }
        }

        binding.shareImageBtn.setOnClickListener {
            if (imageUri == null){
                showToast("Pick Image")
            }
            else{
                shareImage()
            }
        }

        binding.shareBothBtn.setOnClickListener {
            textToShare = binding.textEt.text.toString().trim()
            if (textToShare.isEmpty()){
                showToast("Enter text")
            }
            else if (imageUri==null){
                showToast("Pick Image")
            }
            else{
                shareImageText()
            }
        }

    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActionResultLauncher.launch(intent)
    }

    private var galleryActionResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{result ->

            if (result.resultCode == Activity.RESULT_OK){
                //image picked
                    showToast("Image Picke from gallery")
                val intent =result.data
                imageUri =intent!!.data

                binding.imageIv.setImageURI(imageUri) //set image to imageView
            }
                else{
                  //cancelled
                    showToast("Cancelled")
                }
        }
    )

    private fun showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    private fun shareText(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun shareImage(){
        val contentUri =getContentUri()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type="image/png"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun shareImageText(){
        val contentUri =getContentUri()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type="image/png"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getContentUri():Uri?{
        val bitmap: Bitmap //get bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            val source = ImageDecoder.createSource(contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
        else{
         bitmap= MediaStore.Images.Media.getBitmap(contentResolver ,imageUri)
        }

       /* val bitmapDrawable = binding.imageIv.drawable as BitmapDrawable
        bitmap = bitmapDrawable.bitmap*/

        val imagesFolder = File(cacheDir,"images")
        var contentUri: Uri? = null
        try{
            imagesFolder.mkdirs()//create folder if not exists
            val file = File(imagesFolder,"shared_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50,stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(this,"com.example.easytap.fileprovider",file)
        }
        catch (e:Exception){
            showToast("${e.message}")
        }
        return contentUri
    }
}