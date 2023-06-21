package com.sr.configuration.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sr.configuration.databinding.FragmentLocationBinding
import com.sr.configuration.infrastructure.actualLocationManager


class LocationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val myViewModel : ConfigurationViewModel by activityViewModels()
    var currentLocationMarker: Marker? = null

    // Registrar para la solicitud de permisos múltiples
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val grantedPermissions = mutableListOf<String>()
        val deniedPermissions = mutableListOf<String>()

        // Recorrer el mapa de permisos y clasificarlos como otorgados o denegados
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value
            if (isGranted) {
                grantedPermissions.add(permission)
            } else {
                deniedPermissions.add(permission)
            }
        }

        if (deniedPermissions.isNotEmpty()) {
            Toast.makeText(requireContext(), "Se necesita el permiso de ubicación para continuar", Toast.LENGTH_LONG).show()
            requiredPermission()
        }else{
            if (isLocationEnabled()) {
                // La ubicación está activada en el dispositivo, puedes utilizar los servicios de ubicación aquí
                getRealLocation()
                Toast.makeText(requireContext(), "Permisos aceptados", Toast.LENGTH_LONG).show()
            } else {
                // La ubicación no está activada en el dispositivo, muestra un mensaje al usuario o toma cualquier otra acción apropiada
                showLocationSettings()
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        requiredPermission()



        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null

    }

    private fun requiredPermission(){
        // Lanzar la solicitud de permisos múltiples
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissionLauncher.launch(permissions)
    }

    private fun getRealLocation(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        binding.nextBtn.setOnClickListener{
            actualLocationManager.stopLocation(fusedLocationClient)
            myViewModel.isDashboardActivate.value = true
        }
        actualLocationManager.getActualLocation(fusedLocationClient){location ->

// En tu método de actualización de ubicación:
            val latLng = LatLng(location.first, location.second)
            if (currentLocationMarker == null) {
                currentLocationMarker = googleMap.addMarker(MarkerOptions().position(latLng).title("Mi ubicación"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                currentLocationMarker?.position = latLng
            }

        }


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, 99)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 99){
            getRealLocation()
        }
    }

}