package com.adharul.i.githubusers.model.shared_preference

import android.content.Context
import com.adharul.i.githubusers.model.data.UserPreferenceItems

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val IS_ALARM_NOTIF_ALLOWED = "isAlarmNotifAllowed"
        private const val IS_FIRST_TIME = "isFirstTime"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(value: UserPreferenceItems) {
        val editor = preferences.edit()
        editor.putBoolean(IS_FIRST_TIME, value.isFirstTime)
        editor.putBoolean(IS_ALARM_NOTIF_ALLOWED, value.isAlarmNotifAllowed)
        editor.apply()
    }

    fun getUser(): UserPreferenceItems {
        val model = UserPreferenceItems()
        model.isAlarmNotifAllowed = preferences.getBoolean(IS_ALARM_NOTIF_ALLOWED, true)
        model.isFirstTime = preferences.getBoolean(IS_FIRST_TIME, true)
        return model
    }
}