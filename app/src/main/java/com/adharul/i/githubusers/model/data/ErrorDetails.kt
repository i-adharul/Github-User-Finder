package com.adharul.i.githubusers.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorDetails (
    var errCode: Int? = null,
    var isError: Boolean? = null
) : Parcelable