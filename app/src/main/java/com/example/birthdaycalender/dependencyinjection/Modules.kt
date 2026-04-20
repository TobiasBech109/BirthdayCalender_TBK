package com.example.birthdaycalender.dependencyinjection

import com.example.birthdaycalender.data.FriendsAPI
import com.example.birthdaycalender.data.FriendsRepository
import com.example.birthdaycalender.data.FriendsRepositoryImpl
import com.example.birthdaycalender.viewmodel.FriendsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModules = module {
    single<FriendsRepository> { FriendsRepositoryImpl(get(), get()) }
    single { Dispatchers.IO }

    single { FriendsViewModel(get()) }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://birthdaysrest.azurewebsites.net/api/")
            .build()
    }
    single { get<Retrofit>().create(FriendsAPI::class.java) }
}