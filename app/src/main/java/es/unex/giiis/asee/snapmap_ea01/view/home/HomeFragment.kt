package es.unex.giiis.asee.snapmap_ea01.view.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    //List of photos
    private var _photos: List<Photo> = emptyList()
    private var _loadedPhotos: Map<Long, ImageView> = emptyMap()

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: HomeFragmentViewModel by viewModels { HomeFragmentViewModel.Factory }

    //GoogleMaps variables
    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val ZOOM_LEVEL = 15f
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            observePhotos()
        }
    }

    private fun observePhotos() {
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            _photos = photos
            lifecycleScope.launch {
                getPhotos()
                loadMap()
            }
        }
    }

    private fun loadMap() {
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style_dark
                    )
                )
                if (!success) {
                    // Handle map style load failure here
                }
            } catch (e: Resources.NotFoundException) {
                // Handle exception
            }
        } else {
            try {
                val success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style_light
                    )
                )
                if (!success) {
                    // Handle map style load failure here
                }
            } catch (e: Resources.NotFoundException) {
                // Handle exception
            }
        }

        // Habilita la capa de ubicación para mostrar el punto azul
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
            // Si los permisos ya están habilitados, obtén la ubicación actual
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    // Obtén la latitud y longitud de la ubicación actual
                    val latLng = LatLng(it.latitude, it.longitude)

                    // Mueve la cámara del mapa a la ubicación actual y establece un zoom
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
                    mMap?.uiSettings?.isMyLocationButtonEnabled = false
                    mMap?.isMyLocationEnabled = true

                }
            }

            // Habilita la capa de ubicación
            mMap?.isMyLocationEnabled = true

            mMap?.setOnMarkerClickListener { marker ->
                val photoId = marker.tag as String?
                val action = HomeFragmentDirections.actionHomeFragmentToImageFragment(photoId = photoId!!.toLong())
                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                navController.navigate(action)

                true
            }
        }
        setUpPhotos()
    }

    private fun getPhotos() {
        for (photo in _photos) {
            val image = ImageView(requireContext())

            // Utiliza SimpleTarget para esperar la carga de la imagen antes de proceder
            Glide.with(requireContext())
                .asBitmap()
                .load(photo.photoURL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        // La imagen está lista, ahora puedes proceder con la copia
                        image.setImageBitmap(resource)
                        _loadedPhotos += Pair(photo.photoId!!, image)
                        Log.d("getPhotos", "Loaded image for photoId ${photo.photoId}")
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Manejar la limpieza, si es necesario
                        Log.d("getPhotos", "Image cleared for photoId ${photo.photoId}")
                    }
                })
        }
    }


    @SuppressLint("ResourceType")
    private fun setUpPhotos() {
        lifecycleScope.launch {
            try {
                // Coloca las fotos en el mapa
                for (photo in _photos) {
                    val mapMarker = layoutInflater.inflate(R.layout.picture_map_marker, null)
                    val cardView = mapMarker.findViewById<View>(R.id.markerCardView)
                    val username = cardView.findViewById<TextView>(R.id.info_window_title)
                    val image = cardView.findViewById<ImageView>(R.id.iVPhoto)

                    // Todo: Obtener el nombre de usuario del propietario de la foto e insertarlo al username
                    //val ownerUsername = viewModel.getOwnerPhoto(photo.owner!!)

                    val ownerUsername = viewModel.getOwnerPhoto(photo.owner!!)

                    // Actualiza la interfaz de usuario en el hilo principal
                    username.text = ownerUsername

                    val loadedImage = _loadedPhotos[photo.photoId]
                    if (loadedImage != null) {
                        try {
                            copyImageContent(loadedImage, image)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // Imprimir información de depuración
                            Log.e("SetUpPhotos", "Error copying image content: ${e.message}")
                        }
                    } else {
                        // Manejar el caso en que la imagen cargada es nula
                        // Puedes establecer un recurso de imagen predeterminado o realizar alguna acción apropiada
                        image.setImageResource(R.drawable.baseline_person_24)
                        // Imprimir información de depuración
                        Log.e("SetUpPhotos", "Loaded image is null for photoId: ${photo.photoId}")
                    }

                    val bitmap = Bitmap.createScaledBitmap(
                        viewToBitmap(cardView)!!,
                        cardView.width,
                        cardView.height,
                        false
                    )
                    val smallerMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

                    val marker = MarkerOptions()
                        .position(LatLng(photo.lat, photo.lon))
                        .icon(smallerMarkerIcon)
                    mMap?.addMarker(marker)?.tag = photo.photoId.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Imprimir información de depuración
                Log.e("SetUpPhotos", "Error in setUpPhotos: ${e.message}")
            }
        }
    }

    //Convierte una vista a un bitmap para poder cargarlo en el mapa
    private fun viewToBitmap (view: View): Bitmap? {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)

        return bitmap
    }

    private fun copyImageContent(sourceImageView: ImageView, destinationImageView: ImageView) {
        // Obtener el contenido del ImageView de origen
        val drawable = sourceImageView.drawable

        // Verificar si el drawable no es nulo y es un BitmapDrawable
        if (drawable != null && drawable is BitmapDrawable) {
            // Si no es nulo y es un BitmapDrawable, realizar el cast y obtener el bitmap
            val bitmap = drawable.bitmap

            // Establecer el contenido en el ImageView de destino
            destinationImageView.setImageBitmap(bitmap)
        } else {
            // Manejar el caso donde el drawable es nulo o no es un BitmapDrawable
            Log.e("CopyImageContent", "Drawable is null or not a BitmapDrawable")
            // Puedes establecer un recurso de imagen predeterminado o hacer cualquier otra acción aquí
        }
    }
}