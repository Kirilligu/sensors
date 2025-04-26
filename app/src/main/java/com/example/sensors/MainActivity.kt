package com.example.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sensors.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var currentSensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sensorManager = getSystemService(SensorManager::class.java)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.lightRadio -> setupSensor(Sensor.TYPE_LIGHT, R.string.sensorAbsentL)
                R.id.rotationRadio -> setupSensor(Sensor.TYPE_ROTATION_VECTOR, R.string.sensorAbsentR)
                R.id.accelRadio -> setupSensor(Sensor.TYPE_ACCELEROMETER, R.string.sensorAbsentA)
            }
        }
        binding.radioGroup.check(R.id.lightRadio)
    }
    private fun setupSensor(sensorType: Int, errorRes: Int) {
        sensorManager.unregisterListener(this)
        sensorManager.getDefaultSensor(sensorType)?.let {
            currentSensor = it
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
            binding.sensorDataText.text = getString(errorRes)
        }
    }
    override fun onSensorChanged(event: SensorEvent) {
        binding.sensorDataText.text = when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> "Освещенность: ${event.values[0]} lux"
            Sensor.TYPE_ROTATION_VECTOR -> "Проекция вектора по осям:\nX: ${event.values[0]}\nY: ${event.values[1]}\nZ: ${event.values[2]}"
            Sensor.TYPE_ACCELEROMETER -> "Динамическое ускарение по осям::\nX: ${event.values[0]}\nY: ${event.values[1]}\nZ: ${event.values[2]}"
            else -> "Неизвестный датчик"
        }
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onResume() {
        super.onResume()
        currentSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
