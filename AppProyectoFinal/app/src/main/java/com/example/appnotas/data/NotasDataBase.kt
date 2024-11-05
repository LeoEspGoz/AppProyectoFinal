package com.example.appnotas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// En versión la cambiamos cada que se haga un cambio a la base de datos
@Database(entities = [Nota::class],version = 1, exportSchema = false)
abstract class NotasDataBase : RoomDatabase() {
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var Instance : NotasDataBase?= null

        fun getDatabase(context: Context): NotasDataBase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context,NotasDataBase::class.java, "nota_database")
                    .build().also{ Instance = it }
            }
        }
    }

}