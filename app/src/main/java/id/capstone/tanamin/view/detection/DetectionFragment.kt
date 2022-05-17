package id.capstone.tanamin.view.detection

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import id.capstone.tanamin.R
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.CustomAlertLogoutBinding
import id.capstone.tanamin.databinding.FragmentDetectionBinding
import id.capstone.tanamin.utils.createFile
import id.capstone.tanamin.utils.rotateBitmap
import id.capstone.tanamin.view.detectionresult.DetectionResultActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetectionFragment : Fragment() {
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()
        setupPermissions()
        binding.captureImage.setOnClickListener {
            takePhoto()
        }
        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_error_24)
                    ?.let { showDialogPermission(getString(R.string.permission_negative), it) }
            }else{
                startCamera()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_open_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireActivity().application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.failed_capture),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val result = rotateBitmap(
                        BitmapFactory.decodeFile(photoFile.path),
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    showDialogResult(result)
                }
            }
        )
    }

    private fun showDialogPermission(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(requireContext()).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.closeButton.text = getString(R.string.close_button)
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun showDialogResult(icon: Bitmap) {
        val builder = AlertDialog.Builder(requireContext()).create()
        val bindAlert: CustomAlertLogoutBinding = CustomAlertLogoutBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = getString(R.string.no_data)
        bindAlert.cancelButton.text = getString(R.string.close_button)
        bindAlert.logoutConfirm.text = getString(R.string.see_detail)
        bindAlert.imageView5.setImageBitmap(icon)
        bindAlert.cancelButton.setOnClickListener {
            builder.dismiss()
        }
        bindAlert.logoutConfirm.setOnClickListener {
            val intent = Intent(requireContext(), DetectionResultActivity::class.java)
            startActivity(intent)
            builder.dismiss()
        }
        builder.show()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}