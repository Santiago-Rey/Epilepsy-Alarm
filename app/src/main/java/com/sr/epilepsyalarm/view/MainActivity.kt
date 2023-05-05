package com.sr.epilepsyalarm.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.sr.configuration.infrastructure.actualLocationManager
import com.sr.configuration.util.Constants
import com.sr.configuration.util.IOptionSelectListener
import com.sr.epilepsyalarm.R
import com.sr.epilepsyalarm.databinding.ActivityMainBinding
import com.sr.epilepsyalarm.infrastructure.ButtonService
import com.sr.epilepsyalarm.infrastructure.LockedReceiver
import com.sr.configuration.util.Constants.channelDescription
import com.sr.configuration.util.Constants.channelId
import com.sr.configuration.util.Constants.channelName
import com.sr.configuration.view.ConfigurationViewModel
import com.sr.configuration.view.SignUpUserViewModel


class MainActivity : AppCompatActivity(), IOptionSelectListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_PERMISSIONS = 123
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 456
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lockedReceiver: LockedReceiver
    private val mainViewModel: MainViewModel by viewModels()
    private val myViewModel: ConfigurationViewModel by viewModels()
    private val userViewModel: SignUpUserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        userViewModel.obtainUser()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        itLocation()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_config, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val headerView = navView.getHeaderView(0)

// Ahora puedes obtener las vistas de la cabecera y establecer sus propiedades
        val usernameTextView: TextView = headerView.findViewById(R.id.textView)
       userViewModel.user.observe(this) {
           usernameTextView.text = it?.name ?: "new User"
       }

        initNotification()
        initListenBlockButton()

        lockedReceiver = LockedReceiver()
        myViewModel.soundAlarm.observe(this) {

            mainViewModel.saveDouble(Constants.keySound, it.toDouble(),this)

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initListenBlockButton(){
        val intent = Intent(this, ButtonService::class.java)
        startForegroundService(intent)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Verificar si se concedieron los permisos
        if (!Settings.canDrawOverlays(this)) {

            // Si el permiso no ha sido concedido, solicitar permisos al usuario
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + packageName)
            )
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
        } else {
            checkNotificationPermission()

        }
    }

    private fun checkPermissions(): Boolean {
        // Verificar si se concedieron los permisos
        val permissionCheck1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SYSTEM_ALERT_WINDOW
        )
        val permissionCheck2 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.DISABLE_KEYGUARD
        )
        val permissionCheckSMS = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        )
        return permissionCheck1 == PackageManager.PERMISSION_GRANTED &&
                permissionCheck2 == PackageManager.PERMISSION_GRANTED &&
                permissionCheckSMS == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            // Verificar si el permiso SYSTEM_ALERT_WINDOW fue concedido
            if (Settings.canDrawOverlays(this)) {
                checkNotificationPermission()
            } else {
                // Si el permiso no fue concedido, mostrar un mensaje de error o realizar otra acción
                Toast.makeText(
                    this,
                    "El permiso SYSTEM_ALERT_WINDOW es necesario para usar esta función",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkNotificationPermission() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

        if (!areNotificationsEnabled) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        }
    }

    fun initNotification(){
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = channelDescription
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // Verificar si los permisos ya han sido concedidos
        if (!checkPermissions()) {
            // Si no se han concedido los permisos, solicitarlos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.DISABLE_KEYGUARD,
                    Manifest.permission.SEND_SMS
                ),
                REQUEST_CODE_PERMISSIONS
            )
        } else {
            checkNotificationPermission()
        }

    }

    fun itLocation(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        actualLocationManager.getActualLocation(fusedLocationClient){ location ->
            mainViewModel.saveDouble("latitude", location.first, this)
            mainViewModel.saveDouble("longitude", location.second, this)
        }

    }

    override fun onOptionSelected(option: Int) {
        lockedReceiver.setPulseCount(option)
    }


}