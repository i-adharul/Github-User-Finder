package com.adharul.i.githubusers.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserPreferenceItems (
    var isFirstTime: Boolean = true,
    var isAlarmNotifAllowed: Boolean = true
) : Parcelable