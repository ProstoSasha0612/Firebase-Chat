package com.projectapp.firebasechat.di

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.projectapp.firebasechat.ui.MainActivity
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectActivity(activity: MainActivity)
}

@Module
class AppModule {
    @Provides
    fun provideString(): String {
        return "Hello dagger"
    }

    @Provides
    fun provideFireDataBase(): FirebaseDatabase {
        return Firebase.database
    }
}