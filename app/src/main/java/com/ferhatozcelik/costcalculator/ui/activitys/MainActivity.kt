package com.ferhatozcelik.costcalculator.ui.activitys

import android.os.Bundle
import android.util.Log
import com.ferhatozcelik.costcalculator.databinding.ActivityMainBinding
import com.ferhatozcelik.costcalculator.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate()")
    }

}
