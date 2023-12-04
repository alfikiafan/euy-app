package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myapplication.halaman.Pantri
import com.example.myapplication.halaman.Profil
import com.example.myapplication.halaman.Receipe
import com.example.myapplication.halaman.Utama
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuUtama : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(Utama())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_gradient -> {
                    replaceFragment(Pantri())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_receipe -> {
                    replaceFragment(Receipe())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    replaceFragment(Profil())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_utama)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Default fragment
        replaceFragment(Utama())

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}