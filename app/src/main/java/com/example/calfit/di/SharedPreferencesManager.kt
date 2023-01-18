package com.example.calfit.di

import android.content.Context
import android.provider.SyncStateContract
import com.example.calfit.dataModel.UserDetails
import com.google.gson.Gson

object SharedPreferencesManager {

    val PreferenceName: String = SharedPreferencesManager.javaClass.name

    fun getUserObject(context: Context): UserDetails? {


        val sp = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE)


        val gson = Gson()


        val json = sp.getString("UserObject", null)


        return gson.fromJson(json, UserDetails::class.java)


    }

    fun putUserObject(context: Context, userObject: UserDetails) {

        val sp = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE)


        val gson = Gson()


        sp.edit().putString("UserObject", gson.toJson(userObject)).apply()

    }

    fun removeUserObject(context: Context) {
        val sp = context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE)
        sp.edit().remove("UserObject").apply()
    }
}