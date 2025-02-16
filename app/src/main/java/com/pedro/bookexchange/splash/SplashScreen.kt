package com.pedro.bookexchange.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.MainActivity
import com.pedro.bookexchange.service.UserService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : Fragment() {

    @Inject
    lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (userService.hasUser()) {
            findNavController().navigate(MainActivity.Tab.Library(false)) {
                popUpTo(Fragments.Splash) {
                    inclusive = true
                }
            }
        } else {
            findNavController().navigate(Fragments.SignIn) {
                popUpTo(Fragments.Splash) {
                    inclusive = true
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return null
    }
}