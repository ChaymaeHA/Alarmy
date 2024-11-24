package com.example.alarmy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity

class TimePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_alarm)

        val timePicker: TimePicker = findViewById(R.id.picker)
        val cancelButton: Button = findViewById(R.id.cancel)
        val validateButton: Button = findViewById(R.id.validate)

        // Initialiser à 00:00
        timePicker.hour = 0
        timePicker.minute = 0

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)  // L'alarme n'est pas ajoutée
            finish()
        }

        validateButton.setOnClickListener {

            val hourFormatted = String.format("%02d", timePicker.hour)
            val minuteFormatted = String.format("%02d", timePicker.minute)

            // Créer l'heure formatée
            val updatedTime = "$hourFormatted:$minuteFormatted"
            val resultIntent = Intent()
            resultIntent.putExtra("updated_time", updatedTime)  // L'heure formatée
            setResult(RESULT_OK, resultIntent)  // Ajouter l'alarme
            finish()
        }
    }
}
