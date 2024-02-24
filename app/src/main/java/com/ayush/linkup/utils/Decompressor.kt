package com.ayush.linkup.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

//Image compressor and Decompressor class
object Decompressor {

    fun compress(uri: Uri, ctx: Context): ByteArray {
        val bmp: Bitmap = MediaStore.Images.Media.getBitmap(
            ctx.contentResolver,
            uri
        )

        val byteStream = ByteArrayOutputStream()

        bmp.compress(Bitmap.CompressFormat.JPEG, Constants.COMPRESSION_QUALITY, byteStream)

        return byteStream.toByteArray()
    }
}