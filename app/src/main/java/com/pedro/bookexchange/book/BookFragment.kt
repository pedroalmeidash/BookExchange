package com.pedro.bookexchange.book

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pedro.bookexchange.ui.theme.BookExchangeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.optionals.getOrNull

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val viewModel by viewModels<BookViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.navigateBack.collect {
                if(it) findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                BookExchangeTheme {
                    val viewState by viewModel.state.collectAsState()
                    viewState.getOrNull()?.let {
                        BookScreen(it.second, it.first) { availableForProposal ->
                            viewModel.updateBook(availableForProposal)
                        }
                    } ?: Surface {  }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBook()
    }
}