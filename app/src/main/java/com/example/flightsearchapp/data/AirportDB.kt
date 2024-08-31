package com.example.flightsearchapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(airport::class,favorite::class), version = 2)
abstract class AirportDB:RoomDatabase() {
    abstract fun airportDao():AirportDao
    abstract fun favorite():FavoriteDao
    companion object{
        @Volatile
        private var INSTANCE:AirportDB? = null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS `airport_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                `name` TEXT NOT NULL, 
                `iata_code` TEXT NOT NULL, 
                `passengers` INTEGER NOT NULL
            )
        """)

                database.execSQL("""
            INSERT INTO `airport_new` (`id`, `name`, `iata_code`, `passengers`)
            SELECT `id`, `name`, `iata_code`, `passengers` FROM `airport`
        """)

                // Drop the old table
                database.execSQL("DROP TABLE `airport`")

                // Rename the new table to the original name
                database.execSQL("ALTER TABLE `airport_new` RENAME TO `airport`")
            }
        }

        fun getDatabase(context: Context):AirportDB{
            Log.d("DatabaseCreator","Database ka abba")
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    AirportDB::class.java,
                    "flight_search.db"
                )
                    .addMigrations(MIGRATION_1_2)
                   .createFromAsset("flight_search.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it }

            }
        }
    }
}