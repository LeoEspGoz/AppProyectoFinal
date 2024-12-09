package com.example.inventory.ui.item

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromJsonToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}