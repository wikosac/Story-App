package id.wikosac.storyapp.ui.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.databinding.FragmentUploadBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private val viewModel : UploadViewModel by viewModels()
    private lateinit var currentPhotoPath: String


    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            reqCameraPermission()
        }
    }

    private fun reqCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This feature requires camera permission",
            REQUEST_CODE_PERMISSIONS,
            Manifest.permission.CAMERA
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Permission granted!", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun permissionCheck() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val tokenPref = sharedPreferences.getString("TOKEN", "").toString()
        if (!permissionCheck()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        with(binding) {
            btnCamera.setOnClickListener {
                if (hasCameraPermission()) openCam() else reqCameraPermission()
            }
            btnGallery.setOnClickListener { openGallery() }
            btnUpload.setOnClickListener { upImg(tokenPref) }
        }
    }

    private fun openCam() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri =
                FileProvider.getUriForFile(
                    requireActivity(), "id.wikosac.storyapp.ui.upload", it
                )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun upImg(token: String) {
        if (getFile != null) {
            val desc = binding.descView.text.toString()
            if (desc.isBlank()) {
                Toast.makeText(requireContext(), "Please fill in the description", Toast.LENGTH_SHORT).show()
                return
            }

            val file = resizeImg(getFile as File)
            val description = desc.toRequestBody("text/plain".toMediaType())
            val reqImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imgMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, reqImageFile)

            viewModel.upload(token, imgMultipart, description)
            viewModel.message.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                if (it.equals("Story created successfully")) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please insert an image file first", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val imgTaken = File(currentPhotoPath)
            imgTaken.let { file ->
                getFile = file
                rotateFile(file)
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myImgFile = it.data?.data as Uri
            myImgFile.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                rotateFile(myFile)
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}