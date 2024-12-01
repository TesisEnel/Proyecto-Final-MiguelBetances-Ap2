package edu.ucne.taskmaster.data.di

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateAdater {
    private val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    @ToJson
    fun dateToJson(value: Date?): String? {
        return value?.let { dateformat.format(it) }
    }

    @FromJson
    fun fromJson(value: String?): Date? {
        return value?.let { dateformat.parse(it) }
    }
}

