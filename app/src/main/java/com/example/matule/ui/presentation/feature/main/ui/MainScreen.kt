package com.example.matule.ui.presentation.feature.main.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.main.ArrivalsCard
import com.example.matule.ui.presentation.shared.main.CatalogCard
import com.example.matule.ui.presentation.shared.main.CatalogProducts
import com.example.matule.ui.presentation.shared.main.PopularCard
import com.example.matule.ui.presentation.shared.main.SearchScreenContent
import com.example.matule.ui.presentation.shared.pullToRefresh.PullRefreshLayout
import com.example.matule.ui.presentation.shared.screen.EmptyContent
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.shared.text.TextFieldWithLeadingAndTrailingIcons
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

private interface MainScreenCallback{
    fun addedInCart(productId: Long){}
    fun openPopularScreen() {}
    fun addedInFavorite(id: Long, isFavorite: Boolean) {}
    fun openCartScreen() {}
    fun openArrivalsScreen() {}
    fun openDetailScreen(productId: Long) {}
    fun openSideMenu() {}
    fun onRefresh() {}
}

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleEvent(MainScreenContract.Event.LoadContent)
    }

    val callback = object : MainScreenCallback {

        override fun openPopularScreen() {
            navController.navigate(AppRouts.POPULAR)
        }

        override fun addedInCart(productId: Long) {
            vm.handleEvent(MainScreenContract.Event.AddToCart(productId))
        }

        override fun addedInFavorite(id: Long, isFavorite: Boolean) {
            vm.handleEvent(MainScreenContract.Event.ToggleProductFavorite(id, isFavorite))
        }

        override fun openCartScreen() {
            navController.navigate(AppRouts.CART)
        }

        override fun openArrivalsScreen() {
            navController.navigate(AppRouts.ARRIVALS)
        }

        override fun openDetailScreen(productId: Long) {
            navController.navigate("${AppRouts.DETAILS}/$productId")
        }

        override fun openSideMenu() {
            navController.navigate(AppRouts.SIDE_MENU)
        }

        override fun onRefresh() {
            vm.handleEvent(MainScreenContract.Event.RefreshContent)
        }
    }

    val onCategorySelected: (Long?) -> Unit = { categoryId ->
        if (categoryId == null || categoryId == 0L) {
            vm.handleEvent(MainScreenContract.Event.SelectCategory(0))
        } else {
            vm.handleEvent(MainScreenContract.Event.SelectCategory(categoryId))
        }
    }

    when (val currentState = state) {
        is MainScreenContract.State.Loaded -> {
            Main(
                callback = callback,
                addedInCart = { productId ->
                    vm.handleEvent(MainScreenContract.Event.AddToCart(productId))
                },
                state = currentState,
                onCategorySelected = onCategorySelected,
                addedInFavorite = { id, isFavorite ->
                    vm.handleEvent(
                        MainScreenContract.Event.ToggleProductFavorite(
                            id,
                            isFavorite
                        )
                    )
                }
            )
        }

        is MainScreenContract.State.Loading -> {
            MainLoadingScreen()
        }

        else -> {}
    }

}

@Composable
private fun Main(
    callback: MainScreenCallback,
    state: MainScreenContract.State.Loaded,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    onCategorySelected: (Long?) -> Unit
) {
    var search by remember { mutableStateOf("") }

    Content(
        openPopularScreen = callback::openPopularScreen,
        openArrivalsScreen = callback::openArrivalsScreen,
        openCartScreen = callback::openCartScreen,
        search = search,
        addedInCart = addedInCart,
        onSearchChange = { search = it },
        openDetailScreen = {
            callback.openDetailScreen(it)
        },
        onRefresh = callback::onRefresh,
        openSideMenu = callback::openSideMenu,
        state = state,
        addedInFavorite = addedInFavorite,
        onCategorySelected = onCategorySelected
    )
}

