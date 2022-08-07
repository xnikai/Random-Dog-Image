package me.xnikai.randomdog

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import java.security.Permissions
import java.util.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: DogViewModel by viewModels()
        setContent {
            val dogImage = rememberAsyncImagePainter(model = viewModel.dogImageUrl.value)
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission Accepted: Do something
                    Log.d("ExampleScreen","PERMISSION GRANTED")

                } else {
                    // Permission Denied: Do something
                    Log.d("ExampleScreen","PERMISSION DENIED")
                }
            }
            val context = LocalContext.current
//            val storagePermission = rememberPermissionState(
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color(0xFF191919)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(
                    text = "Random Dog Image",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Card(
                    Modifier
                        .size(400.dp)
                        .padding(25.dp)
                        .background(Color(0xFF191919)),
                        backgroundColor = Color(0xFF2E2E2E),
                        shape = RoundedCornerShape(20.dp)) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(100.dp),color = Color.LightGray)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Image(painter = dogImage, contentDescription = "Dog Image", contentScale = ContentScale.Crop)
                }
                Button(onClick = {
                    viewModel.getDogImage()
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E2E2E), contentColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                    Text(text = "Update Dog")
                }

                val toast =  Toast.makeText(
                    LocalContext.current,
                    "Saved to /Download/RandomDogImages/...",
                    Toast.LENGTH_SHORT
                )
//                if(storagePermission.status.isGranted){
//                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), null)
                    Button(onClick = {
                        viewModel.saveFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), toast)
                        MediaScannerConnection.scanFile(context, arrayOf(Environment.getExternalStorageDirectory().absolutePath), arrayOf("image/png"), null)

//                    sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())))
//                   openFileOutput("dog${UUID.randomUUID()}.png", 0).use {
//                       println(filesDir.path)

////                       val bitmap = BitmapFactory.decodeByteArray()
//
//                   }
                    }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E2E2E), contentColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                        Text(text = "Save Image")

                    }
//                } else {
//                    storagePermission.launchPermissionRequest()
//                }

            }

        }
    }
}