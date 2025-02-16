package com.pedro.bookexchange.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.ui.theme.BookExchangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                BookExchangeTheme {
                    val viewState by viewModel.state.collectAsState()
                    if (viewState.userName.isBlank()) {
                        Surface { }
                    } else {
                        ProfileView(
                            viewState = viewState,
                            onBookClick = {
                                if (viewState.showOptions) {
                                    findNavController()
                                        .navigate(Fragments.Book(it))
                                } else {
                                    findNavController()
                                        .navigate(Fragments.BookProposal(it))
                                }
                            }
                        ) { viewModel.logout() }
                    }
                }
            }
        }
    }
}