package com.pedro.bookexchange

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.cloudinary.android.MediaManager
import com.pedro.bookexchange.addbook.AddBookFragment
import com.pedro.bookexchange.book.BookFragment
import com.pedro.bookexchange.bookproposal.BookProposalFragment
import com.pedro.bookexchange.databinding.ActivityMainBinding
import com.pedro.bookexchange.library.LibraryFragment
import com.pedro.bookexchange.profile.ProfileFragment
import com.pedro.bookexchange.proposallist.ProposalListFragment
import com.pedro.bookexchange.search.SearchFragment
import com.pedro.bookexchange.service.UserService
import com.pedro.bookexchange.signin.LoginFragment
import com.pedro.bookexchange.signup.SignUpFragment
import com.pedro.bookexchange.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.HashMap
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    @Inject
    lateinit var userService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        this.handleWindowInsets(viewBinding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(viewBinding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    finish()
                }
            }
        })
        navController.graph = navController.createGraph(Fragments.Splash) {
            fragment<LibraryFragment, Tab.Library>()
            fragment<SearchFragment, Tab.Search>()
            fragment<ProposalListFragment, Tab.Proposals>()
            fragment<ProfileFragment, Tab.Profile>()
            fragment<AddBookFragment, Fragments.AddBook>()
            fragment<BookFragment, Fragments.Book>()
            fragment<BookProposalFragment, Fragments.BookProposal>()
            fragment<LoginFragment, Fragments.SignIn>()
            fragment<SignUpFragment, Fragments.SignUp>()
            fragment<SplashScreen, Fragments.Splash>()
        }

        lifecycleScope.launch {
            userService.currentUser.distinctUntilChanged().collect {
                if (it == null && navController.currentDestination?.route != Fragments.Splash::class.qualifiedName) {
                    navController.navigate(Fragments.Splash) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
                if (it != null && navController.currentDestination?.route != Tab.Library::class.qualifiedName) {
                    navController.navigate(Tab.Library(false)) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        viewBinding.bottomNav.setContent {
            val backStack by navController.currentBackStackEntryAsState()
            val loginScreens = listOf(
                Fragments.Splash::class.qualifiedName,
                Fragments.SignIn::class.qualifiedName,
                Fragments.SignUp::class.qualifiedName,
            )
            if (!loginScreens.contains(backStack?.destination?.route)) {
                BottomNav(navController)
            }
        }
        initCloudinary()
    }

    private fun initCloudinary() {
        val config= HashMap<String, String>()
        config["cloud_name"] = "dfvfudv7i"
        MediaManager.init(this, config)
    }

    @Composable
    private fun BottomNav(
        navController: NavController,
    ) {
        var selectedTab by remember { mutableStateOf<Tab>(Tab.Library(false)) }
        val tabList = listOf(
            Tab.Library(false),
            Tab.Search,
            Tab.Proposals,
            Tab.Profile("", true),
        )
        Column(Modifier.background(Color(0xE6000000))) {
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                tabList.forEach { tab ->
                    Column(
                        modifier = Modifier.clickable {
                            if (selectedTab == tab) return@clickable
                            navController.navigate(tab) {
                                selectedTab = tab
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .size(32.dp),
                            painter = painterResource(tab.drawableRes),
                            contentDescription = tab.label,
                            colorFilter = ColorFilter.tint(Color(0xFFFA8C16)),
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = tab.label,
                            fontSize = 14.sp,
                            fontWeight = if (selectedTab == tab) {
                                androidx.compose.ui.text.font.FontWeight.Bold
                            } else {
                                androidx.compose.ui.text.font.FontWeight.Normal
                            },
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    @Serializable
    sealed class Tab(
        @DrawableRes val drawableRes: Int,
        val label: String,
    ) {
        @Serializable
        data class Library(val select: Boolean) : Tab(R.drawable.book, "Biblioteca")
        @Serializable
        data object Search : Tab(R.drawable.icons8_search, "Busca")
        @Serializable
        data object Proposals : Tab(R.drawable.icons8_box, "Propostas")
        @Serializable
        data class Profile(val userId: String, val isCurrentUser: Boolean,) : Tab(R.drawable.user, "Perfil")
    }

    fun Activity.handleWindowInsets(rootView: View) {
        // This line exposes window inset information to our app code
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // This line re-applies those window insets to our base views
            view.updatePadding(
                left = systemBarInsets.left,
                top = systemBarInsets.top,
                right = systemBarInsets.right,
                bottom = systemBarInsets.bottom,
            )
            ViewCompat.onApplyWindowInsets(rootView, insets)
            WindowInsetsCompat.CONSUMED
        }
    }
}