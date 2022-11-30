package com.example.ourstory.utils

import com.google.gson.Gson
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class Testing {

    private lateinit var objToJson: List<TestModel>
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        objToJson = listData()
        gson = Gson()
    }

    private fun listData(): List<TestModel> {
        val addList = arrayListOf<TestModel>()

        for (i in 0 until 5) {
            val data = TestModel(
                1, "Test"
            )
            addList.add(data)
        }

        return addList
    }

    @Test
    fun `gson obj to json`() {

        val jsonString = gson.toJson(objToJson)
        println(jsonString)
        Assert.assertEquals(
            jsonString,
            """[{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"}]"""
        )
    }

    @Test
    fun `gson json to object`() {
        val jsonString =
            """[{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"},{"id":1,"description":"Test"}]"""
        val jsonObj = gson.fromJson(jsonString, Array<TestModel>::class.java).asList()
        println(jsonObj)
        Assert.assertEquals(listData(), jsonObj)
    }

}

data class TestModel(
    val id: Int,
    val description: String
)