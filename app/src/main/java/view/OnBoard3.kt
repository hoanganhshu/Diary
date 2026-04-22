package view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diary.app.demo.R
import com.diary.app.demo.databinding.ActivityOnBoard3Binding
import com.google.android.material.card.MaterialCardView

class OnBoard3 : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoard3Binding
    private lateinit var options:List<MaterialCardView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoard3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

             options=listOf(binding.op1,binding.op2,binding.op3,binding.op4,binding.op5)
             binding.btnContinue.isEnabled = false
            options.forEach {
                    card->card.setOnClickListener {
                selectOption(card)
            }

            }
            binding.btnContinue.setOnClickListener {
                var intent= Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        binding.tvSkip.setOnClickListener {
            var intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        }

    private fun selectOption(selected: MaterialCardView) {
        options.forEach { card ->
            card.isChecked = card == selected
            updateCardStyle(card, card == selected)
        }
        binding.btnContinue.isEnabled=true
    }
    private fun updateCardStyle(card: MaterialCardView, isSelected: Boolean) {
        if (isSelected) {
            card.strokeColor = getColor(R.color.purple_500)
            card.setCardBackgroundColor(getColor(R.color.purple_500))
        } else {
            card.strokeColor = getColor(R.color.gray_500)
            card.setCardBackgroundColor(getColor(android.R.color.transparent))
        }
    }
}