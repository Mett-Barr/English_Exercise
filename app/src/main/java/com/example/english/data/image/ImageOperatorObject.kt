package com.example.english.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

const val IMAGE_NAME = "Image_"

object ImageOperatorObject {
    suspend fun addImage(fileNum: String, bitmap: Bitmap, context: Context) {
        val fos = context.openFileOutput(IMAGE_NAME + fileNum, Context.MODE_PRIVATE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }
}

class ImageOperator {
    suspend fun getImage(fileName: String, context: Context): Bitmap {
        val fis = context.openFileInput(fileName)
        return BitmapFactory.decodeStream(fis)

//        val path = context.getFileStreamPath(fileName)
    }

    suspend fun imageTest(context: Context): Bitmap {
        val fis = context.openFileInput(IMAGE_NAME + "1")
        return BitmapFactory.decodeStream(fis)
    }
}