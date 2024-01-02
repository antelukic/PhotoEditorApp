package com.photoeditor.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.photoeditor.app.databinding.ActivityMainBinding
import com.photoeditor.app.domain.saveimage.SaveImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.setUpBottomNav()
        observe()
        setSupportActionBar(binding.toolbar)
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.saveError.collectLatest {
                if (!it) {
                    Toast.makeText(this@MainActivity, "Image not saved, something went wrong!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Image saved successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.saveAsJpeg -> {
            viewModel.saveImage(this, SaveImage.MimeType.JPEG)
            true
        }

        R.id.saveAsPng -> {
            viewModel.saveImage(this, SaveImage.MimeType.PNG)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun ActivityMainBinding.setUpBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        mainBottomNavigation.setupWithNavController(navController)
    }
}
