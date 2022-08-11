package com.projectapp.firebasechat.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.R
import com.projectapp.firebasechat.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    val binding get() = requireNotNull(_binding)

    @Inject
    lateinit var testStr: String

    @Inject
    lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setUpActionBarIcon()
        openSignInFragment()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out) {
            Firebase.auth.signOut()
            openSignInFragment()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun openSignInFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SignInFragment.newInstance())
            .commit()
    }
}
