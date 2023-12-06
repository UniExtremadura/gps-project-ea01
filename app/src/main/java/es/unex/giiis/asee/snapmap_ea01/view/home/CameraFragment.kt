package es.unex.giiis.asee.snapmap_ea01.view.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.PhotoURI_DB
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentCameraBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CameraFragment : Fragment() {
    private var photo = ""
    private lateinit var photosURI : List<PhotoURI_DB>
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding : FragmentCameraBinding
    private lateinit var db: SnapMapDatabase
    private lateinit var repository: Repository
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentCameraBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = SnapMapDatabase.getInstance(requireContext())!!
        repository = Repository.getInstance(db.userDao(), db.photoURIDao(), getNetworkService())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        getListOfPhotos()

        setUpListeners()

        return binding.root
    }

    private fun updatePhoto() {
        with(binding){
            if(!photosURI.isEmpty()) {
                //get random photo from photosURI
                val random = (0..<photosURI.size).random()
                photo = photosURI[random].uri.toString()
                Glide.with(requireContext())
                    .load(photo)
                    .placeholder(R.drawable.baseline_access_time_24)
                    .into(iVDog)
            }
        }
    }

    private fun getListOfPhotos() {
        subscribeUi()
        launchDataLoad { repository.tryUpdateRecentPhotosCache() }
    }

    private fun subscribeUi() {
        repository.photos.observe(viewLifecycleOwner) { photos ->
            updatePhotos(photos)
            updatePhoto()
        }
    }

    private fun updatePhotos(photos: List<PhotoURI_DB>){
        photosURI = photos
    }

    private fun setUpListeners() {
        with(binding){
            btnUpload.setOnClickListener{
                uploadPhoto()
            }
        }
    }

    private fun uploadPhoto() {
        val user = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User

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
                    val photo = Photo(
                        photoId = null,
                        photoURL = photo,
                        owner = user.userId,
                        lat = lat,
                        lon = lon
                    )

                    lifecycleScope.launch {
                        val photoId = db.photoDao().insertPhoto(photo)
                        Log.d("API", "Photo uploaded with id: $photoId")
                    }
                    Toast.makeText(requireContext(), "Photo uploaded", Toast.LENGTH_SHORT).show()
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

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return lifecycleScope.launch {
            try {
                // Add a reload Cache message
                Toast.makeText(requireContext(), "Checking for updates from cache", Toast.LENGTH_SHORT).show()
                block()
            } catch (error: APIError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                Toast.makeText(requireContext(), "Checked successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}