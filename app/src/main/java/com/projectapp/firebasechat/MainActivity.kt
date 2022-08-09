package com.projectapp.firebasechat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.databinding.ActivityMainBinding
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

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

        appComponent.injectActivity(this)
        Log.d("Mytag", "test str = $testStr")

        val myRef = database.getReference("message")
        myRef.setValue("Hello World from dagger")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.tv.text = snapshot.getValue<String>()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Mytag", error.message)
            }
        })
        openSignInFragment()

    }

    private fun openSignInFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,SignInFragment.newInstance())
            .commit()
    }
}


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun injectActivity(activity: MainActivity)
}

@Module
class AppModule {
    @Provides
    fun provideString(): String {
        return "Hello dagger"
    }

    @Singleton
    @Provides
    fun provideFireDataBase(): FirebaseDatabase {
        return Firebase.database
    }
}
