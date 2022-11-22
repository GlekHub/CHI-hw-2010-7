package com.glekhub.chi_hw_2010_7.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RetroDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Animal.SQL_CREATE_ANIMAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(Animal.SQL_DELETE_ANIMAL)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "Retro.db"
        const val DATABASE_VERSION = 1
    }
}