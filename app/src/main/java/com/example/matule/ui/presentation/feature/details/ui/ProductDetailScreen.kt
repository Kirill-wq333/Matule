package com.example.matule.ui.presentation.feature.details.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.ui.presentation.feature.popular.model.Product
import com.example.matule.R
import com.example.matule.ui.presentation.approuts.AppRouts
import com.example.matule.ui.presentation.feature.details.viewmodel.ProductDetailContract
import com.example.matule.ui.presentation.feature.details.viewmodel.ProductDetailViewModel
import com.example.matule.ui.presentation.shared.buttons.CustomButtonForBuy
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.shared.header.CustomHeader
import com.example.matule.ui.presentation.shared.screen.MainLoadingScreen
import com.example.matule.ui.presentation.theme.Colors

private interface ProductDetailScreenCallback{
    fun openCartScreen() {}
    fun onBack() {}
    fun addedInCart(productId: Long) {}
    fun addedInFavorite(id: Long, isFavorite: Boolean) {}
}

@Composable
fun ProductDetailScreen(
    vm: ProductDetailViewModel,
    navController: NavHostController
) {
    val state by vm.state.collectAsState()

    val callback = object : ProductDetailScreenCallback{
        override fun addedInFavorite(id: Long, isFavorite: Boolean) {
            vm.handleEvent(ProductDetailContract.Event.ToggleProductFavorite(id, isFavorite))
        }

        override fun addedInCart(productId: Long) {
            vm.handleEvent(ProductDetailContract.Event.AddToCart(productId))
        }

        override fun openCartScreen() {
            navController.navigate(AppRouts.CART)
        }

        override fun onBack() {
            navController.popBackStack()
        }
    }

    Content(
        state = state,
        callback = callback
    )
}

@Composable
private fun Content(
    state: ProductDetailContract.State,
    callback: ProductDetailScreenCallback
) {

    Column(
        modifier = Modifier
            .background(color = Colors.background)
            .fillMaxSize()
    ) {
        when (state) {
            is ProductDetailContract.State.LoadProduct -> {
                CustomHeader(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = R.string.main,
                    onBack = callback::onBack,
                    visibleNameScreen = true,
                    visibleEndIcon = true,
                    cardItem = state.isEnableDot.size,
                    openScreen = callback::openCartScreen,
                    category = state.product.category,
                    visibleText = false
                )
                Spacer(modifier = Modifier.height(26.dp))
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val isInCart = state.isEnableDot.contains(state.product.id)

                    ProductDetailContent(
                        nameProduct = state.product.name,
                        categoryProduct = state.product.category,
                        price = state.product.price,
                        mainPhoto = state.product.images.first(),
                        photos = state.product.images,
                        description = state.product.description
                    )
                    Actions(
                        visibleInCart = isInCart,
                        liked = state.product.isFavorite,
                        addedInCart = {
                            callback.addedInCart(it)
                        },
                        onLiked = { id, isFavorite ->
                            callback.addedInFavorite(id, isFavorite)
                        },
                        product = state.product
                    )
                }
            }

            is ProductDetailContract.State.Loading -> {
                MainLoadingScreen()
            }
        }

    }
}

@Composable
fun ProductDetailContent(
    nameProduct: String,
    categoryProduct: String,
    price: Double,
    mainPhoto: String?,
    photos: List<String?>,
    description: String
) {
    var isSelected by remember { mutableStateOf(0) }
    val allPhotos = remember(photos, mainPhoto) {
        val list = mutableListOf<String?>()

        list.addAll(photos)
        list
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = nameProduct,
                color = Colors.text,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
            )
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = categoryProduct,
                color = Colors.hint,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.money, price),
                color = Colors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        MainPhotoProduct(
            mainPhoto = allPhotos.getOrNull(isSelected)
        )
        Spacer(modifier = Modifier.height(37.dp))
        ListPhoto(
            photos = allPhotos,
            isSelected = isSelected,
            onPhotosClick = {
                isSelected = it
            }
        )
        Spacer(modifier = Modifier.height(33.dp))
        Description(
            description = description
        )
    }
}

@Composable
fun MainPhotoProduct(
    mainPhoto: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(241.dp, 170.dp)
                .padding(bottom = 42.dp)
                .align(Alignment.TopCenter)
        ) {
            AsyncImage(
                placeholder = painterResource(R.drawable.image_placeholder),
                model = mainPhoto ?: "",
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            Image(
                painterResource(R.drawable.slider),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ListPhoto(
    photos: List<String?>,
    isSelected: Int,
    onPhotosClick: (Int) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        itemsIndexed(photos){ index, photo ->
            val colorBorder by animateColorAsState(
                targetValue = if (index == isSelected) Colors.accent else Colors.block,
                animationSpec = tween(300)
            )

            Box(
                modifier = Modifier
                    .clickable(onClick = {
                        onPhotosClick(index)
                    })
                    .background(color = Colors.block, shape = RoundedCornerShape(16.dp))
                    .border(
                        width = 3.dp,
                        color = colorBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                AsyncImage(
                    model = photo,
                    placeholder = painterResource(R.drawable.image_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(2.dp, 14.5.dp)
                        .size(52.dp, 27.dp)
                )
            }
        }
    }
}

@Composable
fun Description(
    description: String
) {
    val maxLines by remember { mutableStateOf(3) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = description,
            color = Colors.hint,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
            fontWeight = FontWeight.Normal
        )
        if (maxLines > 3) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = stringResource(R.string.more_detailed),
                color = Colors.accent,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun Actions(
    liked: Boolean,
    product: Product,
    onLiked: (Long, Boolean) -> Unit,
    addedInCart: (Long) -> Unit,
    visibleInCart: Boolean
) {

    val icon = if (liked) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
    val tint = if (liked) Colors.red else Color.Unspecified
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        CustomIconButton(
            onClick = { onLiked(product.id, product.isFavorite) },
            icon = icon,
            tint = tint,
            padding = 14.dp,
            size = 24.dp
        )
        CustomButtonForBuy(
            onClick = { addedInCart(product.id) },
            modifier = Modifier
                .fillMaxWidth(),
            visibleInCart = visibleInCart
        )
    }
}