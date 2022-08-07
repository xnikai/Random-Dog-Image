package me.xnikai.randomdog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import me.xnikai.randomdog.data.remote.DogApi
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.random.Random

class DogViewModel: ViewModel() {
    private val _dogImageUrl = mutableStateOf("")
    val dogImageUrl: State<String> = _dogImageUrl

    private val _imageToSave = mutableStateOf<ByteArray>(ByteArray(0))
    val imageToSave: State<ByteArray> = _imageToSave


    fun getDogImage(){
        viewModelScope.launch(Dispatchers.IO) {
            _dogImageUrl.value = DogApi().getDogImageUrl()
            _imageToSave.value = DogApi().getImageUrlByteArray(dogImageUrl.value)
            this.cancel()
        }
    }

    fun saveFile(path: File, toast: Toast){
            if (imageToSave.value.isNotEmpty()){
                val bitmap = BitmapFactory
                    .decodeByteArray(imageToSave.value,
                        0,
                        imageToSave.value.size)
                val newPath = File(path.absolutePath + "/RandomDogImages")
                newPath.mkdirs()
                val writer = FileOutputStream(File(newPath, "dog${Random.nextInt(100000)}.png"))
//                writer.write(imageToSave.value)
                bitmap.compress(Bitmap.CompressFormat.PNG, 95, writer)
                writer.flush()
                writer.fd.sync()
                writer.close()
                toast.show()
                return
            }

    }

    init {
        getDogImage()

    }
}