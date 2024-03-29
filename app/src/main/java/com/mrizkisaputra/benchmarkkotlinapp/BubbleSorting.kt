package com.mrizkisaputra.benchmarkkotlinapp

import android.util.Log
import org.json.JSONArray

class BubbleSorting {

    fun sort(data: JSONArray): JSONArray {
        Log.i("SEBELUM DI SORTING", "$data")
        val n = data.length()
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                val jsonObject1 = data.getJSONObject(j)
                val jsonObject2 = data.getJSONObject(j + 1)
                if (jsonObject1.getInt("nomorAntrian") > jsonObject2.getInt("nomorAntrian")) {
                    // Menukar elemen secara asc
                    data.put(j, jsonObject2)
                    data.put(j + 1, jsonObject1)
                }
            }
        }
        return data
    }
}