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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.domain.ui.presentation.feature.popular.model.Product
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
import kotlinx.coroutines.launch
import kotlin.compareTo
import kotlin.text.contains

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

    when (val currentState = state) {
        is MainScreenContract.State.Loaded -> {
            Main(
                callback = callback,
                addedInCart = { productId ->
                    vm.handleEvent(MainScreenContract.Event.AddToCart(productId))
                },
                state = currentState,
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
) {
    var search by remember { mutableStateOf("") }
    Content(
        openPopularScreen = callback::openPopularScreen,
        openArrivalsScreen = callback::openArrivalsScreen,
        openCartScreen = callback::openCartScreen,
        addedInCart = addedInCart,
        openDetailScreen = {
            callback.openDetailScreen(it)
        },
        onRefresh = callback::onRefresh,
        openSideMenu = callback::openSideMenu,
        state = state,
        search = search,
        addedInFavorite = addedInFavorite,
        onSearchChange = {
            search = it
        }
    )
}

@Composable
private fun Content(
    addedInCart: (Long) -> Unit,
    state: MainScreenContract.State.Loaded,
    openCartScreen: () -> Unit = {},
    openDetailScreen: (Long) -> Unit = {},
    openPopularScreen: () -> Unit,
    search: String,
    openSideMenu: () -> Unit,
    onRefresh: () -> Unit,
    onSearchChange: (String) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openArrivalsScreen: () -> Unit,
) {

    var catalogScreen by remember { mutableStateOf(false) }
    var searchScreen by remember { mutableStateOf(false) }
    var isSearchPerformed by remember { mutableStateOf(false) }
    var visibleCategoryItem by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(-1) }
    val scope = rememberCoroutineScope()
    val popularProduct = state.popularProducts.filter { it.isPopular }
    val showSearchHeader = searchScreen || search.isNotEmpty()

    val all = stringResource(R.string.all)
    val catalog = remember(state.popularProducts) {
        listOf(all) + state.popularProducts.map { it.subcategory }.toSet().toList().sorted()
    }

    val pagerState = rememberPagerState( pageCount = { catalog.size })
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

    val columnModifier = when(screenState){
        ScreenState.MAIN -> Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Colors.background)
            .padding(bottom = 100.dp)

        ScreenState.CATALOG, ScreenState.SEARCH -> Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(bottom = 100.dp)
    }

    Column(
        modifier = columnModifier
    ) {
        Header(
            state = state,
            openSideMenu = openSideMenu,
            openCartScreen = openCartScreen,
            onBackSearch = {
                searchScreen = false
                onSearchChange("")
            },
            onBackCategory = {
                visibleCategoryItem = false
                catalogScreen = false
            },
            screenState = screenState,
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatableSearch(
            onSearchChange = { newText ->
                onSearchChange(newText)
                searchScreen = newText.isNotEmpty()

                if (isSearchPerformed && newText != search) {
                    isSearchPerformed = false
                }
            },
            screenState = screenState,
            catalogScreen = catalogScreen,
            search = search,
            onSearch = {
                isSearchPerformed = true
                if (search.isNotEmpty() && !searchHistory.contains(search)) {
                    searchHistory.add(0, search)
                    if (searchHistory.size > 10) {
                        searchHistory.removeAt(searchHistory.lastIndex)
                    }
                }
            },
            searchScreen = searchScreen,
            searchResults = searchResults,
            searchHistory = searchHistory,
            onSearchItemClick = { searchItem ->
                onSearchChange(searchItem)
                searchScreen = true
                isSearchPerformed = true
            },
            addedInCart = addedInCart,
            addedInFavorite = addedInFavorite,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen,
            isSearchPerformed = isSearchPerformed,
            state = state,
        )

        AnimatableCategory(
            showSearchHeader = showSearchHeader,
            state = state,
            screenState = screenState,
            addedInCart = addedInCart,
            addedInFavorite = addedInFavorite,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen,
            pagerState = pagerState,
            isSelected = visibleCategoryItem,
            catalog = catalog,
            onCategorySelected = {
                selectedCategory == it
                catalogScreen = true
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            },
        )
        MainContent(
            catalogScreen = catalogScreen,
            openArrivalsScreen = openArrivalsScreen,
            showSearchHeader = showSearchHeader,
            openPopularScreen = openPopularScreen,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen,
            addedInCart = addedInCart,
            addedInFavorite = addedInFavorite,
            popularProduct = popularProduct,
            state = state
        )
    }
}

@Composable
fun Header(
    state: MainScreenContract.State.Loaded,
    screenState: ScreenState,
    onBackCategory: () -> Unit,
    onBackSearch: () -> Unit,
    openSideMenu: () -> Unit,
    openCartScreen: () -> Unit
) {
    Crossfade(
        targetState = screenState,
        animationSpec = tween(durationMillis = 700)
    ) { stateScreen ->
        when (stateScreen) {
            ScreenState.CATALOG -> {
                CustomHeader(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = R.string.category,
                    onBack = onBackCategory,
                    visibleNameScreen = true
                )
            }

            ScreenState.SEARCH -> {
                CustomHeader(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = R.string.search,
                    onBack = onBackSearch,
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
}
enum class ScreenState {
    MAIN, CATALOG, SEARCH
}


@Composable
fun AnimatableSearch(
    screenState: ScreenState,
    catalogScreen: Boolean,
    search: String,
    searchResults: List<Product>,
    searchHistory: List<String>,
    onSearchItemClick: (String) -> Unit,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit,
    isSearchPerformed: Boolean,
    state: MainScreenContract.State.Loaded,
    searchScreen: Boolean,
    onSearch: (() -> Unit)?,
    onSearchChange: (String) -> Unit
) {
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
                onTextChange = onSearchChange,
                onSearch = onSearch
            )

            Spacer(modifier = Modifier.height(if (catalogScreen) 28.dp else 22.dp))
        }
    }
    when(screenState) {
        ScreenState.SEARCH -> {
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
                    onSearchItemClick = onSearchItemClick,
                    isSearchPerformed = isSearchPerformed
                )
            }
        }

        else -> {}
    }
}
@Composable
fun AnimatableCategory(
    showSearchHeader: Boolean,
    state: MainScreenContract.State.Loaded,
    screenState: ScreenState,
    catalog: List<String>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit,
    onCategorySelected: (Int) -> Unit,
    openDetailScreen: (Long) -> Unit,
    pagerState: PagerState,
    isSelected: Boolean
) {


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
                catalog = catalog,
                onCategorySelected = onCategorySelected,
                pagerState = pagerState,
                isSelected = isSelected
            )

            Spacer(modifier = Modifier.height(if (showSearchHeader) 20.dp else 24.dp))

        }
    }
    when(screenState) {
        ScreenState.CATALOG -> {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier.fillMaxSize(),
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

                    val productsForPage = if (page == 0) {
                        state.popularProducts
                    } else {
                        val subcategory = catalog.getOrNull(page) ?: ""
                        state.popularProducts.filter { it.subcategory == subcategory }
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
                        )
                    }
                }
            }
        }

        else -> {}
    }
}

@Composable
fun MainContent(
    catalogScreen: Boolean,
    openArrivalsScreen: () -> Unit,
    showSearchHeader: Boolean,
    openPopularScreen: () -> Unit,
    openCartScreen: () -> Unit,
    openDetailScreen: (Long) -> Unit,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    popularProduct: List<Product>,
    state: MainScreenContract.State.Loaded

) {
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