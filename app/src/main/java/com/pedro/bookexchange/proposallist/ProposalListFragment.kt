package com.pedro.bookexchange.proposallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pedro.bookexchange.ui.theme.BookExchangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProposalListFragment : Fragment() {

    private val viewModel by viewModels<ProposalListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                BookExchangeTheme {
                    val viewState by viewModel.state.collectAsState()
                    ProposalListView(
                        viewState,
                        onAccept = { viewModel.acceptProposal(it) },
                        onReject = { viewModel.rejectProposal(it) },
                    )
                }
            }
        }
    }
}