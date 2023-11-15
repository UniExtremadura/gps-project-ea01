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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), OnMapReadyCallback {

    //List of photos
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

        }

        getPhotos()
        setUpPhotos()

        // Adición de un marcador de prueba
        val mapMarker = layoutInflater.inflate(R.layout.picture_map_marker, null)
        val cardView = mapMarker.findViewById<View>(R.id.markerCardView)
        val bitmap = Bitmap.createScaledBitmap(viewToBitmap(cardView)!!, cardView.width, cardView.height, false)
        val smallerMarkerIcon = BitmapDescriptorFactory.fromBitmap(bitmap)
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(39.46473283509486, -6.38389228558333))
                .icon(smallerMarkerIcon)
        )

    }

    private fun getPhotos(){

        //TODO: Change this to get the photos from the database
        val lat1 = 39.49082391807905
        val lon1 = -6.4061240044765
        val lat2 = 39.44995320116261
        val lon2 = -6.342590539110122

        for (i in 1..10){
            val lat = (lat1 - lat2) * Math.random() + lat2
            val lon = (lon1 - lon2) * Math.random() + lon2
            val photo = Photo("x", lat, lon, 0)
            _photos += photo
        }
    }

    private fun setUpPhotos(){
        //Coloca las fotos en el mapa
        for (photo in _photos){
            val mapMarker = layoutInflater.inflate(R.layout.picture_map_marker, null)
            val cardView = mapMarker.findViewById<View>(R.id.markerCardView)
            val username = cardView.findViewById<TextView>(R.id.info_window_title)
            val imageView = cardView.findViewById<ImageView>(R.id.iVPhoto)
            username.setText("Jose Luis")

            val imageUrl = "https://s1.ppllstatics.com/lasprovincias/www/multimedia/202112/12/media/cortadas/gatos-kb2-U160232243326NVC-1248x770@Las%20Provincias.jpg"

            // Configuración de opciones de carga
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.baseline_thumb_up_24) // Puedes establecer un placeholder mientras se carga la imagen
                .error(R.drawable.baseline_broken_image_24) // Puedes establecer una imagen de error si la carga falla
            //TODO: Fix problem with glide
            Glide.with(this)
                .load(imageUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)



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