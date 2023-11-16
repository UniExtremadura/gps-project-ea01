package es.unex.giiis.asee.snapmap_ea01.view.home

import android.Manifest
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
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.api.APIError
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentCameraBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment() {
    private var photo = ""
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var binding : FragmentCameraBinding
    private lateinit var db: SnapMapDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCameraBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        db = SnapMapDatabase.getInstance(requireContext())!!

        updatePhoto()
        setUpListeners()

        return binding.root
    }

    private fun updatePhoto() {
        with(binding){
            lifecycleScope.launch {
                try{
                    photo = fetchDog()
                    Log.d("API", "Dog: $photo")
                    Glide.with(requireContext())
                        .load(photo)
                        .placeholder(R.drawable.baseline_access_time_24)
                        .into(iVDog)
                } catch (e: APIError) {
                    Log.e("MainActivity", "Error fetching dog", e)
                }
            }
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
        val user = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User

        //Obtain location
        val location = LocationServices.getFusedLocationProviderClient(requireContext())

        var lat: Double = 0.0
        var long: Double = 0.0

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
        }else{
            location.lastLocation.addOnSuccessListener {
                lat = it.latitude
                long = it.longitude
            }

            val photo = Photo(
                photoId = null,
                photoURL = photo,
                owner = user.userId,
                lat = lat,
                long = long
            )

            lifecycleScope.launch {
                val photoId = db.photoDao().insertPhoto(photo)
                Log.d("API", "Photo uploaded with id: $photoId")
            }
            Toast.makeText(requireContext(), "Photo uploaded", Toast.LENGTH_SHORT).show()
            updatePhoto()
        }
    }

    private suspend fun fetchDog(): String {
        try {
            val gson = GsonBuilder().setLenient().create()
            return getNetworkService().getDog().uri.toString()
        } catch (cause: Throwable) {
            Log.e("API", "Error fetching duck", cause)
            throw APIError("Error fetching duck", cause)
        }
    }
}