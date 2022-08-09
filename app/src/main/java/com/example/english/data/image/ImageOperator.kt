package com.example.english.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.english.R

const val IMAGE_NAME = "Image_"

object ImageOperatorObject {
    suspend fun addImage(fileNum: String, bitmap: Bitmap, context: Context) {
        val fos = context.openFileOutput(IMAGE_NAME + fileNum, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }


//    suspend fun addImageByUrl(fileNum: String, url: String, context: Context) {
//        val fos = context.openFileOutput(IMAGE_NAME + fileNum, Context.MODE_PRIVATE)
//                    val bitmap = imageStore(imageUrl, this@MainActivity)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//        fos.flush()
//        fos.close()
//    }

}

class ImageOperator {
    suspend fun getImage(newsId: String, context: Context): Bitmap? {
        return try {
            val fis = context.openFileInput(IMAGE_NAME + newsId)
            BitmapFactory.decodeStream(fis)
        } catch (e: Exception) {
            Log.d("image file not found", "getImage: $newsId")
            BitmapFactory.decodeResource(context.resources, R.drawable.default_image)
//            ImageObj.context = context
//            ImageObj.defaultImage
        }
//        val path = context.getFileStreamPath(fileName)
    }

    suspend fun imageTest(context: Context): Bitmap {
        val fis = context.openFileInput(IMAGE_NAME + "1")
        return BitmapFactory.decodeStream(fis)
    }

//    companion object {
//        val defaultImage = BitmapFactory.decodeResource(context.resources, R.drawable.default_image)
//    }
}

object ImageObj {
//    lateinit var context: Context
//    val defaultImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_image)
}