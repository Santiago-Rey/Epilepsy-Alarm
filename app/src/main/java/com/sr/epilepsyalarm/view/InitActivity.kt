package com.sr.epilepsyalarm.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.sr.configuration.view.ConfigurationViewModel
import com.sr.epilepsyalarm.databinding.ActivityInitBinding
import com.sr.configuration.util.Constants.keySound


class InitActivity : AppCompatActivity(){
    lateinit var binding: ActivityInitBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val myViewModel: ConfigurationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObserver()
        mainViewModel.readConfiguration(this)
    }

    private fun setObserver() {
        myViewModel.isDashboardActivate.observe(this) {
            if (it) {
                mainViewModel.saveBoolean("first_login_", true,this)
                mainViewModel.sendInitMessage()
                goToMainActivity()
            }
        }
        myViewModel.soundAlarm.observe(this) {

            mainViewModel.saveDouble(keySound, it.toDouble(),this)

        }

        mainViewModel.isFirstLogin.observe(this) {
            if (it) goToMainActivity()
        }
    }



    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}