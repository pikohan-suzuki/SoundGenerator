package com.amebaownd.pikohan_nwiatori.soundgenerator.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import java.lang.RuntimeException

class MyContext(application : Context){
    var applicationContext = application
    companion object{
        private lateinit var instance:MyContext
        fun onCreateAppcalition(applicationContext: Context){
            instance = MyContext(applicationContext)
        }
        fun getContext(): Context {
            if(instance.applicationContext==null)
                throw RuntimeException()
            return instance.applicationContext
        }
        fun getString(resId:Int):String{
            return instance.applicationContext.getString(resId)
        }

        fun getColor(resId:Int):Int{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                instance.applicationContext.getColor(resId)
            } else {
                instance.applicationContext.resources.getColor(resId)
            }
        }

        fun getDrawable(resId:Int): Drawable?{
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                instance.applicationContext.getDrawable(resId)
            } else {
                instance.applicationContext.resources.getDrawable(resId)
            }
        }

    }
}