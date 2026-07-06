package com.diary.app.demo.ui.diary

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.R
import com.diary.app.demo.adapter.ImagesAdapter
import com.diary.app.demo.bottomsheet.BottomSheetBackGround
import com.diary.app.demo.bottomsheet.BottomSheetDialogEmoji
import com.diary.app.demo.bottomsheet.BottomSheetFont
import com.diary.app.demo.databinding.ActivityAddDiaryBinding
import com.diary.app.demo.ui.BaseActivity
import com.diary.app.demo.view.BottomSheetImagePicker
import com.diary.app.demo.ui.diary.viewmodel.AddDIaryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class AddDiary : BaseActivity<ActivityAddDiaryBinding>() {

    private lateinit var imageAdapter: ImagesAdapter

    private var selectedBackground: Int? = null
    private val addDiaryViewModel: AddDIaryViewModel by viewModels()

    private var emoji: Int = R.drawable.emoj1
    private var currentCreatedAtFromLoadedDiary: Long = 0L
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>



    private var styletext: Int? = null
    private var hasUnsavedChanges = false


    override val themeindex: Int = 1
    private var selectedDayKey = ""

    private var id: Int = 0

    private var textcolor: Int = Color.BLACK
    private var selectedBackgroundPosition: Int? = null
    private var textalign: Int = Gravity.START

    data class FontFamilyStyles(
        val regular: Int,
        val italic: Int? = null,
        val bold: Int? = null
    )


    private val fontFamilies = listOf(

        FontFamilyStyles(
            regular = R.font.nunitovariablefont_wght,
            italic = R.font.nunitoitalicvariablefont_wght,
            bold = R.font.nunitobold
        ),

        FontFamilyStyles(
            regular = R.font.amiriregular,
            italic = R.font.amiriitalic,
            bold = R.font.amiribold
        ),

        FontFamilyStyles(
            regular = R.font.merriweatherregular,
            italic = R.font.merriweatheritalic
        ),

        FontFamilyStyles(
            regular = R.font.badscriptregular
        ),

        FontFamilyStyles(
            regular = R.font.cardoregular,
            italic = R.font.cardoitalic,
            bold = R.font.cardobold
        ),

        FontFamilyStyles(
            regular = R.font.leaguegothicregularvariablefont_wdth
        ),

        FontFamilyStyles(
            regular = R.font.greatvibesregular
        ),

        FontFamilyStyles(
            regular = R.font.pacificoregular
        ),

        FontFamilyStyles(
            regular = R.font.comingsoonregular
        ),

        FontFamilyStyles(
            regular = R.font.amaticscregular,
            bold = R.font.amaticscbold
        ),

        FontFamilyStyles(
            regular = R.font.lobsterregular
        ),

        FontFamilyStyles(
            regular = R.font.reemkufivariablefont_wght
        ),

        FontFamilyStyles(
            regular = R.font.dancingscriptvariablefont_wght
        ),

        FontFamilyStyles(
            regular = R.font.cutivemonoregular
        )
    )
    private var photoUri: Uri? = null

    private var currentFontIndex: Int = 0
    private val realbackgroundList = listOf(
        R.drawable.simple1,
        R.drawable.simple2,
        R.drawable.simple3,
        R.drawable.simple4_1,
        R.drawable.simple4_2,
        R.drawable.simple4_3,
        R.drawable.simple4_4,
        R.drawable.simple4_5,
        R.drawable.chosenbg,
        R.drawable.chosenbg_2,
        R.drawable.chosenbg_11,
        R.drawable.chosenbg_12,
        R.drawable.chosenbg_13,
        R.drawable.chosenbg_16,
        R.drawable.chosenbg_1,
        R.drawable.chosenbg_3,
        R.drawable.chosenbg_4,
        R.drawable.chosenbg_5,
        R.drawable.chosenbg_14,
        R.drawable.chosenbg_15,
        R.drawable.chosenbg_9,
        R.drawable.chosenbg_7,
        R.drawable.chosenbg_8,
        R.drawable.chosenbg_6,
    )

    private val previewBackgroundList = listOf(
        R.drawable.simple1,
        R.drawable.simple2,
        R.drawable.simple3,
        R.drawable.simple4_1,
        R.drawable.simple4_2,
        R.drawable.simple4_3,
        R.drawable.simple4_4,
        R.drawable.simple4_5,
        R.drawable.animal1,
        R.drawable.animal2,
        R.drawable.animal3,
        R.drawable.animal4,
        R.drawable.animal5,
        R.drawable.animal6,
        R.drawable.animal7,
        R.drawable.food1,
        R.drawable.food2,
        R.drawable.food3,
        R.drawable.food4,
        R.drawable.food5,
        R.drawable.holiday1,
        R.drawable.holiday_2,
        R.drawable.holiday_3,
        R.drawable.holiday_4,
    )

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>




    private var currentTextStyle: Int = Typeface.NORMAL

    private lateinit var sheet3: BottomSheetDialogEmoji


    override fun getLayoutActivity(): Int = R.layout.activity_add_diary

    override fun initViews() {
        mBinding.cardEntry.setCardBackgroundColor(Color.WHITE)



        mBinding.edtTitle.gravity = textalign
        mBinding.edtContent.gravity = textalign
        val parent = mBinding.root as androidx.constraintlayout.widget.ConstraintLayout
        val set = ConstraintSet()
        set.clone(parent)

        if (textalign== Gravity.CENTER) {

            set.clear(R.id.constraintday, ConstraintSet.START)
            set.clear(R.id.constraintday, ConstraintSet.END)
            set.centerHorizontally(
                R.id.constraintday,
                ConstraintSet.PARENT_ID
            )
        } else {

            set.clear(R.id.constraintday, ConstraintSet.END)
            set.connect(
                R.id.constraintday,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
            set.setMargin(R.id.constraintday, ConstraintSet.START, 24)
        }

        set.applyTo(parent)
        val cal = Calendar.getInstance()
        selectedDayKey = String.format(
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )

        mBinding.btnBack.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (hasUnsavedChanges) {
                    showNotSaveYetBottomSheet()
                } else {
                    finish()
                }
            }
        })
        mBinding.edtTitle.addTextChangedListener {
            hasUnsavedChanges = true
        }
        mBinding.edtContent.addTextChangedListener {
            hasUnsavedChanges = true }




        id = intent.getIntExtra("diary_id", 0)

        imageAdapter = ImagesAdapter()
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)

        val timeMillis = System.currentTimeMillis()


        val date = Date(timeMillis)
        Log.d("BASE_ADAPTER", "${date}")
        val appLocales = AppCompatDelegate.getApplicationLocales()
        val locale = appLocales.get(0) ?: Locale("en", "US")


        val dayFormat = SimpleDateFormat("dd", locale)
        val dayText = dayFormat.format(date)
        Log.d("BASE_ADAPTER", "${dayText}")


        val monthYearFormat = SimpleDateFormat("MMM yyyy", locale)
        val monthYearText = monthYearFormat.format(date)
        Log.d("BASE_ADAPTER", "${monthYearText}")

        mBinding.day.text = dayText
        mBinding.month.text=monthYearText
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

        mBinding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(
                this@AddDiary, LinearLayoutManager.HORIZONTAL, false
            )
        }

        mBinding.constraintday.setOnClickListener {
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }
        datePicker.addOnPositiveButtonClickListener { millis ->
            val calendarUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendarUtc.timeInMillis = millis
            val d = calendarUtc.get(Calendar.DAY_OF_MONTH)
            val y = calendarUtc.get(Calendar.YEAR)
            val m = calendarUtc.get(Calendar.MONTH) + 1

            selectedDayKey = String.format("%04d-%02d-%02d", y, m, d)
            val appLocales = AppCompatDelegate.getApplicationLocales()
            val locale = appLocales.get(0) ?: Locale("en", "US")



            val date = Date(millis)


            val dayFormat = SimpleDateFormat("dd", locale)
            val dayText = dayFormat.format(date)


            val monthYearFormat = SimpleDateFormat("MMM yyyy", locale)
            val monthYearText = monthYearFormat.format(date)

            mBinding.day.text = dayText
            mBinding.month.text=monthYearText
        }

        val gallerylauncher =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
                try {
                    mBinding.rvImages.visibility = View.VISIBLE
                    imageAdapter.submitList(uris)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
         cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && photoUri != null) {
                    try {
                        mBinding.rvImages.visibility = View.VISIBLE
                        val currentList = imageAdapter.images.toMutableList()
                        currentList.add(photoUri!!)
                        imageAdapter.submitList(currentList)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Cần quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show()
            }
        }


        mBinding.bottomNavAddDiary.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_item1 -> {
                    BottomSheetBackGround { previewId ->


                        val index = previewBackgroundList.indexOf(previewId)

                        if (index != -1) {
                            selectedBackgroundPosition = index


                            val realBgResId = realbackgroundList[index]

                            selectedBackground = realBgResId

                            mBinding.root.setBackgroundResource(realBgResId)
                            mBinding.cardEntry.setCardBackgroundColor(Color.TRANSPARENT)

                        }

                    }.show(supportFragmentManager, "BackGroundBottomSheet")
                    true
                }





                R.id.nav_item2 -> {
                    // Tính toán itemHeight trong ViewModel trước khi mở BottomSheet
                    val screenWidth = resources.displayMetrics.widthPixels
                    val horizontalPadding = (20 * resources.displayMetrics.density).toInt()
                    val recyclerViewMargin = (20 * resources.displayMetrics.density).toInt()
                    val recyclerViewWidth = screenWidth - horizontalPadding - recyclerViewMargin
                    addDiaryViewModel.calculateGalleryItemHeight(
                        recyclerViewWidth,
                        resources.displayMetrics.density
                    )

                    BottomSheetImagePicker(
                        onCameraClick = {
                            openCamera()
                        },
                        onImageSelected = { uris ->
                            mBinding.rvImages.visibility = View.VISIBLE
                            val currentList = imageAdapter.images.toMutableList()
                            currentList.addAll(uris)
                            imageAdapter.submitList(currentList)
                        }
                    ).show(supportFragmentManager, "ImagePickerBottomSheet")
                    true
                }



                R.id.nav_item3 -> {
                    sheet3 = BottomSheetDialogEmoji { resId ->
                        emoji = resId
                        mBinding.emoji.setImageResource(resId)

                        mBinding.emoji.background = null


                        mBinding.emoji.visibility = View.VISIBLE
                        addDiaryViewModel.selectEmoji(resId)
                    }
                    sheet3.show(supportFragmentManager, "DiaryBottomSheet")
                    true
                }



                R.id.nav_item5 -> {

                    BottomSheetFont(
                        currentAlign = textalign,
                        currentStyle = currentTextStyle,
                        onFontSelected = { index ->
                            currentFontIndex = index
                            applyFontAndStyle()
                        },
                        onStyleSelected = { style ->
                            currentTextStyle = style
                            applyFontAndStyle()
                        },
                        onselectAlign = { align ->
                            textalign = align
                            mBinding.edtContent.gravity = align
                            mBinding.edtTitle.gravity = align
                            val parent = mBinding.root as androidx.constraintlayout.widget.ConstraintLayout
                            val set = ConstraintSet()
                            set.clone(parent)

                            if (align == Gravity.CENTER) {

                                set.clear(R.id.constraintday, ConstraintSet.START)
                                set.clear(R.id.constraintday, ConstraintSet.END)

                                set.connect(
                                    R.id.constraintday,
                                    ConstraintSet.TOP,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.TOP
                                )

                                set.centerHorizontally(
                                    R.id.constraintday,
                                    ConstraintSet.PARENT_ID
                                )

                            } else if (align == Gravity.START) {
                                set.clear(R.id.constraintday, ConstraintSet.END)

                                set.connect(
                                    R.id.constraintday,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START,
                                    24
                                )

                                set.connect(
                                    R.id.constraintday,
                                    ConstraintSet.TOP,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.TOP
                                )

                            } else {

                                set.clear(R.id.constraintday, ConstraintSet.END)
                                set.connect(
                                    R.id.constraintday,
                                    ConstraintSet.START,
                                    ConstraintSet.PARENT_ID,
                                    ConstraintSet.START
                                )
                                set.setMargin(R.id.constraintday, ConstraintSet.START, 24)
                            }

                            set.applyTo(parent)
                        },
                        onColorSelected = { color ->
                            textcolor = color
                            mBinding.edtContent.setTextColor(color)
                            mBinding.edtTitle.setTextColor(color)
                            mBinding.day.setTextColor(color)
                            mBinding.month.setTextColor(color)
//                            mBinding.txtDate.compoundDrawableTintList = ColorStateList.valueOf(color)

                        }
                    ).show(supportFragmentManager, "FontBottomSheet")

                    true
                }

                else -> false
            }
        }

        mBinding.btnSave.setOnClickListener {
            showSaving()

            lifecycleScope.launch {
                delay(2000)
                saveData()
                hasUnsavedChanges = true

                showDone()
                delay(3000)
                finish()
            }
        }



        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    addDiaryViewModel.selectFont.collect { fontResId ->
                        val typeface =
                            fontResId?.let { ResourcesCompat.getFont(this@AddDiary, it) }
                        mBinding.edtContent.typeface = typeface
                    }
                }

                launch {
                    addDiaryViewModel.textAlign.collect { align ->
                        mBinding.edtTitle.gravity = align
                        mBinding.edtContent.gravity = align
                        val parent = mBinding.root as androidx.constraintlayout.widget.ConstraintLayout
                        val set = ConstraintSet()
                        set.clone(parent)

                        if (align == Gravity.CENTER) {

                            set.clear(R.id.constraintday, ConstraintSet.START)
                            set.clear(R.id.constraintday, ConstraintSet.END)
                            set.centerHorizontally(
                                R.id.constraintday,
                                ConstraintSet.PARENT_ID
                            )
                        } else {

                            set.clear(R.id.constraintday, ConstraintSet.END)
                            set.connect(
                                R.id.constraintday,
                                ConstraintSet.START,
                                ConstraintSet.PARENT_ID,
                                ConstraintSet.START
                            )
                            set.setMargin(R.id.constraintday, ConstraintSet.START, 24)
                        }

                        set.applyTo(parent)
                    }
                }

                launch {
                    addDiaryViewModel.selectColor.collect { color ->
                        color?.let {textcolor=color
                            mBinding.edtContent.setTextColor(it)
                        mBinding.edtTitle.setTextColor(it)
                        mBinding.day.setTextColor(it)
                            Log.d("BASE_ADAPTER", "selectColor.collect triggered - color: $color, current textcolor: $textcolor")
                            mBinding.month.setTextColor(it)}
                    }
                }

                launch {
                    addDiaryViewModel.selectBackGround.collect { bgId ->
                        bgId?.let {
                            selectedBackground = it
                            mBinding.cardEntry.setCardBackgroundColor(Color.TRANSPARENT)
                            mBinding.root.setBackgroundResource(it)
                        }
                    }
                }

                launch {
                    addDiaryViewModel.selectedEmoji.collect { eid ->
                        eid?.let {
                            emoji = it
                            mBinding.emoji.setImageResource(it)
                            mBinding.emoji.background = null
                            mBinding.emoji.visibility = View.VISIBLE

                        }
                    }
                }
            }
        }

        val showItems: Boolean = intent.getBooleanExtra("show", false)
        if (!showItems) {
            mBinding.bottomNavAddDiary.visibility = View.GONE
            mBinding.btnSave.visibility = View.GONE
            loadDiaryDetail()
            showDiaryBottomSheet()
        } else {
            mBinding.bottomNavAddDiary.visibility = View.VISIBLE
            mBinding.btnSave.visibility = View.VISIBLE
        }
    }
    private fun showNotSaveYetBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_not_save_yet, null)
        dialog.setContentView(view)

        val btnDiscard = view.findViewById<View>(R.id.btnCancelConfirm)
        val btnSave = view.findViewById<View>(R.id.btnDeleteConfirm)
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val sheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            sheet?.setBackgroundColor(Color.TRANSPARENT)
            sheet?.let { ViewCompat.setElevation(it, 0f) }
        }

        btnDiscard.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        btnSave.setOnClickListener {
            dialog.dismiss()
            showSaving()
            lifecycleScope.launch {
                delay(2000)
                saveData()
                showDone()
                delay(3000)
                finish()
            }
        }

        dialog.show()
    }
    private fun openCamera() {
        // Check permission trước
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Đã có permission, mở camera
            val photoFile =
                File(getExternalFilesDir(null), "photo_${System.currentTimeMillis()}.jpg")
            photoUri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                photoFile
            )
            photoUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        } else {
            // Chưa có permission, request
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }



    private fun applyFontAndStyle() {
        val family = fontFamilies[currentFontIndex]

        val (fontResId, styleToUse) = when (currentTextStyle) {
            Typeface.ITALIC -> {
                val res = family.italic ?: family.regular
                res to Typeface.ITALIC
            }

            Typeface.BOLD -> {
                val res = family.bold ?: family.regular
                res to Typeface.BOLD
            }

            else -> {
                family.regular to Typeface.NORMAL
            }
        }

        val tf = ResourcesCompat.getFont(this, fontResId)

        mBinding.edtContent.setTypeface(tf, styleToUse)
        mBinding.edtTitle.setTypeface(tf, styleToUse)
        mBinding.day.setTypeface(tf, styleToUse)
        mBinding.month.setTypeface(tf, styleToUse)

        styletext = fontResId
    }

    private fun saveData() {
        val title = mBinding.edtTitle.text.toString()
        val description = mBinding.edtContent.text.toString()


        val uriList = imageAdapter.images
        val theme : Int? = selectedBackground
        val style = styletext ?: 0

        val isEdit = (id != 0)
        val createdAtToUse = if (isEdit) currentCreatedAtFromLoadedDiary else System.currentTimeMillis()

        addDiaryViewModel.saveDiary(
            id = id,
            title = title,
            description = description,
            imageList = uriList,
            theme = theme,
            emoji = emoji,
            day = selectedDayKey,
            style = style,
            color = textcolor,
            align = textalign,
            createdAt = createdAtToUse
        )

        finish()
    }

    private fun showDiaryBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_edit, null)
        dialog.setContentView(view)

        val btnDelete = view.findViewById<MaterialButton>(R.id.btnDelete)
        val btnEdit = view.findViewById<MaterialButton>(R.id.btnEdit)

        dialog.setCanceledOnTouchOutside(true)


        dialog.setOnCancelListener {

            finish()
        }

        btnDelete.setOnClickListener {
            showDiaryBottomSheetConfirm()
            dialog.dismiss()
        }

        btnEdit.setOnClickListener {
            dialog.dismiss()
            mBinding.bottomNavAddDiary.visibility = View.VISIBLE
            mBinding.btnSave.visibility = View.VISIBLE
        }

        dialog.show()
    }
    private fun showSaving() {
        mBinding.loadingOverlay.visibility = View.VISIBLE
        mBinding.progressCircle.visibility = View.VISIBLE
        mBinding.imgDone.visibility = View.GONE
        mBinding.tvLoadingStatus.text = "Saving..."
        mBinding.btnSave.isEnabled = false
    }

    private fun showDone() {
        mBinding.progressCircle.visibility = View.GONE
        mBinding.imgDone.visibility = View.VISIBLE
        mBinding.tvLoadingStatus.text = "Done!"
    }


    private fun showDiaryBottomSheetConfirm() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_confirm_delete, null)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)


        dialog.setOnCancelListener {

            showDiaryBottomSheet()
        }

        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            val sheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            sheet?.setBackgroundColor(Color.TRANSPARENT)
            sheet?.let { ViewCompat.setElevation(it, 0f) }
        }

        val btnDelete = view.findViewById<MaterialButton>(R.id.btnDeleteConfirm)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btnCancelConfirm)

        btnDelete.setOnClickListener {
            addDiaryViewModel.deleteDiary(id = id)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
            showDiaryBottomSheet()
        }

        dialog.show()
    }
    private fun loadDiaryDetail() {
        if (id == 0) return

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addDiaryViewModel.getDiaryById(id).collect { d ->
                    d ?: return@collect

                    currentCreatedAtFromLoadedDiary = d.createdAt

                    mBinding.edtTitle.setText(d.title)
                    mBinding.edtContent.setText(d.description)

                    // Load images
                    if (d.uriListImage.isNotEmpty()) {
                        mBinding.rvImages.visibility = View.VISIBLE
                        imageAdapter.submitList(d.uriListImage)
                    } else {
                        mBinding.rvImages.visibility = View.GONE
                    }

                    // Load background/theme
                    d.theme?.let { bgResId ->
                        selectedBackground = bgResId
                        mBinding.cardEntry.setCardBackgroundColor(Color.TRANSPARENT)
                        mBinding.root.setBackgroundResource(bgResId)
                    }

                    // Load emoji
                    emoji = d.emoji
                    if (d.emoji != 0) {
                        mBinding.emoji.setImageResource(d.emoji)
                        mBinding.emoji.background = null
                        mBinding.emoji.visibility = View.VISIBLE
                    } else {
                        mBinding.emoji.visibility = View.GONE
                    }

                    // Load font style
                    styletext = d.style
                    if (d.style != 0) {
                        try {
                            val tf = ResourcesCompat.getFont(this@AddDiary, d.style)
                            mBinding.edtContent.typeface = tf
                            mBinding.edtTitle.typeface = tf
                            mBinding.day.typeface = tf
                            mBinding.month.typeface = tf
                        } catch (e: Exception) {
                            Log.e("AddDiary", "Error loading font style", e)
                        }
                    }

                    // Load text color
                    textcolor = d.color
                    mBinding.edtContent.setTextColor(d.color)
                    mBinding.edtTitle.setTextColor(d.color)
                    mBinding.day.setTextColor(d.color)
                    mBinding.month.setTextColor(d.color)

                    // Load alignment
                    textalign = d.align
                    mBinding.edtContent.gravity = d.align
                    mBinding.edtTitle.gravity = d.align

                    val parent = mBinding.root as androidx.constraintlayout.widget.ConstraintLayout
                    val set = ConstraintSet()
                    set.clone(parent)
                    if (d.align == Gravity.CENTER) {
                        set.clear(R.id.constraintday, ConstraintSet.START)
                        set.clear(R.id.constraintday, ConstraintSet.END)
                        set.centerHorizontally(
                            R.id.constraintday,
                            ConstraintSet.PARENT_ID
                        )
                    } else {
                        set.clear(R.id.constraintday, ConstraintSet.END)
                        set.connect(
                            R.id.constraintday,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START
                        )
                        set.setMargin(R.id.constraintday, ConstraintSet.START, 24)
                    }
                    set.applyTo(parent)

                    selectedDayKey = d.day

                    try {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        val date = dateFormat.parse(d.day) ?: Date(d.createdAt)
                        Log.d("BASE_ADAPTER", "${d.createdAt}")
                        Log.d("BASE_ADAPTER", "${d.day}")

                        val dayText = SimpleDateFormat("dd", Locale.ENGLISH).format(date)
                        val monthYearText =
                            SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(date)

                        mBinding.day.setText(dayText)
                        mBinding.month.setText(monthYearText)
                    } catch (e: Exception) {
                        val date = Date(d.createdAt)
                        val dayText = SimpleDateFormat("dd", Locale.ENGLISH).format(date)
                        val monthYearText =
                            SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(date)
                        mBinding.day.setText(dayText)
                        mBinding.month.setText(monthYearText)
                    }

                    hasUnsavedChanges = false
                }
            }
        }
    }


}