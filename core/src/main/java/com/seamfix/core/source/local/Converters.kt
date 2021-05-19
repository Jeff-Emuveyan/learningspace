package com.seamfix.core.source.local

import androidx.annotation.Keep
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.seamfix.core.model.table.Location

const val EMPTY = ""
@Keep
class Converters {

    @TypeConverter
    fun locationToString(location: Location?): String {
        if(location == null) return EMPTY
        val gSon = Gson()
        return gSon.toJson(location)
    }

    @TypeConverter
    fun stringToLocation(string: String): Location?{
        if(string == EMPTY) return null
        val gSon = Gson()
        return gSon.fromJson(string, Location::class.java)
    }
}