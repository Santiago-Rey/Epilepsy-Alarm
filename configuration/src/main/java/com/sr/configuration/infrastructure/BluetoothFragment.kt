package com.sr.configuration.infrastructure
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.sr.configuration.R
import java.io.IOException
import java.util.*

class BluetoothFragment : Fragment() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var pairedDevices: Set<BluetoothDevice>
    private lateinit var listView: ListView

    companion object {
        const val EXTRA_ADDRESS = "device_address"
    }

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bluetooth, container, false)

        listView = view.findViewById(R.id.listView)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!bluetoothAdapter.isEnabled) {
            val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetooth, 0)
        }

        showPairedDevices()

        return view
    }

    private fun showPairedDevices() {
        pairedDevices = bluetoothAdapter.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()

        if (pairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in pairedDevices) {
                list.add(device)
                Log.i("Device", "${device.name} - ${device.address}")
            }
        } else {
            Log.i("Device", "No paired devices found.")
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(requireContext(), ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }
}

class ControlActivity : AppCompatActivity() {

    private lateinit var bluetoothSocket: BluetoothSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val address = intent.getStringExtra(BluetoothFragment.EXTRA_ADDRESS)

        val device: BluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address)

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            bluetoothSocket.connect()
            Log.i("Bluetooth", "Connected to ${device.name} - ${device.address}")
        } catch (e: IOException) {
            Log.e("Bluetooth", "Connection failed.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            bluetoothSocket.close()
            Log.i("Bluetooth", "Connection closed.")
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error closing connection.")
        }
    }*/
}
