package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventory.ui.item.Converters

@Database(entities = [Note::class], version = 3, exportSchema = false)  // Aumenta la versión a 3
@TypeConverters(Converters::class) // Registrar los TypeConverters
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()  // Usa migración destructiva para evitar problemas de migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
