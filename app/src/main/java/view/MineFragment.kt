package view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.diary.app.demo.R
import com.diary.app.demo.databinding.FragmentMineBinding
import com.diary.app.demo.viewmodel.NotificationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


@AndroidEntryPoint
class MineFragment : Fragment() {



    private lateinit var binding : FragmentMineBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMineBinding.inflate(inflater, container, false)


        return binding.root
    }


    override  fun onResume() {
        super.onResume()
        viewModel.loadInitialData()

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MineFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MineFragment().apply {

                }




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewModel.loadInitialData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.alarmTime.collect { triple ->
                        binding.tvNotificationTime.text =
                            triple?.let { (h, m, ampm) -> String.format("%02d:%02d %s", h, m, ampm) }
                                ?: getString(R.string.show_hours)
                    }
                }
                launch {
                    viewModel.isNotificationOn.collect { isOn ->

                        binding.switchNotification.setOnCheckedChangeListener(null)
                        binding.switchNotification.isChecked = isOn
                        binding.switchNotification.setOnCheckedChangeListener { _, checked ->
                            viewModel.setNotificationState(checked)
                        }
                    }
                }
            }}

        binding.cardNotification.setOnClickListener {
            startActivity(Intent(requireContext(), NotificationActivity::class.java))
        }
        binding.cardAppTheme.setOnClickListener {
            val intent= Intent(requireContext(), ThemeActivity::class.java)
            startActivity(intent)
        }
        binding.tvMoodGallery.setOnClickListener {
            val intent =Intent(requireContext(), MoodGallery::class.java)
            startActivity(intent)
        }
        binding.tvPasscode.setOnClickListener {
            val intent =Intent(requireContext(), PassCodeActivity::class.java)
            startActivity(intent)

        }
        binding.tvShare.setOnClickListener {
            shareApp()
        }

        binding.tvRateUs.setOnClickListener {
            showRateUs()
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val intent =Intent(requireContext(), PolicyActivity::class.java)
            startActivity(intent)
        }

        binding.tvLanguage.setOnClickListener {
            val intent =Intent(requireContext(), LanguageActivity::class.java)
            startActivity(intent)
        }



    }


    private fun  showRateUs(){
        val manager = ReviewManagerFactory.create(requireContext())
        val request =manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if(task.isSuccessful){
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(requireActivity(),reviewInfo)
                flow.addOnCompleteListener {

                        if (!isAdded) return@addOnCompleteListener

                        showDialog()
            }
    }
            else{
                showDialog()

            }
    }}
    private fun showDialog(){
    val dialogView =layoutInflater.inflate(R.layout.dialog_rate,null)
        val ratingBar =dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = dialogView.findViewById<TextView>(R.id.btnSubmit)
        val btnLater = dialogView.findViewById<TextView>(R.id.btnLater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnLater.setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val stars = ratingBar.rating.toInt()

                Toast.makeText(
                    requireContext(),
                    "Cảm ơn bạn! Chúng mình sẽ cố gắng cải thiện hơn ",
                    Toast.LENGTH_SHORT
                ).show()



            dialog.dismiss()
        }
        dialog.show()

}

    private fun shareApp() {
        val shareText = "Hãy thử ngay app nhật kí này ! \nhttps://play.google.com/store/apps/}"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(Intent.createChooser(intent, "Chia sẻ ứng dụng qua..."))
    }








}

