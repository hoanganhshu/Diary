package com.diary.app.demo.data.repository

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream


class FileRepository(private val context: Context) {
    fun copyImageFromUri(uri : Uri) : File {
        if (uri.scheme == "file") {
            val path = uri.path
            if (path != null) {
                val file = File(path)
                val folder = getAppImageFolder()
                if (file.parentFile?.absolutePath == folder.absolutePath) {
                    return file
                }
            }
        }

        val inputStream = context.contentResolver.openInputStream(uri)

        val folder= getAppImageFolder()
        val fileName="Image_${System.currentTimeMillis()}.jpg"
        val fileImage=File(folder,fileName)

        FileOutputStream(fileImage).use {
            outputStream -> inputStream.use {
                inputStream?.copyTo(outputStream)
            }
        }
        return fileImage
    }
    private fun getAppImageFolder(): File {
        val folder=File(context.filesDir,"my_image")
        if(!folder.exists()){
            folder.mkdir()

        }
        return folder
    }
    fun copyImagesFromUriList(uris: List<Uri>): List<String> {
        val result = mutableListOf<String>()
        for (uri in uris) {
            val file = copyImageFromUri(uri)
            result.add(file.absolutePath)
        }
        return result
    }

}