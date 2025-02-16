package com.pedro.bookexchange.bookproposal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.MainActivity
import com.pedro.bookexchange.ui.theme.BookExchangeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookProposalFragment : Fragment() {

    private val viewModel by viewModels<BookProposalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            findNavController().currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow("selectedId", "")
                ?.collect { addedBookId ->
                    if (!addedBookId.isNullOrBlank()) {
                        viewModel.addBook(addedBookId)
                    }
                }
        }
        lifecycleScope.launch {
            viewModel.navigateToList.collect {
                if (it) {
                    findNavController().popBackStack()
                    findNavController().navigate(MainActivity.Tab.Proposals)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BookExchangeTheme {
                    val viewState by viewModel.state.collectAsState()
                    BookProposalView(
                        bookProposalViewState = viewState,
                        onAdd = {
                            findNavController().navigate(MainActivity.Tab.Library(true))
                        }, onButtonTapped = { viewModel.onButtonTapped() },
                        onUser = {
                            findNavController().navigate(MainActivity.Tab.Profile(it, false))
                        }
                    )
                }
            }
        }
    }
}