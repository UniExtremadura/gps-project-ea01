package es.unex.giiis.asee.snapmap_ea01.view.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.Photo
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    //List of photos
    private lateinit var db: SnapMapDatabase
    private var _photos: List<Photo> = emptyList()

    //GoogleMaps variables
    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val ZOOM_LEVEL = 15f
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        db = SnapMapDatabase.getInstance(requireContext())!!

        getPhotos()

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this) // Configura el callback OnMapReady

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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

                // Abrimos el fragmento de imagen pasando la ID de la foto
                val imageFragment = ImageFragment.newInstance()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, imageFragment)
                    .addToBackStack(null)
                    .commit()

                true
            }
        }

        getPhotos()
        setUpPhotos()
    }

    private fun getPhotos(){
        //Obtiene las fotos de la base de datos
        //TODO: LLamar a método para sólo obtener las fotos de los usuarios que sigue
        lifecycleScope.launch {
            try {
                _photos = db.photoDao().getAllPhotos()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setUpPhotos(){
        //Coloca las fotos en el mapa
        for (photo in _photos){
            val mapMarker = layoutInflater.inflate(R.layout.picture_map_marker, null)
            val cardView = mapMarker.findViewById<View>(R.id.markerCardView)
            val username = cardView.findViewById<TextView>(R.id.info_window_title)

            //Todo: Obtener el nombre de usuario del propietario de la foto e insertarlo al username

            username.text = photo.owner.toString()

            val bitmap = Bitmap.createScaledBitmap(viewToBitmap(cardView)!!, cardView.width, cardView.height, false)
            val smallerMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)

            mMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(photo.lat, photo.lon))
                    .icon(smallerMarkerIcon)
            )
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

}