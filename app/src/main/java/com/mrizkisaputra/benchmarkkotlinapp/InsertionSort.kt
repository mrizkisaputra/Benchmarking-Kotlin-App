package com.mrizkisaputra.benchmarkkotlinapp

import android.util.Log
import org.json.JSONArray

class InsertionSort {
    companion object {
        fun sort(data: JSONArray): JSONArray {
            Log.i("SEBELUM DI SORTING", "$data")
            val n = data.length()
            for (i in 1 until n) {
                val key = data.getJSONObject(i)
                var j = i - 1

                while (j >= 0 && key.getInt("nomorAntrian") < data.getJSONObject(j).getInt("nomorAntrian")) {
                    data.put(j+1, data.getJSONObject(j))
                    --j
                }
                data.put(j+1, key)
            }

            return data;
        }
    }
}