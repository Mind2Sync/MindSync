package com.devdreamerx.mind_sync.ui.dashboard

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdreamerx.mind_sync.R
import com.devdreamerx.mind_sync.databinding.FragmentDashboardBinding
import com.devdreamerx.mind_sync.util.PredictionResponseEvent
import com.devdreamerx.mind_sync.viewModel.DataViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayInputStream
import java.io.InputStream


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)

        dataViewModel.name.observe(viewLifecycleOwner) { name ->
            Log.d("DataViewModel", "Name from DataViewModel: $name")
            binding.nameResults.text = name
        }

//        dataViewModel.imageBase64.observe(viewLifecycleOwner) { imageBase64 ->
//            Log.d("DataViewModel", "Image Base64 from DataViewModel: $imageBase64")
//            val bitmap = convertBase64ToBitmap(imageBase64)
//            binding.dashboardOCTimg.setImageBitmap(bitmap)
//        }


    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onPredictionResponseEvent(event: PredictionResponseEvent) {
        val predictionResponse = event.predictionResponse
        // Update UI elements with prediction response data
//        binding.predictionYN.text = predictionResponse.prediction.toString()
//        if (predictionResponse.prediction) {
//            binding.predictionYN.text = "YES"
//            binding.predictionYN.setTextColor(ContextCompat.getColor(requireContext(), R.color.customPredictionRed)) // Assuming you have defined the red color in your resources
//        } else {
//            binding.predictionYN.text = "NO"
//            binding.predictionYN.setTextColor(ContextCompat.getColor(requireContext(), R.color.customPredictionGreen)) // Assuming you have defined the green color in your resources
//        }
//        binding.percentage.text = predictionResponse.chances.toString()
//        val percentageString = String.format("%.0f%%", predictionResponse.chances * 100)
//        binding.percentage.text = percentageString
//        binding.txtRecommendations.text = predictionResponse.recommendation

//        val segmentedImageUrl = predictionResponse.segmentedImage
//        if (!segmentedImageUrl.isNullOrEmpty()) {
//            loadImageFromUrl(segmentedImageUrl)
//        }
    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)

        val inputStream: InputStream = ByteArrayInputStream(decodedBytes)
        return BitmapFactory.decodeStream(inputStream)
    }


//    private fun loadImageFromUrl(imageUrl: String) {
//        Glide.with(this)
//            .load(imageUrl)
//            .into(binding.dashboardSegmentedImg)
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        EventBus.getDefault().unregister(this)
    }
}
