package com.adharul.i.githubusers.view.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.shared_preference.UserPreference
import com.adharul.i.githubusers.view.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(main_toolbar)

        //Notification Alarm
        val notifSettingActivity = NotifSettingActivity()
        val mUserPreference = UserPreference(this)
        val userPreference = mUserPreference.getUser()
        if (userPreference.isFirstTime) {
            notifSettingActivity.getAlarmReceiverInstance().setRepeatingAlarm(this)
            userPreference.isFirstTime = false
            mUserPreference.setUser(userPreference)
        }

        //Fragment Manager
        val mFragmentManager = supportFragmentManager
        val mMainFragment = MainFragment()
        val fragment = mFragmentManager.findFragmentByTag(MainFragment::class.java.simpleName)
        if (fragment !is MainFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_main, mMainFragment, MainFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.localization_setting -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }
            R.id.notification_setting -> {
                val mIntent = Intent(this, NotifSettingActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.favorite_menu -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
