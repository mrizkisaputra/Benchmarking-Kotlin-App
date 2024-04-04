package com.mrizkisaputra.benchmarkkotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mrizkisaputra.benchmarkkotlinapp.databinding.ActivityMainBinding
import org.json.JSONArray
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var jsonData: JSONArray
    // membuat background thread untuk memproses sorting, supaya tidak terjadi application not responding
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var algorithm: String
    private lateinit var iteration: String
    private lateinit var executionTimes: MutableList<Long>
    private lateinit var executionTimesAdapter: ArrayAdapter<String>

    private fun readJsonFromAsset(algorithm: String): JSONArray {
        val inputStream: InputStream
        if (algorithm.equals("Bubble Sorting") || algorithm.equals("Insertion Sorting")) {
            inputStream = assets.open("bubble_insertion.json") // membaca file json
        } else {
            inputStream = assets.open("quick_shell.json") // membaca file json
        }
        val json: String = inputStream.bufferedReader().use { it.readText() }
        return JSONArray(json)
    }

    private fun clearBenchmarkResults() {
        executionTimes.clear()
        executionTimesAdapter.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        executionTimes = ArrayList()
        executionTimesAdapter = ArrayAdapter(this, R.layout.item_list, R.id.text)
        binding.listview.adapter = executionTimesAdapter

        binding.startBenchmark.setOnClickListener {
            clearBenchmarkResults()
            algorithm = binding.algorithmToBenchmark.text.toString()
            iteration = binding.iterations.text.toString()

            if (algorithm.isEmpty()) {
                binding.inputLayoutAlgorithmToBenchmark.error = "please select one algorithm"
                return@setOnClickListener
            } else {
                binding.inputLayoutAlgorithmToBenchmark.error = null
            }

            if (iteration.isEmpty()) {
                binding.inputLayoutIteration.error = "iteration is not be empty"
                return@setOnClickListener
            } else {
                binding.inputLayoutIteration.error = null
            }

            executor.execute {
                handler.post {
                    binding.benchmarkProgress.visibility = View.VISIBLE
                    binding.startBenchmark.text = "Benchmark in progress..."
                    binding.startBenchmark.isEnabled = false
                    binding.lineChartExecutionTime.visibility = View.GONE
                }
                when (algorithm) {
                    "Bubble Sorting" -> startBenchmarkBubbleSort(iteration.toInt())
                    "Insertion Sorting" -> startBenchmarkInsertionSort(iteration.toInt())
                    "Quick Sorting" -> startBenchmarkQuickSort(iteration.toInt())
                    "Shell Sorting" -> startBenchmarkShellSort(iteration.toInt())
                }
                handler.post {
                    binding.benchmarkProgress.visibility = View.GONE
                    binding.startBenchmark.text = "Start Benchmark"
                    binding.startBenchmark.isEnabled = true
                    binding.lineChartExecutionTime.visibility = View.VISIBLE
                }
            }
        }

        binding.lineChartExecutionTime.setOnClickListener {
            Intent(this, GrafikExecutionTimeActivity::class.java).apply {
                putExtra("EXTRA_EXECUTION_TIMES", executionTimes.toLongArray())
                putExtra("EXTRA_ITERATION", iteration)
                putExtra("EXTRA_ALGORITHM", algorithm)
                putExtra("EXTRA_TOTAL_DATA", jsonData.length())
                startActivity(this)
            }
        }
    }

    private fun startBenchmarkBubbleSort(iteration: Int) {
        for (i in 1..iteration) {
            jsonData = readJsonFromAsset(algorithm)
            val measureTimeMillis = measureTimeMillis { // mendapatkan data execution time
                val sorted = BubbleSorting.sort(jsonData)
                Log.i("SETELAH DI SORTING", "$sorted")
            }
            executionTimes.add(measureTimeMillis) // menyimpan data execution time
            handler.post {
                executionTimesAdapter.add("Pengujian iterasi $i data berhasil diurutkan")
            }
        }
    }

    private fun startBenchmarkInsertionSort(iteration: Int) {
        for (i in 1..iteration) {
            jsonData = readJsonFromAsset(algorithm);
            val measureTimeMillis = measureTimeMillis { // mendapatkan data execution time
                val sorted = InsertionSort.sort(jsonData)
                Log.i("SETELAH DI SORTING", "$sorted")
            }
            executionTimes.add(measureTimeMillis) // menyimpan data execution time
            handler.post {
                executionTimesAdapter.add("Pengujian iterasi $i data berhasil diurutkan")
            }
        }
    }

    private fun startBenchmarkQuickSort(teration: Int) {

    }

    private fun startBenchmarkShellSort(iteration: Int) {
        for (i in 1..iteration) {
            jsonData = readJsonFromAsset(algorithm);
            val measureTimeMillis = measureTimeMillis { // mendapatkan data execution time
                val sorted = ShellSort.sort(jsonData)
                Log.i("SETELAH DI SORTING", "$sorted")
            }
            executionTimes.add(measureTimeMillis) // menyimpan data execution time
            handler.post {
                executionTimesAdapter.add("Pengujian iterasi $i data berhasil diurutkan")
            }
        }
    }


}