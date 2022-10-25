package com.example.ourstory.ui.page.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ourstory.MainActivity
import com.example.ourstory.R
import com.example.ourstory.core.Status
import com.example.ourstory.databinding.FragmentAddBinding
import com.example.ourstory.request.AddRequest
import com.example.ourstory.utils.Constants.REQUIRED_PERMISSIONS
import com.example.ourstory.utils.createCustomTempFile
import com.example.ourstory.utils.snackBar
import com.example.ourstory.utils.textTrim
import com.example.ourstory.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddViewModel by viewModels()
    private var myFile: File? = null

    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permission()

        binding.btnAdd.setOnClickListener {
            postStory()
        }
        binding.ibCamera.setOnClickListener {
            takeCamera()
        }
        binding.ibGallery.setOnClickListener {
            takeGallery()
        }
        binding.edtDesc.doAfterTextChanged {
            setButton()

        }

        setButton()

    }

    private fun postStory() {
        val desc = binding.edtDesc.textTrim()
        if (myFile != null) {
            val request = AddRequest(myFile!!, desc, lat = null, lon = null)
            addStory(request)
        } else
            binding.root.snackBar(getString(R.string.add_image))

    }

    private fun addStory(request: AddRequest) {
        viewModel.addStory(request).observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnAdd.text = ""
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnAdd.text = getString(R.string.add_story)
                    binding.root.snackBar(res.message ?: getString(R.string.unknown_error))
                }
                Status.SUCCESS -> {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    binding.progressBar.visibility = View.GONE
                    binding.root.snackBar(getString(R.string.add_success))
                }
            }
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted)
                Toast.makeText(requireContext(), getString(R.string.granted), Toast.LENGTH_LONG)
                    .show()
        }

    private fun permission() {
        activity?.let {
            if (hasPermissions(activity as Context, REQUIRED_PERMISSIONS)) {
                Toast.makeText(requireContext(), getString(R.string.granted), Toast.LENGTH_LONG)
                    .show()
            } else {
                permReqLauncher.launch(
                    REQUIRED_PERMISSIONS
                )
            }
        }
    }


    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takeCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        createCustomTempFile(requireContext().applicationContext).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(), getString(R.string.authority), it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun takeGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(this.myFile?.path)
            binding.ivPhoto.setImageBitmap(result)
            imageShowSetup(myFile)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            binding.ivPhoto.setImageURI(selectedImg)
            imageShowSetup(myFile)
        }
    }

    private fun imageShowSetup(myFile: File) {
        this.myFile = myFile
        val imgSize = myFile.length() / 1024
        println(imgSize)
        if (imgSize > 1024)
            binding.tvImgSize.visibility = View.VISIBLE
        else
            binding.tvImgSize.visibility = View.GONE
        binding.ivPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun setButton() {
        val desc = binding.edtDesc.textTrim()

        binding.btnAdd.isEnabled = desc.isNotEmpty()

    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}