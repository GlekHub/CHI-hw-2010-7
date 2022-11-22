package com.glekhub.chi_hw_2010_7.data

import android.content.ContentValues
import android.content.Context

class RetroDbManager(context: Context) {

    private val dbHelper = RetroDbHelper(context)
    private var values = ContentValues()

    fun insertAnimals(list: List<Animal>) {
        val db = dbHelper.writableDatabase

        //db.execSQL("DELETE FROM ${Animal.TABLE}")

        list.forEach {
            values.apply {
                put(Animal.ANIMAL_ID, it.id)
                put(Animal.NAME, it.name)
                put(Animal.ANIMAL_TYPE, it.animal_type)
                put(Animal.WEIGHT_MAX, it.weight_max)
                put(Animal.IMAGE_LINK, it.image_link)
            }
            db.insert(Animal.TABLE, null, values)
            values.clear()
        }
        db.close()
    }

    fun fetchAnimals(): List<Animal> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            Animal.TABLE, null, null, null, null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            val animals = ArrayList<Animal>(cursor.count)
            cursor.moveToFirst()

            do {
                var index = cursor.getColumnIndex(Animal.ANIMAL_ID)
                val id = cursor.getInt(index)

                index = cursor.getColumnIndex(Animal.NAME)
                val name = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.ANIMAL_TYPE)
                val type = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.WEIGHT_MAX)
                val weight = cursor.getString(index)

                index = cursor.getColumnIndex(Animal.IMAGE_LINK)
                val image = cursor.getString(index)

                animals.add(Animal(id, name, type, weight, image))
            } while (cursor.moveToNext())

            cursor.close()
            db.close()
            return animals
        }
        db.close()
        return emptyList()
    }
}