package com.tarlic.RPGDiceRoller

import android.os.Parcel
import android.os.Parcelable
import java.sql.Date
import java.util.*

class DateComparator() : Comparator<Date>, Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun compare(date1: Date, date2: Date): Int {
        //return date1.compareTo(date2);
        return date2.compareTo(date1)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateComparator> {
        override fun createFromParcel(parcel: Parcel): DateComparator {
            return DateComparator(parcel)
        }

        override fun newArray(size: Int): Array<DateComparator?> {
            return arrayOfNulls(size)
        }
    }
}