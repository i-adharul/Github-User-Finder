package com.adharul.i.githubfavoriteusers.model.helper

import android.database.Cursor
import com.adharul.i.githubfavoriteusers.model.data.FavoriteUserDetails
import com.adharul.i.githubfavoriteusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_AVATAR_URL
import com.adharul.i.githubfavoriteusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_ID
import com.adharul.i.githubfavoriteusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_USERNAME

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteUserDetails> {
        val favoriteList = ArrayList<FavoriteUserDetails>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val username = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
                favoriteList.add(FavoriteUserDetails(id, username, avatarUrl))
            }
        }
        return favoriteList
    }
}