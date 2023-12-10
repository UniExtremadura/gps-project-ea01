package es.unex.giiis.asee.snapmap_ea01.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.PhotoURI_DB
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding : FragmentCameraBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: CameraViewModel by viewModels { CameraViewModel.Factory }

    private var photosURI : List<PhotoURI_DB> = emptyList()
    private var photo : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentCameraBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setUpListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }

        subscribeUI()
    }

    private fun subscribeUI(){
        viewModel.photosURI.observe(viewLifecycleOwner) { photos ->
            if (photos.isNotEmpty()) {
                photosURI = photos
                updatePhoto()
            }
        }
    }

    private fun updatePhoto() {
        with(binding){
            //get random photo from photosURI
            val random = (0..<photosURI.size).random()
            photo = photosURI[random].uri
            Glide.with(requireContext())
                .load(photo)
                .placeholder(R.drawable.baseline_access_time_24)
                .into(iVDog)
        }
    }

    private fun setUpListeners() {
        with(binding){
            btnUpload.setOnClickListener{
                uploadPhoto()
            }
        }
    }

    private fun uploadPhoto() {

        //Obtain location
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permisos de ubicación si no están habilitados
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    // Utiliza la ubicación obtenida para crear la foto
                    viewModel.uploadPhoto(photo, lat, lon)
                    updatePhoto()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to retrieve current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}