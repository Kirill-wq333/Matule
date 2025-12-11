package com.example.matule.ui.presentation.shared.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matule.R
import com.example.matule.ui.presentation.shared.buttons.CustomIconButton
import com.example.matule.ui.presentation.theme.Colors
import com.example.matule.ui.presentation.theme.MatuleTypography

@Preview
@Composable
private fun PrevInformationSection() {
    Box(
        modifier = Modifier.fillMaxSize().background(Colors.background),
        contentAlignment = Alignment.Center
    ) {
        InformationSection(
            address = "dfshsdhsd",
            phone = "+7(912) 325-34-43",
            city = "Tolliatti",
            country = "",
            postalCode = "214",
            email = "admin@example.com",
            visibleEndIcon = true
        )
    }
}

@Composable
fun InformationSection(
    address: String?,
    city: String?,
    country: String?,
    postalCode: String?,
    phone: String?,
    visibleEndIcon: Boolean,
    email: String?
) {

    Content(
        phone = phone,
        email = email,
        address = address,
        city = city,
        country = country,
        postalCode = postalCode,
        visibleEndIcon = visibleEndIcon
    )
}

@Composable
private fun Content(
    address: String?,
    city: String?,
    country: String?,
    postalCode: String?,
    phone: String?,
    visibleEndIcon: Boolean,
    email: String?
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = Colors.block, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ContactNumberBlock(
            phone = phone,
            email = email,
            visibleEndIcon = visibleEndIcon
        )
        AddressBlock(
            address = address,
            city = city,
            country = country,
            postalCode = postalCode,
            onClick = {},
            visibleEndIcon = visibleEndIcon
        )
        PaymentMethodBlock(
            visibleEndIcon = visibleEndIcon
        )
    }
}

@Composable
fun ContactNumberBlock(
    visibleEndIcon: Boolean,
    phone: String?,
    email: String?
) {
    Block(
        label = R.string.contact_info,
        spacer = 16.dp,
        content = {
            Card(
                name = email ?: "*******@****.***",
                nameCard = stringResource(R.string.email),
                endIcon = R.drawable.ic_edit,
                startIcon = R.drawable.ic_email,
                onClick = {},
                visibleEndIcon = visibleEndIcon
            )
            Card(
                name = phone ?: "+*(***) ***-**-**",
                nameCard = stringResource(R.string.phone),
                endIcon = R.drawable.ic_edit,
                startIcon = R.drawable.ic_call,
                onClick = {},
                visibleEndIcon = visibleEndIcon
            )
        }
    )
}

@Composable
fun AddressBlock(
    address: String?,
    visibleEndIcon: Boolean,
    country: String?,
    city: String?,
    postalCode: String?,
    onClick: () -> Unit
) {
    Block(
        label = R.string.address,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$country, $city, $address, $postalCode",
                    color = Colors.hint,
                    style = MatuleTypography.bodySmall
                )
                if (visibleEndIcon) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(onClick = onClick)
                            .size(20.dp)
                    )
                }
            }
            Map()
        }
    )
}

@Composable
fun Map() {
    Box(
        modifier = Modifier
            .clickable(onClick = {})
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(101.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.map),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 19.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(R.string.view_map),
                color = Colors.block,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
            )
            CustomIconButton(
                backColor = Colors.accent,
                tint = Colors.block,
                icon = R.drawable.ic_marker,
                padding = 8.dp,
                size = 20.dp
            )
        }
    }
}

@Composable
fun PaymentMethodBlock(
    visibleEndIcon: Boolean,
) {
    Block(
        modifier = Modifier.padding(end = 20.5.dp),
        label = R.string.payment_method,
        verticalSpacer = 0.dp,
        content = {
            Card(
                name = "Dbl Card",
                nameCard = "**** **** 0235 3217",
                endIcon = R.drawable.ic_arrow_down,
                startIcon = R.drawable.ic_email,
                onClick = {},
                visibleEndIcon = visibleEndIcon
            )
        }
    )
}

@Composable
fun Block(
    modifier: Modifier = Modifier,
    label: Int,
    spacer: Dp = 12.dp,
    content: @Composable () -> Unit,
    verticalSpacer: Dp = 16.dp
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(label),
            color = Colors.text,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
        )
        Spacer(modifier = Modifier.height(spacer))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(verticalSpacer)
        ) {
            content()
        }
    }
}

@Composable
fun Card(
    nameCard: String,
    name: String,
    visibleEndIcon: Boolean,
    endIcon: Int,
    startIcon: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomIconButton(
                backColor = Colors.background,
                icon = startIcon,
                shape = RoundedCornerShape(12.dp),
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    color = Colors.text,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                )
                Text(
                    text = nameCard,
                    color = Colors.hint,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
                )
            }
        }
        if (visibleEndIcon) {
            Icon(
                imageVector = ImageVector.vectorResource(endIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .size(20.dp)
            )
        }
    }
}