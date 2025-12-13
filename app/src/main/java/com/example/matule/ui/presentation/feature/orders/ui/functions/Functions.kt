package com.example.matule.ui.presentation.feature.orders.ui.functions

import com.example.domain.ui.presentation.feature.orders.model.Order
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.forEach

enum class DayGroup(val getLocalizedTitle: (Locale) -> String) {
    TODAY({ locale ->
        when (locale.language) {
            "ru" -> "Сегодня"
            "en" -> "Today"
            else -> "Today"
        }
    }),
    YESTERDAY({ locale ->
        when (locale.language) {
            "ru" -> "Вчера"
            "en" -> "Yesterday"
            else -> "Yesterday"
        }
    }),
    RECENTLY({ locale ->
        when (locale.language) {
            "ru" -> "Недавно"
            "en" -> "Recently"
            else -> "Recently"
        }
    })
}

fun formatSmartTimeAgo(
    isoDate: String,
    locale: Locale = Locale.getDefault()
): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val now = Date()
        val diffMillis = now.time - date.time
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        when (locale.language) {
            "ru" -> formatForRussian(diffMillis, minutes, hours, locale)
            "en" -> formatForEnglish(diffMillis, minutes, hours, locale)
            else -> formatForEnglish(diffMillis, minutes, hours, locale)
        }
    } catch (_: Exception) {
        ""
    }
}

private fun formatForRussian(
    diffMillis: Long,
    minutes: Long,
    hours: Long,
    locale: Locale
): String {
    return when {
        diffMillis < 60000 -> "только что"

        minutes < 60 -> {
            when (minutes.toInt()) {
                1, 21, 31, 41, 51 -> "$minutes минуту назад"
                2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54 -> "$minutes минуты назад"
                else -> "$minutes минут назад"
            }
        }

        hours < 24 -> {
            when (hours.toInt()) {
                1, 21 -> "$hours час назад"
                2, 3, 4, 22, 23 -> "$hours часа назад"
                else -> "$hours часов назад"
            }
        }

        else -> {
            val timeFormat = SimpleDateFormat("HH:mm", locale)
            timeFormat.format(timeFormat)
        }
    }
}

private fun formatForEnglish(
    diffMillis: Long,
    minutes: Long,
    hours: Long,
    locale: Locale
): String {
    return when {
        diffMillis < 60000 -> "just now"

        minutes < 60 -> {
            if (minutes == 1L) "$minutes minute ago" else "$minutes minutes ago"
        }

        hours < 24 -> {
            if (hours == 1L) "$hours hour ago" else "$hours hours ago"
        }

        else -> {
            // Форматируем только время для сегодняшних дат
            val timeFormat = SimpleDateFormat("HH:mm", locale)
            timeFormat.format(timeFormat)
        }
    }
}

// Умное форматирование для вчера
fun formatYesterdayTimeSmart(
    isoDate: String,
    locale: Locale = Locale.getDefault()
): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val timeFormat = SimpleDateFormat("HH:mm", locale)
        timeFormat.format(date)
    } catch (_: Exception) {
        ""
    }
}

// Умное форматирование для старых заказов с поддержкой локалей
fun formatDaysAgoSmart(
    isoDate: String,
    locale: Locale = Locale.getDefault()
): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val orderDate = dateFormat.parse(isoDate) ?: return ""

        val calendarNow = Calendar.getInstance()
        val calendarOrder = Calendar.getInstance().apply { time = orderDate }

        // Сбрасываем время для сравнения только дат
        calendarNow.set(Calendar.HOUR_OF_DAY, 0)
        calendarNow.set(Calendar.MINUTE, 0)
        calendarNow.set(Calendar.SECOND, 0)
        calendarNow.set(Calendar.MILLISECOND, 0)

        calendarOrder.set(Calendar.HOUR_OF_DAY, 0)
        calendarOrder.set(Calendar.MINUTE, 0)
        calendarOrder.set(Calendar.SECOND, 0)
        calendarOrder.set(Calendar.MILLISECOND, 0)

        val diffMillis = calendarNow.timeInMillis - calendarOrder.timeInMillis
        val daysDiff = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

        when (locale.language) {
            "ru" -> {
                when (daysDiff) {
                    0 -> "сегодня"
                    1 -> "вчера"
                    2, 3, 4 -> "$daysDiff дня назад"
                    in 5..30 -> "$daysDiff дней назад"
                    else -> "более месяца назад"
                }
            }
            "en" -> {
                when (daysDiff) {
                    0 -> "today"
                    1 -> "yesterday"
                    in 2..30 -> "$daysDiff days ago"
                    else -> "more than a month ago"
                }
            }
            else -> {
                when (daysDiff) {
                    0 -> "today"
                    1 -> "yesterday"
                    else -> "$daysDiff days ago"
                }
            }
        }
    } catch (_: Exception) {
        ""
    }
}

