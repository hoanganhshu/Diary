package com.diary.app.demo.view

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.diary.app.demo.R
import com.diary.app.demo.adapter.GalleryImageAdapter
import com.diary.app.demo.databinding.BottomSheetImageBinding
import com.diary.app.demo.viewmodel.AddDIaryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BottomSheetImagePicker(
    private val onCameraClick: () -> Unit,
    private val onImageSelected: (List<Uri>) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetImageBinding
    private lateinit var adapter: GalleryImageAdapter
    private var itemHeightCalculated = false
    private var hasLoadedImages = false
    private var hasSetHeight = false

    private val viewModel: AddDIaryViewModel by activityViewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            loadGalleryImages()
        } else {
            Toast.makeText(requireContext(), getString(R.string.requestpermission), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetImageBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(true)

        binding.rvGalleryImages.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvGalleryImages.isNestedScrollingEnabled = true

        adapter = GalleryImageAdapter(
            onCameraClick = {
                onCameraClick()
            },
            onImageClick = { uri ->
                // Không dismiss, chỉ update selection
            }
        )

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.rvGalleryImages.adapter = adapter
        binding.rvGalleryImages.setHasFixedSize(true)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.galleryItemHeight.collect { itemHeight ->
                    itemHeight?.let { height ->
                        if (height > 0 && !hasSetHeight) {
                            adapter.itemHeight = height
                            hasSetHeight = true

                            if (!hasLoadedImages) {
                                checkPermissionAndLoadImages()
                            }
                        }
                    }
                }
            }
        }

        binding.choseimage.setOnClickListener {
            val selectedImages = adapter.getSelectedImages()
            if (selectedImages.isNotEmpty()) {
                dismiss()
                onImageSelected(selectedImages)
                adapter.clearSelection()
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn ít nhất một ảnh", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun checkPermissionAndLoadImages() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val hasPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            loadGalleryImages()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    private fun loadGalleryImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val images = withContext(Dispatchers.IO) {
                    loadImagesFromMediaStore()
                }

                if (adapter.itemHeight == 0) {

                    viewModel.galleryItemHeight.value.let { height ->
                        height?.let {
                            if (it > 0) {
                                adapter.itemHeight = height
                            }
                        }
                    }
                }

                // Submit list sau khi đã có height
                adapter.submitList(images)
            } catch (e: Exception) {
                Log.e("BottomSheetImagePicker", "Error loading images", e)
                Toast.makeText(requireContext(), "Lỗi khi load ảnh: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun loadImagesFromMediaStore(): List<Uri> = withContext(Dispatchers.IO) {
        val images = mutableListOf<Uri>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val limit = 50

        requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            var count = 0
            while (cursor.moveToNext() && count < limit) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(contentUri)
                count++
            }
        }

        images
    }
}