package com.mrizkisaputra.benchmarkkotlinapp

import org.json.JSONArray

class QuickSort {
    companion object {
        private fun partition(data: JSONArray, low: Int, high: Int): Int {
            // Pilih elemen pivot
            val pivot = data.getJSONObject(high)
            val pivotValue = pivot.getInt("nomorAntrian")

            var i = low - 1 // Indeks dari elemen yang lebih kecil

            for (j in low until high) {
                // Jika elemen saat ini lebih kecil atau sama dengan pivot
                if (data.getJSONObject(j).getInt("nomorAntrian") <= pivotValue) {
                    i++

                    // Tukar arr[i] dan arr[j]
                    val temp = data.getJSONObject(i)
                    data.put(i, data.getJSONObject(j))
                    data.put(j, temp)
                }
            }

            // Tukar arr[i+1] dan arr[high] (atau pivot)
            val temp = data.getJSONObject(i + 1)
            data.put(i + 1, data.getJSONObject(high))
            data.put(high, temp)

            return i + 1
        }

        fun sort(data: JSONArray, low: Int, high: Int) {
            if (low < high) {
                // Membagi array dan mendapatkan indeks pivot
                val pi = partition(data, low, high)

                // Memanggil quickSort() rekursif untuk dua subarray sebelum dan setelah pivot
                sort(data, low, pi - 1)
                sort(data, pi + 1, high)
            }
        }
    }
}