fun formatTimeOnly(
    isoDate: String,
    locale: Locale = Locale.getDefault()
): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val timeFormat = SimpleDateFormat("HH:mm", locale)
        timeFormat.format(date)
    } catch (_: Exception) {
        ""
    }
}

fun getDayGroupTitle(
    dayGroup: DayGroup,
    locale: Locale = Locale.getDefault()
): String {
    return dayGroup.getLocalizedTitle(locale)
}

// Группировка заказов с поддержкой локалей
fun groupOrdersByDay(
    orders: List<Order>,
    locale: Locale = Locale.getDefault()
): Map<DayGroup, List<Order>> {
    val result = mutableMapOf<DayGroup, MutableList<Order>>()

    orders.forEach { order ->
        val orderDate = parseDate(order.createdAt)
        val dayGroup = getDayGroup(orderDate)

        result.getOrPut(dayGroup) { mutableListOf() }.add(order)
    }

    result.forEach { (_, ordersList) ->
        ordersList.sortByDescending { parseDate(it.createdAt) }
    }

    return result.toSortedMap(compareBy {
        when (it) {
            DayGroup.TODAY -> 0
            DayGroup.YESTERDAY -> 1
            DayGroup.RECENTLY -> 2
        }
    })
}

fun getDayGroupForOrder(order: Order): DayGroup {
    val orderDate = parseDate(order.createdAt)
    return getDayGroup(orderDate)
}

fun getDayGroup(date: Date?): DayGroup {
    if (date == null) return DayGroup.RECENTLY

    val calendarNow = Calendar.getInstance()
    val calendarOrder = Calendar.getInstance().apply { time = date }

    if (calendarNow.get(Calendar.YEAR) == calendarOrder.get(Calendar.YEAR) &&
        calendarNow.get(Calendar.MONTH) == calendarOrder.get(Calendar.MONTH) &&
        calendarNow.get(Calendar.DAY_OF_MONTH) == calendarOrder.get(Calendar.DAY_OF_MONTH)) {
        return DayGroup.TODAY
    }

    calendarNow.add(Calendar.DAY_OF_MONTH, -1)
    if (calendarNow.get(Calendar.YEAR) == calendarOrder.get(Calendar.YEAR) &&
        calendarNow.get(Calendar.MONTH) == calendarOrder.get(Calendar.MONTH) &&
        calendarNow.get(Calendar.DAY_OF_MONTH) == calendarOrder.get(Calendar.DAY_OF_MONTH)) {
        return DayGroup.YESTERDAY
    }

    return DayGroup.RECENTLY
}

fun parseDate(isoDate: String): Date? {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.parse(isoDate)
    } catch (_: Exception) {
        null
    }
}

// Форматирование заголовка заказа с поддержкой локалей
fun getOrderTitle(
    order: Order,
    locale: Locale = Locale.getDefault()
): String {
    return when (locale.language) {
        "ru" -> formatOrderTitleRu(order)
        "en" -> formatOrderTitleEn(order)
        else -> formatOrderTitleEn(order) // fallback to English
    }
}

private fun formatOrderTitleRu(order: Order): String {
    return when {
        order.items.isEmpty() -> "Заказ"
        order.items.size == 1 -> order.items.first().name
        else -> {
            val firstItem = order.items.first().name
            when (val otherCount = order.items.size - 1) {
                1 -> "$firstItem и еще 1 товар"
                in 2..4 -> "$firstItem и еще $otherCount товара"
                else -> "$firstItem и еще $otherCount товаров"
            }
        }
    }
}

private fun formatOrderTitleEn(order: Order): String {
    return when {
        order.items.isEmpty() -> "Order"
        order.items.size == 1 -> order.items.first().name
        else -> {
            val firstItem = order.items.first().name
            when (val otherCount = order.items.size - 1) {
                1 -> "$firstItem and 1 more item"
                else -> "$firstItem and $otherCount more items"
            }
        }
    }
}