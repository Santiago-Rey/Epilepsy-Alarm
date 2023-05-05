package com.sr.configuration.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfigurationViewModel: ViewModel() {

    var isDashboardActivate : MutableLiveData<Boolean> = MutableLiveData(false)
    var soundAlarm : MutableLiveData<Int> = MutableLiveData()



}