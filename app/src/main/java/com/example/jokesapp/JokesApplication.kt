package com.example.jokesapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JokesApplication : Application() {

    companion object {
        lateinit var currentInstance: JokesApplication

        fun getCachedCategoryIdFromPreferences(): Int {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE
            )

            return sharedPref?.getInt(APP_PREFERENCES_CACHED_CATEGORY_ID, 0) ?: 0
        }

        fun setCachedCategoryIdInPreferences(id: Int) {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putInt(APP_PREFERENCES_CACHED_CATEGORY_ID, id)
                apply()
            }
        }

        fun getCategoryNameFromPreferences(): String {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE
            )

            return sharedPref?.getString(APP_PREFERENCES_CATEGORY_NAME, "") ?: ""
        }

        fun setCategoryNameInPreferences(name: String) {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putString(APP_PREFERENCES_CATEGORY_NAME, name)
                apply()
            }
        }

        fun getLastCategoryIdFromPreferences(): Int {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES, Context.MODE_PRIVATE
            )

            return sharedPref?.getInt(APP_PREFERENCES_LAST_CATEGORY_LAST_ID, 0) ?: 0
        }

        fun setLastCategoryIdInPreferences(id: Int) {
            val sharedPref = currentInstance.getSharedPreferences(
                APP_PREFERENCES,
                Context.MODE_PRIVATE
            )

            with(sharedPref.edit()) {
                putInt(APP_PREFERENCES_LAST_CATEGORY_LAST_ID, id)
                apply()
            }
        }

        private const val APP_PREFERENCES = "app_preferences"
        private const val APP_PREFERENCES_CACHED_CATEGORY_ID = "preferences_cached_id"
        private const val APP_PREFERENCES_LAST_CATEGORY_LAST_ID = "preferences_last_id"
        private const val APP_PREFERENCES_CATEGORY_NAME = "preferences_name"
    }

    override fun onCreate() {
        super.onCreate()

        currentInstance = this
    }
}