package com.devdreamerx.mind_sync.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devdreamerx.mind_sync.R
import com.devdreamerx.mind_sync.databinding.FragmentHomeBinding
import com.devdreamerx.mind_sync.model.PredictionResponse
import com.devdreamerx.mind_sync.netwokService.RetrofitInstance
import com.devdreamerx.mind_sync.util.PredictionResponseEvent
import com.devdreamerx.mind_sync.viewModel.DataViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.greenrobot.eventbus.EventBus
import retrofit2.await
import java.io.File
import java.io.InputStream
import java.util.UUID

@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // We will change the type later on
//    private var selectedImageUri: Uri? = null
    private var selectedFileUri: Uri? = null
    private lateinit var selectedFileNameTextView: TextView
    private var selectedFileName: String? = null
    private val dataViewModel: DataViewModel by viewModels({ requireActivity() })
    private var loadingDialog: Dialog? = null
    private var isButtonRotated = false
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectedFileNameTextView = binding.selectedFileNameTextView

        binding.addMriScanBtn.setOnClickListener {
            openFilePicker()
        }


        binding.btnSubmit.setOnClickListener {
            val name = binding.edtNameHome.text.toString().trim()
            val age = binding.edtAgeHome.text.toString().toIntOrNull() ?: 0
            val sex = binding.edtSexHome.text.toString().trim()
            val country = binding.homeCountry.text.toString().trim()


            if (name.isNotEmpty() && age > 0 && sex.isNotEmpty() && country.isNotEmpty()) {
                val mriScan = getFilePart()
//                val imageBase64 = getImageBase64()

                if (mriScan != null) {
                    showLoadingDialog()
                    lifecycleScope.launch(Dispatchers.Main) {
                        try {
                            val response = RetrofitInstance.predictionApi.getPrediction(
                                name, age, sex, country, mriScan
                            ).await()

                            Log.d("APIResponse", "Response received: $response")

                            val predictionResponse = PredictionResponse(
                                prediction = response.prediction
                            )

                            EventBus.getDefault().postSticky(PredictionResponseEvent(predictionResponse))

                            dataViewModel.setNameAndFileName(name, selectedFileUri)

                            navigateToDashboardWithDelay()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("APIError", "Error in API request: ${e.message}")
                            showToast("Error in API request: ${e.message}")
                            dismissLoadingDialog()
                        }
                    }
                } else {
                    Log.e("ImageError", "Image file is null")
                    showToast("Image file is null")
                }
            } else {
                showToast("Please fill in all fields")
            }
        }

        return root
    }

    private fun openFilePicker() {

        if (!isButtonRotated) {
            rotateButton(45f){}
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICK_CODE)
        } else {

            rotateButton(0f) {}
            showToast("File selection canceled")
            selectedFileName = null
            selectedFileNameTextView.text = ""
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker() // Open file picker after permission granted
        } else {
            // Handle permission denied case (optional)
        }
    }

    private fun rotateButton(degrees: Float, action: () -> Unit) {
        binding.addMriScanBtn.animate().apply {
            duration = 200 // Animation duration in milliseconds
            rotation(degrees) // Rotate the button to specified degrees
            start()
        }.withEndAction(action) // Execute the action after animation completion

        isButtonRotated = !isButtonRotated // Toggle the rotation state
    }

    private fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Please wait while we load your results.\nDo not press back or close the app.")
            .setCancelable(false)
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edt_text_bg))
            .setView(ProgressBar(requireContext()).apply {
                isIndeterminate = true
                indeterminateDrawable.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.customProgressColor), android.graphics.PorterDuff.Mode.MULTIPLY)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val margin = resources.getDimensionPixelSize(R.dimen.progress_bar_padding)
                setPadding(margin, margin, margin, margin)
            })
            .create()

        dialog.show()
        loadingDialog = dialog
    }


    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun navigateToDashboardWithDelay() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(3000)
            findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard4)
            dismissLoadingDialog()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Handle selected file
            selectedFileUri = data.data
            selectedFileName = selectedFileUri?.let { getFileName(it) }
            selectedFileNameTextView.text = selectedFileName
            showToast("File selected: $selectedFileName")
        } else {
            showToast("No file selected")
        }
    }

//    private fun getFilePart(): MultipartBody.Part? {
//        val uri = selectedFileUri ?: return null
//        val contentResolver = requireContext().contentResolver
//        val inputStream = contentResolver.openInputStream(uri) ?: return null
//        val file = getFileName(uri).let { File(it) } // Creating a File object using the file name
//        file.copyInputStreamToFile(inputStream)
//        val requestBody = file.asRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())
//        return requestBody.let { MultipartBody.Part.createFormData("file", file.name, it) }
//        return TODO("Provide the return value")
//    }

    private fun getFilePart(): MultipartBody.Part? {
        val uri = selectedFileUri ?: return null
        val contentResolver = requireContext().contentResolver

        contentResolver.openInputStream(uri)?.use { inputStream ->
            val requestBody = RequestBody.create(
                "application/octet-stream".toMediaTypeOrNull(),
                inputStream.readBytes() // Read all bytes into a byte array
            )
            val part = MultipartBody.Part.createFormData("mriScan", getFileName(uri), requestBody)
            return part
        }

        return null // Return null if opening the stream fails
    }


    private fun getFileName(uri: Uri): String {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val displayName = cursor?.getString(cursor.getColumnIndexOrThrow("_display_name"))
        cursor?.close()
        return displayName ?: "temp_file"
    }

    companion object {
        private const val FILE_PICK_CODE = 101
        private const val STORAGE_PERMISSION_CODE = 102
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}