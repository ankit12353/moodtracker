package com.android.mood.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.android.mood.R
import com.android.mood.helper.Constant.LANGUAGE
import com.android.mood.helper.Constant.PREFERENCE
import com.android.mood.helper.LocaleHelper
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sp = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val language = sp.getString(LANGUAGE,Locale.getDefault().language)
        LocaleHelper.setLocale(this,language!!)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1000)
    }
}