package com.example.todoss5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoss5.database.dao.CategoryEntityDao
import com.example.todoss5.database.dao.ItemEntityDao
import com.example.todoss5.database.entity.CategoryEntity
import com.example.todoss5.database.entity.ItemEntity

@Database(
    entities = [ItemEntity::class,CategoryEntity::class],
    version = 3 //update anytime change to table, DB
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var appDatabase: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if(appDatabase != null) {
                return appDatabase!!
            }

            appDatabase = Room
                .databaseBuilder(context.applicationContext, AppDatabase::class.java, "to-buy-database")
                .addMigrations(MIGRATION_1_2())
                .addMigrations(MIGRATION_2_3())//prevent remove of all data after update version of db
                .build()
            return appDatabase!!
        }
    }

    class MIGRATION_1_2 : Migration (1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            //list what change by newer version
            database.execSQL("CREATE TABLE IF NOT EXISTS `category_entity` (`id` TEXT NOT NULL,`name` TEXT NOT NULL,PRIMARY KEY(`id`)) ")
        }
    }

    class MIGRATION_2_3 : Migration (2,3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            //list what change by newer version
            database.execSQL("ALTER TABLE `item_entity` ADD COLUMN `quantity` INTEGER DEFAULT 0 NOT NULL")
        }
    }

    abstract fun itemEntityDao(): ItemEntityDao
    abstract fun categoryEntityDao() : CategoryEntityDao
}
