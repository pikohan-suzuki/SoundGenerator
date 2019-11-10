package com.amebaownd.pikohan_nwiatori.soundgenerator.util

import android.content.Context

//object ServiceLoader {
//    //private var database : AppDatabase? = null
//    //var ideaMemoRepository:IdeaMemoRepository? = null
//
//    fun provideRepository(context:Context):IdeaMemoRepository{
//        synchronized(this){
//            return ideaMemoRepository
//                ?: createIdeaMemoRepository(
//                    context
//                )
//        }
//    }
//
//    private fun createIdeaMemoRepository(context:Context):IdeaMemoRepository{
//        val database = database
//            ?: createDatabase(
//                context
//            )
//        val result = IdeaMemoRepository(database.memoDao())
//        ideaMemoRepository = result
//        return result
//    }
//
//    private fun createDatabase(context:Context):AppDatabase{
//        val result = AppDatabase.getDatabase(context)
//        database =result
//        return result
//    }
//}