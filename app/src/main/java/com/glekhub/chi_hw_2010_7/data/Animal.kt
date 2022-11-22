package com.glekhub.chi_hw_2010_7.data

data class Animal(
    val id: Int,
    val name: String,
    val animal_type: String,
    val weight_max: String,
    val image_link: String,
) {
    companion object {
        const val TABLE = "Animal"
        const val ANIMAL_ID = "_id"
        const val NAME = "name"
        const val ANIMAL_TYPE = "animal_type"
        const val WEIGHT_MAX = "weight_max"
        const val IMAGE_LINK = "image_link"

        const val SQL_CREATE_ANIMAL =
            "CREATE TABLE $TABLE (" +
                    "$ANIMAL_ID INTEGER PRIMARY KEY," +
                    "$NAME TEXT NOT NULL," +
                    "$ANIMAL_TYPE TEXT NOT NULL," +
                    "$WEIGHT_MAX TEXT NOT NULL," +
                    "$IMAGE_LINK TEXT NOT NULL);"

        const val SQL_DELETE_ANIMAL = "DROP TABLE IF EXISTS $TABLE"
    }
}