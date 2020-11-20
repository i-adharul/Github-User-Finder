package com.adharul.i.githubusers.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_ID
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_USERNAME
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.TABLE_NAME
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: FavoriteUserHelper? = null
        fun getInstance(context: Context): FavoriteUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteUserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID ASC")
    }
    fun queryByUsername(username: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_USERNAME LIKE ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_ID = '$id'", null)
    }
}