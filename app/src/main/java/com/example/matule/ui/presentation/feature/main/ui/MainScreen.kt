package com.example.matule.ui.presentation.feature.main.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.ui.presentation.feature.catalog.model.Category
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.domain.ui.presentation.feature.arrivals.model.Promotion
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.main.viewmodel.MainScreenContract
import com.example.matule.ui.presentation.feature.main.viewmodel.MainViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.header.CustomHeaderMain
import com.example.matule.ui.presentation.shared.main.CardItem
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.shared.text.TextFieldWithLeadingAndTrailingIcons
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography
import kotlinx.coroutines.delay

private interface MainScreenCallback{
    fun addedInCart(productId: Long){}
    fun openPopularScreen() {}
    fun addedInFavorite(id: Long, isFavorite: Boolean) {}
    fun openCartScreen() {}
    fun openArrivalsScreen() {}
    fun openDetailScreen(productId: Long) {}
    fun openSideMenu() {}
}

@Composable
fun MainScreen(
    vm: MainViewModel = hiltViewModel(),
    navController: NavHostController,
) {

    val state by vm.state.collectAsState()

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
    addedInFavorite: (Long, Boolean) -> Unit
) {
    var search by remember { mutableStateOf("") }

    Content(
        openPopularScreen = callback::openPopularScreen,
        openArrivalsScreen = callback::openArrivalsScreen,
        openCartScreen = callback::openCartScreen,
        search = search,
        addedInCart = addedInCart,
        onSearchChange = { search = it },
        openDetailScreen = callback::openDetailScreen,
        openSideMenu = callback::openSideMenu,
        state = state,
        addedInFavorite = addedInFavorite,
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
    addedInFavorite: (Long, Boolean) -> Unit,
    openArrivalsScreen: () -> Unit,
) {

    var catalogScreen by remember { mutableStateOf(false) }
    var searchScreen by remember { mutableStateOf(false) }

    val popularProduct = state.popularProducts.filter { it.isPopular }

    val showSearchHeader = searchScreen || search.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.background)
            .padding(horizontal = 20.dp)
    ) {
        AnimatedVisibility(
            visible = catalogScreen,
            enter = slideInHorizontally(animationSpec = tween(1700)) { it } + fadeIn(animationSpec = tween(700)),
            exit = slideOutHorizontally(animationSpec = tween(700)) { it } + fadeOut(animationSpec = tween(700))
        ) {
            CustomHeader(
                text = R.string.category,
                onBack = {
                    catalogScreen = false
                },
                visibleNameScreen = true
            )
        }

        AnimatedVisibility(
            visible = showSearchHeader && !catalogScreen,
            enter = slideInHorizontally(animationSpec = tween(1700)) { it } + fadeIn(animationSpec = tween(700)),
            exit = slideOutHorizontally(animationSpec = tween(700)) { it } + fadeOut(animationSpec = tween(700))
        ) {
            Column {
                CustomHeader(
                    text = R.string.search,
                    onBack = {
                        searchScreen = false
                        onSearchChange(search)
                    },
                    visibleNameScreen = true
                )
                Spacer(modifier = Modifier.height(26.dp))
            }
        }

        AnimatedVisibility(
            visible = !catalogScreen && !showSearchHeader,
            enter = slideInHorizontally(animationSpec = tween(1700)) { -it } + fadeIn(animationSpec = tween(700)),
            exit = slideOutHorizontally(animationSpec = tween(700)) { -it } + fadeOut(animationSpec = tween(700))
        ) {
            Column {
                CustomHeaderMain(
                    text = R.string.main,
                    cardItem = state.cartItems.size,
                    openSideMenu = openSideMenu,
                    openCartScreen = openCartScreen
                )
                Spacer(modifier = Modifier.height(21.dp))
            }
        }

        AnimatedVisibility(
            visible = !catalogScreen,
            enter = slideInHorizontally(animationSpec = tween(1700)) { -it } + fadeIn(animationSpec = tween(700)),
            exit = slideOutHorizontally(animationSpec = tween(700)) { -it } + fadeOut(animationSpec = tween(700))
        ) {
            SearchAndFeature(
                query = search,
                searchScreen = searchScreen,
                onTextChange = { newText ->
                    onSearchChange(newText)
                    if (newText.isNotEmpty()) {
                        searchScreen = true
                    } else {
                        searchScreen = false
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        CatalogCard(
            openCatalogScreen = {
                catalogScreen = true
            },
            categories = state.categories
        )
        Spacer(modifier = Modifier.height(24.dp))
        PopularCard(
            openPopularScreen = openPopularScreen,
            addedInCart = addedInCart,
            openCartScreen = openCartScreen,
            openDetailScreen = openDetailScreen,
            popularProducts = popularProduct,
            addedInFavorite = addedInFavorite,
            cartItems = state.cartItems
        )
        Spacer(modifier = Modifier.height(29.dp))
        ArrivalsCard(
            openArrivalsScreen = openArrivalsScreen,
            promotions = state.promotions
        )
    }
}

@Composable
fun SearchAndFeature(
    query: String,
    searchScreen: Boolean,
    onTextChange: (String) -> Unit
) {

    val width by animateFloatAsState(
        targetValue = if (searchScreen) 1f else 0.8f,
        animationSpec = tween(700)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextFieldWithLeadingAndTrailingIcons(
            modifier = Modifier.fillMaxWidth(width),
            query = query,
            onTextChange = onTextChange,
            placeholder = stringResource(R.string.search),
            trailingIcon = {
                AnimatedVisibility(
                    visible = width == 1f
                ) {
                    Row(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(end = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VerticalDivider(
                            thickness = 1.5.dp,
                            color = Colors.subTextDark
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_voice),
                            contentDescription = null,
                            tint = Colors.subTextDark
                        )
                    }
                }
            },
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
private fun PopularCard(
    openPopularScreen: () -> Unit,
    cartItems: Set<Long>,
    popularProducts: List<Product>,
    addedInCart: (Long) -> Unit,
    addedInFavorite: (Long, Boolean) -> Unit,
    openCartScreen: () -> Unit = {},
    openDetailScreen: (Long) -> Unit = {}
) {
    val products = popularProducts.take(2)

    Card(
        text = R.string.popular,
        spacer = 30.dp,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                products.forEach { product ->

                    val isInCart = cartItems.contains(product.id)

                    CardItem(
                        cardName = product.name,
                        cardImage = product.images.firstOrNull() ?: "",
                        money = product.price,
                        addedInCart = { addedInCart(product.id) },
                        liked = product.isFavorite,
                        cartIcon = isInCart,
                        addedInFavorite = { addedInFavorite(product.id, product.isFavorite) },
                        openCartScreen = openCartScreen,
                        openDetailScreen = { openDetailScreen(product.id) }
                    )
                }
            }
        },
        onClick = openPopularScreen
    )
}

@Composable
fun CatalogCard(
    openCatalogScreen: () -> Unit,
    categories: List<Category>
) {

    val categoriesList = remember(categories) {
        listOf("Все") + categories.map { it.name }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(19.dp)
    ) {
        Text(
            text = stringResource(R.string.category),
            color = Colors.text,
            style = MatuleTypography.titleMedium
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categoriesList.forEach { category ->
                Item(
                    text = category,
                    onClick = openCatalogScreen
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ArrivalsCard(
    openArrivalsScreen: () -> Unit,
    promotions: List<Promotion>
) {
    var currentIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(promotions) {
        if (promotions.size > 1) {
            while (true) {
                delay(5000)
                currentIndex = (currentIndex + 1) % promotions.size
            }
        }
    }

    Card(
        text = R.string.new_arrivals,
        spacer = 20.dp,
        content = {
            if (promotions.isNotEmpty()) {
                val currentPromotion = promotions[currentIndex]

                AnimatedContent(
                    targetState = currentPromotion,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = tween(1500),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) with slideOutHorizontally(
                            animationSpec = tween(1500),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    }
                ) { promotion ->
                    Arrivals(
                        arrivalsImage = promotion.image,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
        },
        onClick = openArrivalsScreen
    )
}

@Composable
fun Arrivals(
    modifier: Modifier = Modifier,
    arrivalsImage: String
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .fillMaxWidth()
            .background(color = Colors.block, shape = RoundedCornerShape(16.dp))
            .height(95.dp)
    ){
        AsyncImage(
            model = arrivalsImage,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(1f)
        )
    }
}

@Composable
private fun Card(
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
            modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun Item(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(108.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                clip = false
            )
            .background(color = Colors.block, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Colors.text,
            style = MatuleTypography.bodySmall,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(top = 11.dp, bottom = 17.dp)
        )
    }
}