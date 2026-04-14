package com.example.birthdaycalender.viewmodel

//class AuthenticationViewModel : ViewModel() {
//    private val auth = FirebaseAuth.getInstance()
//
//    var user: FirebaseUser? by mutableStateOf(auth.currentUser)
//    var message by mutableStateOf("")
//
//    fun signIn(email: String, password: String) {
//        //viewModelScope.launch {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    user = auth.currentUser
//                    message = ""
//                } else {
//                    user = null
//                    message = task.exception?.message ?: "Unknown error"
//                }
//            }
//        //}
//    }
//
//    fun signOut() {
//        user = null
//        auth.signOut()
//    }
//
//    fun register(email: String, password: String) {
//        viewModelScope.launch {
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        user = auth.currentUser
//                        message = ""
//                    } else {
//                        user = null
//                        message = task.exception?.message ?: "Unknown error"
//                    }
//                }
//        }
//    }
//}