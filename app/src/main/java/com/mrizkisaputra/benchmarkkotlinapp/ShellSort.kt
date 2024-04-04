package com.mrizkisaputra.benchmarkkotlinapp

import android.util.Log
import org.json.JSONArray
import org.json.JSONException

class ShellSort {
    companion object {
        fun sort(data: JSONArray): JSONArray {
            Log.i("SEBELUM DI SORTING", "$data")
            val n = data.length()
            for (interval in n / 2 downTo 1) {
                for (i in interval until n) {
                    try {
                        val temp = data.getJSONObject(i)
                        var j = i
                        while (j >= interval && data.getJSONObject(j - interval).getInt("nomorAntrian") > temp.getInt("nomorAntrian")) {
                            data.put(j, data.getJSONObject(j - interval))
                            j -= interval
                        }
                        data.put(j, temp)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            return data;
        }
    }
}