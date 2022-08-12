package com.projectapp.firebasechat.di

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.R
import com.projectapp.firebasechat.ui.ChatFragment
import com.projectapp.firebasechat.ui.MainActivity
import com.projectapp.firebasechat.ui.SignInFragment
import com.projectapp.firebasechat.viewmodel.SignInViewModel
import dagger.*
import javax.inject.Scope

@Scope
annotation class AppComponentScope

@Scope
annotation class FragmentScope

@AppComponentScope
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun signInFragmentComponentFactory(): SignInFragmentComponent.Factory
    fun chatFragmentComponentFactory(): ChatFragmentComponent.Factory

    fun injectActivity(activity: MainActivity)

}

@FragmentScope
@Subcomponent(modules = [SignInFragmentModule::class])
interface SignInFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): SignInFragmentComponent
    }

    fun inject(fragment: SignInFragment)

}

@FragmentScope
@Subcomponent
interface ChatFragmentComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ChatFragmentComponent
    }

    fun inject(fragment: ChatFragment)
}

@Module
interface AppModule {
    companion object {
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth {
            return Firebase.auth
        }

        @Provides
        fun provideFireDataBase(): FirebaseDatabase {
            return Firebase.database
        }
    }
}

@Module
interface SignInFragmentModule {
    companion object {

        @Provides
        fun provideGoogleSignInClient(activity: AppCompatActivity): GoogleSignInClient {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(activity, gso)
        }
    }
}