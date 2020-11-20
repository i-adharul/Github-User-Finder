package com.adharul.i.githubusers.model.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_AVATAR_URL
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_ID
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_USERNAME
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)  {
    companion object {
        private const val DATABASE_NAME = "dbgithubusers"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_USERNAME TEXT NOT NULL," +
                " $COLUMN_AVATAR_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}