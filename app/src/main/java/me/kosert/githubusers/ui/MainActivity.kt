package me.kosert.githubusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import me.kosert.githubusers.R
import me.kosert.githubusers.databinding.ActivityMainBinding
import me.kosert.githubusers.util.Log
import me.kosert.githubusers.util.viewBinding

class MainActivity : AppCompatActivity() {

    private val navController: NavController
        get() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment
            return navHostFragment.navController
        }

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(onFragmentChanged)
    }

    override fun onStop() {
        super.onStop()
        navController.removeOnDestinationChangedListener(onFragmentChanged)
    }

    private val onFragmentChanged = { _: NavController, destination: NavDestination, _: Bundle? ->
        Log.d("Destination changed: $destination")
        val isRoot = destination.id == R.id.listFragment
        binding.backButton.isVisible = !isRoot
    }
}