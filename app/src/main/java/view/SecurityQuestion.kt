package view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivitySecurityQuestionBinding
import dagger.hilt.android.AndroidEntryPoint
import viewmodel.SecurityViewModel

@AndroidEntryPoint
class SecurityQuestion : AppCompatActivity() {
    private lateinit var binding: ActivitySecurityQuestionBinding
    private val colorList = listOf(
        "Red",
        "Blue",
        "Green",
        "Yellow",
        "Pink",
        "Black",
        "White",
        "Orange"
    )
    private  val viewmodel : SecurityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySecurityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            colorList
        )

        binding.edtAnswer.setAdapter(adapter)
        binding.notice.visibility=View.GONE
        binding.btnConfirm.isEnabled=false

        binding.edtAnswer.setOnClickListener {
            binding.edtAnswer.showDropDown()
        }
        binding.edtAnswer.setOnItemClickListener { parent, view, position, id ->
            val selectedColor = parent.getItemAtPosition(position).toString()
            binding.edtAnswer.setText(selectedColor)
            binding.btnConfirm.isEnabled=true
        }


        binding.btnConfirm.setOnClickListener {
            val colorfav = binding.edtAnswer.text.toString()

            if (colorfav.isEmpty()) {
                binding.notice.text = "Please, enter your favorite color"
                binding.notice.visibility = View.VISIBLE
                return@setOnClickListener
            }


            val isCorrect = viewmodel.verifySecurityAnswer(colorfav)

            if (isCorrect) {
                val pinCode = viewmodel.getSavedPin()
                if (pinCode != null) {
                    showPinDialog(pinCode)
                    binding.notice.visibility = View.GONE
                } else {
                    binding.notice.text = "You haven't set a PINS yet"
                    binding.notice.visibility = View.VISIBLE
                }
            } else {
                binding.notice.text = "Wrong Answer!"
                binding.notice.visibility = View.VISIBLE
            }
        }




    }


    private fun showPinDialog(pin: String?) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_show_pin, null)

        val tvPin1 = dialogView.findViewById<TextView>(R.id.tvPin1)
        val tvPin2 = dialogView.findViewById<TextView>(R.id.tvPin2)
        val tvPin3 = dialogView.findViewById<TextView>(R.id.tvPin3)
        val tvPin4 = dialogView.findViewById<TextView>(R.id.tvPin4)


        tvPin1.text = pin?.getOrNull(0)?.toString() ?: ""
        tvPin2.text = pin?.getOrNull(1)?.toString() ?: ""
        tvPin3.text = pin?.getOrNull(2)?.toString() ?: ""
        tvPin4.text = pin?.getOrNull(3)?.toString() ?: ""

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }




}