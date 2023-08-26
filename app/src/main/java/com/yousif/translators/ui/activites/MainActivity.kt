package com.yousif.translators.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.yousif.translators.R
import com.yousif.translators.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_main,
            null,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavController()

    }

    private fun initNavController() {
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragmentContainer.id) as NavHostFragment).navController
//        appBarConfiguration =
//            AppBarConfiguration(setOf(R.id.fragmentTranslation), binding.drawerLayoutMain)
//        //binding.toolbarMain.setupWithNavController(navController, appBarConfiguration)
//        binding.navView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }


}