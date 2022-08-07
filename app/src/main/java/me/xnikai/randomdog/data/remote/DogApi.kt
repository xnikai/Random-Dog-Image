package me.xnikai.randomdog.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import me.xnikai.randomdog.data.model.DogData
import okhttp3.OkHttpClient
import okhttp3.Request

class DogApi {
    fun getDogImageUrl(): String{
        // https://dog.ceo/api/breeds/image/random
        val client = OkHttpClient()
        val gson = Gson()

        val request = Request.Builder()
            .url("https://dog.ceo/api/breeds/image/random")
            .build()

        val response = client.newCall(request).execute()


        val dogData = gson.fromJson(response.body!!.string(),DogData::class.java)

        return dogData.message
    }

    fun getImageUrlByteArray(url: String): ByteArray {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        return response.body!!.bytes()
    }
}