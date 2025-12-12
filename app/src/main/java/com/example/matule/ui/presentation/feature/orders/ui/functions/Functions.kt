package com.example.matule.ui.presentation.feature.orders.ui.functions

import com.example.domain.ui.presentation.feature.orders.model.Order
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.collections.forEach

enum class DayGroup {
    TODAY,
    YESTERDAY,
    RECENTLY
}

fun formatSmartTimeAgo(isoDate: String): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val now = Date()
        val diffMillis = now.time - date.time

        when {
            diffMillis < 60000 -> "только что" // меньше минуты
            diffMillis < 3600000 -> { // меньше часа
                val minutes = (diffMillis / 60000).toInt()
                when (minutes) {
                    1, 21, 31, 41, 51 -> "$minutes минуту назад"
                    2, 3, 4, 22, 23, 24, 32, 33, 34, 42, 43, 44, 52, 53, 54 -> "$minutes минуты назад"
                    else -> "$minutes минут назад"
                }
            }
            diffMillis < 86400000 -> { // меньше суток
                val hours = (diffMillis / 3600000).toInt()
                when (hours) {
                    1, 21 -> "$hours час назад"
                    2, 3, 4, 22, 23 -> "$hours часа назад"
                    else -> "$hours часов назад"
                }
            }
            else -> formatTimeOnly(isoDate) // больше суток, но сегодня
        }
    } catch (_: Exception) {
        ""
    }
}

// Умное форматирование для вчера
fun formatYesterdayTimeSmart(isoDate: String): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.format(date)
    } catch (_: Exception) {
        ""
    }
}

// Умное форматирование для старых заказов
fun formatDaysAgoSmart(isoDate: String): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
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

        when (daysDiff) {
            0 -> "сегодня"
            1 -> "вчера"
            2, 3, 4 -> "$daysDiff дня назад"
            in 5..30 -> "$daysDiff дней назад"
            else -> "более месяца назад"
        }
    } catch (_: Exception) {
        ""
    }
}
fun formatTimeOnly(isoDate: String): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(isoDate) ?: return ""

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.format(date)
    } catch (_: Exception) {
        ""
    }
}

fun groupOrdersByDay(orders: List<Order>): Map<DayGroup, List<Order>> {
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

fun getDayGroupTitle(dayGroup: DayGroup): String {
    return when (dayGroup) {
        DayGroup.TODAY -> "Сегодня"
        DayGroup.YESTERDAY -> "Вчера"
        DayGroup.RECENTLY -> "Недавно"
    }
}

fun parseDate(isoDate: String): Date? {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.parse(isoDate)
    } catch (_: Exception) {
        null
    }
}

fun getOrderTitle(order: Order): String {
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