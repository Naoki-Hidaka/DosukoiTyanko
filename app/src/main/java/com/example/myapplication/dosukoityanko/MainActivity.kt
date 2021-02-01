package com.example.myapplication.dosukoityanko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.dosukoityanko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupWithNavController(binding.toolbar, findNavController(R.id.nav_host_main))
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(findNavController(R.id.nav_host_main))
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host_main).navigateUp()
}
