package com.example.taskot.db

import androidx.room.TypeConverter
import java.util.Date

class Helpers {

    @TypeConverter
    fun fromDate(date : Date) : Long{
        return date.time
    }

    @TypeConverter
    fun toDate(time : Long) : Date {
        return Date(time)
    }

}