package com.pedro.bookexchange.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pedro.bookexchange.Fragments
import com.pedro.bookexchange.ui.theme.BookExchangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewState by viewModel.viewState.collectAsState()
                BookExchangeTheme {
                    SearchView(viewState, { viewModel.searchBook(it) }, {
                        findNavController().navigate(Fragments.BookProposal(it))
                    })
                }
            }
        }
    }
}
