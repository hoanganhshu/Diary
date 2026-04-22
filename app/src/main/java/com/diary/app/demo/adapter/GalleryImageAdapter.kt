package com.diary.app.demo.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diary.app.demo.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

class GalleryImageAdapter(
    private val onCameraClick: () -> Unit,
    private val onImageClick: (Uri) -> Unit
) : RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {

    private val images = mutableListOf<Uri>()
    private val selectedImages = linkedMapOf<Uri, Int>()
    var itemHeight: Int = 0
        set(value) {
            if (field != value && value > 0) {
                field = value
            }
        }

    companion object {
        private const val TYPE_CAMERA = 0
        private const val TYPE_IMAGE = 1
    }

    fun submitList(newImages: List<Uri>) {
        images.clear()
        images.addAll(newImages)
        updateSelectionNumbers()
        notifyDataSetChanged()
    }

    fun getSelectedImages(): List<Uri> = selectedImages.keys.toList()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_CAMERA else TYPE_IMAGE
    }

    private fun getSelectionNumber(uri: Uri): Int? = selectedImages[uri]

    private fun updateSelectionNumbers() {
        val keys = selectedImages.keys.toList()
        keys.forEachIndexed { index, uri ->
            selectedImages[uri] = index + 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery_image, parent, false)

        // Set height ngay khi tạo ViewHolder
        if (itemHeight > 0) {
            val layoutParams = view.layoutParams
            layoutParams.height = itemHeight
            view.layoutParams = layoutParams
        }

        return ImageViewHolder(view, itemHeight)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (position == 0) {
            holder.bindCamera()
        } else {
            val imageUri = images[position - 1]
            val selectionNumber = getSelectionNumber(imageUri)
            holder.bindImage(imageUri, selectionNumber)
        }
    }

    override fun getItemCount(): Int {
        return images.size + 1
    }

    fun clearSelection() {
        selectedImages.clear()
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(
        itemView: View,
        private val fixedHeight: Int
    ) : RecyclerView.ViewHolder(itemView), CoroutineScope {

        private val imgView: ImageView = itemView.findViewById(R.id.imgGallery)
        private val card: MaterialCardView = itemView.findViewById(R.id.cardGallery)
        private val numberimage: MaterialCardView = itemView.findViewById(R.id.numberimage)
        private val numbertext: TextView = itemView.findViewById(R.id.numbertext)
        private var loadJob: Job? = null

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main

        fun bindCamera() {
            // Cancel any pending image load
            loadJob?.cancel()
            imgView.setImageResource(R.drawable.camera)
            imgView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            card.setOnClickListener {
                onCameraClick()
            }
        }

        fun bindImage(uri: Uri, selectionNumber: Int?) {
            // Cancel previous load job
            loadJob?.cancel()

            // Clear previous image to avoid showing wrong image
            imgView.setImageDrawable(null)
            imgView.background = ContextCompat.getDrawable(itemView.context, android.R.color.darker_gray)

            imgView.scaleType = ImageView.ScaleType.CENTER_CROP

            // Load image asynchronously
            loadJob = launch {
                val bitmap = withContext(Dispatchers.IO) {
                    loadBitmap(uri, fixedHeight)
                }

                // Check if view is still bound to this position
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    bitmap?.let {
                        imgView.setImageBitmap(it)
                    } ?: run {
                        imgView.setImageDrawable(null)
                    }
                }
            }

            // Update selection state
            if (selectionNumber != null) {
                card.strokeWidth = itemView.resources.getDimensionPixelSize(R.dimen.image_selected_stroke_width)
                card.strokeColor = MaterialColors.getColor(
                    itemView.context,
                    androidx.appcompat.R.attr.colorPrimary,
                    "GalleryImageAdapter"
                )
                numberimage.visibility = View.VISIBLE
                numbertext.text = selectionNumber.toString()
            } else {
                card.strokeWidth = 0
                card.strokeColor = ContextCompat.getColor(itemView.context, android.R.color.transparent)
                numberimage.visibility = View.GONE
            }

            card.setOnClickListener {
                if (selectedImages.containsKey(uri)) {
                    selectedImages.remove(uri)
                    updateSelectionNumbers()
                    notifyDataSetChanged()
                } else {
                    selectedImages[uri] = selectedImages.size + 1
                    notifyItemChanged(adapterPosition)
                }
                onImageClick(uri)
            }
        }

        private suspend fun loadBitmap(uri: Uri, size: Int): Bitmap? = withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = itemView.context.contentResolver.openInputStream(uri)
                inputStream?.use { stream ->
                    // Decode with inSampleSize to reduce memory
                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                    }
                    BitmapFactory.decodeStream(stream, null, options)

                    // Calculate inSampleSize
                    options.inSampleSize = calculateInSampleSize(options, size, size)
                    options.inJustDecodeBounds = false

                    // Reopen stream and decode
                    val newStream = itemView.context.contentResolver.openInputStream(uri)
                    newStream?.use {
                        BitmapFactory.decodeStream(it, null, options)
                    }
                }
            } catch (e: Exception) {
                Log.e("GalleryImageAdapter", "Error loading bitmap", e)
                null
            }
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2

                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }
    }
}