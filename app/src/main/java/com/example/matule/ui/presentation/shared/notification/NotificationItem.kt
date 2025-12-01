package com.example.matule.ui.presentation.shared.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matule.R
import com.example.matule.ui.presentation.theme.Colors
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun NotificationItem(
    heading: String,
    description: String,
    date: String
) {
    Box(
        modifier = Modifier
            .background(
                color = Colors.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = heading,
                color = Colors.text,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = Colors.text,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                fontWeight = FontWeight.Normal,
                lineHeight = 14.4.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = formatIsoToDisplayUTC(date),
                color = Colors.subTextDark,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.new_peninim_mt_inclined_2)),
                fontWeight = FontWeight.Normal,
                lineHeight = 14.4.sp
            )
        }
    }
}

fun formatIsoToDisplayUTC(isoDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(isoDate)

        val outputFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())

        outputFormat.format(date!!)
    } catch (e: Exception) {
        isoDate
    }
}