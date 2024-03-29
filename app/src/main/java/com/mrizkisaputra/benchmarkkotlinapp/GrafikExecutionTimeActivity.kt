package com.mrizkisaputra.benchmarkkotlinapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mrizkisaputra.benchmarkkotlinapp.databinding.ActivityGrafikExecutionTimeBinding

class GrafikExecutionTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGrafikExecutionTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGrafikExecutionTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            // Atur properti grafik
            lineChart.setTouchEnabled(true)
            lineChart.setPinchZoom(true)
            lineChart.isDragEnabled = true
            lineChart.setScaleEnabled(true)
            lineChart.description = Description().apply {
                text = "Millisecond"
                setPosition(150f, 15f)
            }
            lineChart.axisRight.setDrawLabels(false)
            lineChart.axisLeft.apply {
                axisMinimum = 0f
                axisLineWidth = 3f
                axisLineColor = Color.BLACK
                labelCount = 12
            }
        }

        val executionTimes: LongArray? = intent.getLongArrayExtra("EXTRA_EXECUTION_TIMES")
        val iteration = intent.getStringExtra("EXTRA_ITERATION")
        val algorithm = intent.getStringExtra("EXTRA_ALGORITHM")
        val size = intent.getIntExtra("EXTRA_TOTAL_DATA", 0)
        if (executionTimes != null) {
            val total = executionTimes.sum().toDouble()
            val averagesMillis: Double = total / executionTimes.size
            val averagesSecond: Double = averagesMillis / executionTimes.size / 1000
            binding.averageMillisecond.text = "Rata-rata : $averagesMillis ms"
            binding.averageSecond.text = "Rata-rata : $averagesSecond second"
            setDataLineChart(executionTimes)
        }
        binding.algorithm.text = "Algoritma : $algorithm"
        binding.totalInputData.text = "Jumlah data : $size"

    }

    private fun setDataLineChart(executionTimes: LongArray) {
        val entries = ArrayList<Entry>()
        val valueBottom = ArrayList<String>()
        for ((index, value) in executionTimes.withIndex()) {
            // Tambahkan setiap nilai ke dalam Entry
            entries.add(Entry(index.toFloat(), value.toFloat()))
            valueBottom.add("${index + 1}")
        }

        binding.lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(valueBottom)
            labelCount = entries.size
            granularity = 1f
        }

        val executionTime = LineDataSet(entries, "Waktu Eksekusi").apply {
            color = Color.BLUE
            valueTextColor = Color.RED
        }

        val lineData = LineData(executionTime)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
    }

}