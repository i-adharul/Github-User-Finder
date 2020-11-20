package com.adharul.i.githubusers.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteUserDetails (
    var id: Int? = null,
    var username: String? = null,
    var avatarUrl: String? = null
) : Parcelable