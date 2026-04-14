//package com.example.birthdaycalender.dependencyinjection
//
//// jakewarton is now official retrofit
//
///*private val json = Json {
//    ignoreUnknownKeys = true
//    isLenient = true
//}*/
//
//val appModules = module {
//    single<BookRepository> { BooksRepositoryImpl(get(), get()) }
//    single { Dispatchers.IO }
//    single { BooksViewModel(get()) }
//    single {
//        Retrofit.Builder()
//            .addConverterFactory(
//                GsonConverterFactory.create()
//                //json.asConverterFactory("application/json; charset=UTF8".toMediaType())
//                // retrofit converter not working, using Gson
//            )
//            .baseUrl("https://anbo-restbookquerystring.azurewebsites.net/api/")
//            .build()
//    }
//    single { get<Retrofit>().create(BooksAPI::class.java) }
//}