@Composable
private fun Content(
    search: String,
    onSearchChange: (String) -> Unit,
    addedInCart: (Long) -> Unit,
    state: MainScreenContract.State.Loaded,
    openCartScreen: () -> Unit = {},
    openDetailScreen: (Long) -> Unit = {},
    openPopularScreen: () -> Unit,
    openSideMenu: () -> Unit,
    onRefresh: () -> Unit,
    onCategorySelected: (Long?) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openArrivalsScreen: () -> Unit,
) {

    var catalogScreen by remember { mutableStateOf(false) }
    var searchScreen by remember { mutableStateOf(false) }
    var isSearchPerformed by remember { mutableStateOf(false) }

    val popularProduct = state.popularProducts.filter { it.isPopular }
    val showSearchHeader = searchScreen || search.isNotEmpty()

    val pagerState = rememberPagerState(
        initialPage = calculateInitialPage(state),
        pageCount = { state.categories.size + 1 }
    )
    val searchHistory = remember { mutableStateListOf<String>() }

    val searchResults = remember(search, state.popularProducts) {
        if (search.isNotEmpty()) {
            state.popularProducts.filter { product ->
                product.name.contains(search, ignoreCase = true) ||
                        product.description.contains(search, ignoreCase = true)
            }
        } else {
            emptyList()
        }
    }

    LaunchedEffect(state.selectedCategoryId) {
        val targetPage = when (state.selectedCategoryId) {
            null, 0L -> 0
            else -> {
                val index = state.categories.indexOfFirst { it.id == state.selectedCategoryId }
                if (index >= 0) index + 1 else 0
            }
        }

        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val newCategoryId = when (val currentPage = pagerState.currentPage) {
            0 -> 0L
            else -> state.categories.getOrNull(currentPage - 1)?.id ?: 0L
        }

        if (state.selectedCategoryId != newCategoryId) {
            onCategorySelected(newCategoryId)
        }
    }

    LaunchedEffect(search) {
        if (search.isEmpty()) {
            isSearchPerformed = false
        }
    }

    val screenState = remember(catalogScreen, showSearchHeader) {
        when {
            catalogScreen -> ScreenState.CATALOG
            showSearchHeader -> ScreenState.SEARCH
            else -> ScreenState.MAIN
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(bottom = 50.dp)
    ) {
        Crossfade(
            targetState = screenState,
            modifier = Modifier,
            animationSpec = tween(durationMillis = 700)
        ) { stateScreen ->
            when (stateScreen) {
                ScreenState.CATALOG -> {
                    CustomHeader(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = R.string.category,
                        onBack = { catalogScreen = false },
                        visibleNameScreen = true
                    )
                }

                ScreenState.SEARCH -> {
                    CustomHeader(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = R.string.search,
                        onBack = {
                            searchScreen = false
                            onSearchChange("")
                        },
                        visibleNameScreen = true
                    )
                }

                ScreenState.MAIN -> {
                    CustomHeaderMain(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = R.string.main,
                        cardItem = state.isEnableDot.size,
                        openSideMenu = openSideMenu,
                        openCartScreen = openCartScreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = !catalogScreen,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            Column {
                SearchAndFeature(
                    query = search,
                    searchScreen = searchScreen,
                    onTextChange = { newText ->
                        onSearchChange(newText)
                        searchScreen = newText.isNotEmpty()

                        if (isSearchPerformed && newText != search) {
                            isSearchPerformed = false
                        }
                    },
                    onSearch = {
                        isSearchPerformed = true
                        if (search.isNotEmpty() && !searchHistory.contains(search)) {
                            searchHistory.add(0, search)
                            if (searchHistory.size > 10) {
                                searchHistory.removeAt(searchHistory.lastIndex)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(if (catalogScreen) 28.dp else 22.dp))
            }
        }

        AnimatedVisibility(
            visible = search.isNotEmpty(),
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Bottom
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Bottom
            ) + fadeOut(animationSpec = tween(700))
        ) {
            SearchScreenContent(
                searchQuery = search,
                searchResults = searchResults,
                searchHistory = searchHistory,
                cartItems = state.isEnableDot,
                addedInCart = addedInCart,
                addedInFavorite = addedInFavorite,
                openCartScreen = openCartScreen,
                openDetailScreen = openDetailScreen,
                onSearchItemClick = { searchItem ->
                    onSearchChange(searchItem)
                    searchScreen = true
                    isSearchPerformed = true
                },
                isSearchPerformed = isSearchPerformed
            )
        }

        AnimatedVisibility(
            visible = !showSearchHeader,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CatalogCard(
                    categories = state.categories,
                    onCategorySelected = onCategorySelected,
                    pagerState = pagerState,
                )

                Spacer(modifier = Modifier.height(if (showSearchHeader) 20.dp else 24.dp))

            }
        }

        AnimatedVisibility(
            visible = !showSearchHeader && state.selectedCategoryId != null,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val currentCategory =
                    if (page == 0) null else state.categories.getOrNull(page - 1)


                val productsForPage =
                    remember(currentCategory, state.popularProducts, state.categories) {
                        if (page == 0) {
                            state.popularProducts
                        } else {
                            currentCategory?.let {
                                state.popularProducts.filter { product ->
                                    product.category == currentCategory.slug
                                }
                            } ?: emptyList()
                        }
                    }

                if (productsForPage.isEmpty()) {
                    EmptyContent(
                        icon = R.drawable.ic_orders,
                        emptyText = R.string.empty_arrivals
                    )
                } else {
                    CatalogProducts(
                        addedInCart = addedInCart,
                        openCartScreen = openCartScreen,
                        openDetailScreen = openDetailScreen,
                        addedInFavorite = addedInFavorite,
                        cartItems = state.isEnableDot,
                        products = productsForPage,
                        categories = state.categories,
                        categoryId = state.selectedCategoryId
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !catalogScreen && !showSearchHeader,
            enter = expandVertically(
                animationSpec = tween(700),
                expandFrom = Alignment.Top
            ) + fadeIn(animationSpec = tween(700)),
            exit = shrinkVertically(
                animationSpec = tween(700),
                shrinkTowards = Alignment.Top
            ) + fadeOut(animationSpec = tween(700))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                PopularCard(
                    openPopularScreen = openPopularScreen,
                    addedInCart = addedInCart,
                    openCartScreen = openCartScreen,
                    openDetailScreen = openDetailScreen,
                    popularProducts = popularProduct,
                    addedInFavorite = addedInFavorite,
                    cartItems = state.isEnableDot
                )
                Spacer(modifier = Modifier.height(29.dp))
                ArrivalsCard(
                    openArrivalsScreen = openArrivalsScreen,
                    promotions = state.promotions
                )
            }
        }
    }
}
enum class ScreenState {
    MAIN, CATALOG, SEARCH
}

@Composable
fun SearchAndFeature(
    query: String,
    searchScreen: Boolean,
    onTextChange: (String) -> Unit,
    onSearch: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextFieldWithLeadingAndTrailingIcons(
            query = query,
            onTextChange = onTextChange,
            placeholder = stringResource(R.string.search),
            onSearch = onSearch,
            searchScreen = searchScreen,
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = Colors.hint
                )
            }
        )
        if (!searchScreen) {
            CustomIconButton(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        clip = false,
                        shape = CircleShape
                    ),
                padding = 14.dp,
                icon = R.drawable.ic_sliders,
                backColor = Colors.accent,
                tint = Colors.background
            )
        }
    }
}

@Composable
fun Card(
    text: Int,
    content: @Composable () -> Unit,
    spacer: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(text),
                color = Colors.text,
                style = MatuleTypography.titleMedium
            )
            Text(
                text = stringResource(R.string.see_all),
                color = Colors.accent,
                style = MatuleTypography.bodySmall,
                modifier = Modifier.clickable(onClick = onClick)
            )
        }
        content()
    }
}

private fun calculateInitialPage(state: MainScreenContract.State.Loaded): Int {
    return when (state.selectedCategoryId) {
        null, 0L -> 0
        else -> {
            val index = state.categories.indexOfFirst { it.id == state.selectedCategoryId }
            if (index >= 0) index + 1 else 0
        }
    }
}