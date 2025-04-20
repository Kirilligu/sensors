package com.example.sensors
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sensorManager = getSystemService(SensorManager::class.java)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val listView = findViewById<ListView>(R.id.list_sensor)
        spinner.adapter = ArrayAdapter.createFromResource(
            this, R.array.type_sensors, android.R.layout.simple_spinner_item
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: android.widget.AdapterView<*>?, v: android.view.View?, pos: Int, id: Long) {
                val types = when (pos) {
                    0 -> listOf(
                        Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT,
                        Sensor.TYPE_PRESSURE, Sensor.TYPE_RELATIVE_HUMIDITY,
                        Sensor.TYPE_AMBIENT_TEMPERATURE)
                    1 -> listOf(
                        Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE,
                        Sensor.TYPE_PROXIMITY, Sensor.TYPE_GRAVITY)
                    else -> listOf(
                        Sensor.TYPE_HEART_RATE, Sensor.TYPE_HEART_BEAT)
                }
                listView.adapter = ArrayAdapter(this@MainActivity,
                    android.R.layout.simple_list_item_1,
                    types.flatMap { sensorManager.getSensorList(it) ?: emptyList() }
                        .map { it.name })
            }
            override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
        }
    }
}