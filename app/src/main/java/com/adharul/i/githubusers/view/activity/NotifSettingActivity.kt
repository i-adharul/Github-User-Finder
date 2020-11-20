package com.adharul.i.githubusers.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.UserPreferenceItems
import com.adharul.i.githubusers.model.receiver.AlarmReceiver
import com.adharul.i.githubusers.model.shared_preference.UserPreference
import kotlinx.android.synthetic.main.activity_notif_setting.*


class NotifSettingActivity : AppCompatActivity() {

    companion object {
        private val alarmReceiver = AlarmReceiver()
    }

    private lateinit var mUserPreference: UserPreference
    private lateinit var userModel: UserPreferenceItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif_setting)

        setSupportActionBar(notif_setting_toolbar)
        supportActionBar?.title = "Notification Setting"
        supportActionBar?.elevation = 0f

        mUserPreference = UserPreference(this)
        userModel = mUserPreference.getUser()

        val simpleToggleButton = toggleAlarmNotif
        simpleToggleButton.isChecked = userModel.isAlarmNotifAllowed

        simpleToggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                userModel.isAlarmNotifAllowed = isChecked
                mUserPreference.setUser(userModel)
                alarmReceiver.setRepeatingAlarm(this)
            } else {
                userModel.isAlarmNotifAllowed = isChecked
                mUserPreference.setUser(userModel)
                alarmReceiver.cancelAlarm(this)
            }
        }
    }

    fun getAlarmReceiverInstance(): AlarmReceiver {
        return alarmReceiver
    }
}
