package com.ferhatozcelik.costcalculator.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ferhatozcelik.costcalculator.data.local.Converters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var serialNumber: String,
    var units: Int,
    var cost: Int,
    @TypeConverters(Converters::class)
    val createAt: Long
) : Parcelable


