package com.pedro.bookexchange.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class LibraryFragment : Fragment() {

    private val viewModel by viewModels<LibraryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BookExchangeTheme {
                    val viewState by viewModel.state.collectAsState()
                    LibraryView(
                        state = viewState,
                        onAddBook = {
                            findNavController().navigate(Fragments.AddBook)
                        },
                        onBookClick = {
                            if (!viewState.toSelect) {
                                findNavController().navigate(Fragments.Book(it))
                            } else {
                                findNavController().previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedId", it)

                                findNavController().popBackStack()
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBooks()
    }
}