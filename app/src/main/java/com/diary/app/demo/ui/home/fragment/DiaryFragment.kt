package com.diary.app.demo.ui.home.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.diary.app.demo.adapter.AdapterDiaries
import com.diary.app.demo.adapter.DiaryCard
import com.diary.app.demo.data.local.DiaryEntity
import com.diary.app.demo.databinding.FragmentDiaryBinding
import com.diary.app.demo.ui.diary.AddDiary
import com.diary.app.demo.service.GetWeatherApi
import com.diary.app.demo.ui.home.viewmodel.HomeViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [DiaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


@AndroidEntryPoint
class DiaryFragment : Fragment() {
    private lateinit var binding : FragmentDiaryBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val weatherVm: GetWeatherApi by viewModels()
    private var currentDiaryList: List<DiaryEntity> = emptyList()
    private val adapter by lazy {
        AdapterDiaries { diary ->
            val intent = Intent(requireContext(), AddDiary::class.java)
            intent.putExtra("diary_id", diary.id)
            intent.putExtra("show", false)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DiaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DiaryFragment().apply {

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        diariesAdapter = DiariesAdapter { diaryEntity ->
//            val intent = Intent(requireContext(), CalendarActivity::class.java)
//            intent.putExtra("diary_id", diaryEntity.id)
//
//            startActivity(intent)
//        }
//        binding.rvDiaries.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvDiaries.adapter = diariesAdapter


        binding.rvDiaries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDiaries.adapter = adapter




        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.diaries.collect { diaryList ->
                    if (diaryList.isNotEmpty()) {
                        val groupedByDay = diaryList.groupByTo(LinkedHashMap()) { it.day }

                        val cards = groupedByDay.map { (_, diaries) ->
                            DiaryCard(diaries = diaries)
                        }

                        adapter.submitList(cards)

                        binding.emtyView.visibility = View.GONE
                        binding.rvDiaries.visibility = View.VISIBLE
                    } else {
                        binding.emtyView.visibility = View.VISIBLE
                        binding.rvDiaries.visibility = View.GONE
                    }
                }
            }
        }







            binding.weathernoti.setOnClickListener {
                askLocationPermission()
            }

        updateWeatherVisibility(hasData = false)
        }
    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grants ->
            val ok = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (ok) getCurrentLocationAndFetchWeather()
            else Toast.makeText(requireContext(), "Hãy cấp quyền vị trí để xem thời tiết", Toast.LENGTH_SHORT).show()
        }

    private fun askLocationPermission() {
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(requireContext()) }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocationAndFetchWeather() {
        fused.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) {
                fetchWeatherByLatLon(loc.latitude, loc.longitude)
            } else {
                val req = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setMaxUpdateAgeMillis(0)
                    .build()
                fused.getCurrentLocation(req, null).addOnSuccessListener { cur ->
                    if (cur != null) fetchWeatherByLatLon(cur.latitude, cur.longitude)
                    else Toast.makeText(requireContext(), "Không lấy được vị trí", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun fetchWeatherByLatLon(lat: Double, lon: Double) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    weatherVm.getWeather(
                        lat = lat.toString(),
                        lon = lon.toString(),
                        apiKey = "c168ed935e3291ad45e7906f036c458c"
                    )
                }
                val cityName = data.name?.takeIf { it.isNotBlank() }
                val temp = data.main?.temp

                if (cityName != null && temp != null) {
                    binding.tvCity.text = cityName.lowercase()
                    binding.tvTemp.text = "${temp.toInt()} °C"
                    updateWeatherVisibility(hasData = true)
                } else {
                    updateWeatherVisibility(hasData = false)
                }
            } catch (e: Exception) {
                updateWeatherVisibility(hasData = false)
                Toast.makeText(requireContext(), "Lỗi gọi API thời tiết", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateWeatherVisibility(hasData: Boolean) {
        val visibility = if (hasData) View.VISIBLE else View.GONE
        binding.tvCity.visibility = visibility
        binding.tvTemp.visibility = visibility
    }
}