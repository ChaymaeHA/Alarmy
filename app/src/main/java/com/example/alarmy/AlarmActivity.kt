package com.example.alarmy

import android.Manifest
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlarmActivity : AppCompatActivity() {

    private lateinit var alarmList: MutableList<Alarm>
    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var selectSoundButton: FloatingActionButton
    private lateinit var addAlarmButton: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var ringtonePickerLauncher: ActivityResultLauncher<Intent>  // Sélecteur de sonnerie
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>  // Gestionnaire de permissions
    private var selectedPosition: Int? = null  // Variable pour garder la position sélectionnée

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des alarmes
        alarmList = mutableListOf()

        // Configuration de l'adaptateur RecyclerView
        alarmAdapter = AlarmAdapter(
            this,
            alarmList,
            onItemClick = { position -> openTimePicker(position) },
            onSoundClick = { position -> openSoundPicker(position) }
        )

        recyclerView = findViewById(R.id.recycler_view_alarms)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = alarmAdapter


        addAlarmButton = findViewById(R.id.add)



        addAlarmButton.setOnClickListener {
            openTimePicker()  // Ouvrir TimePicker pour ajouter une nouvelle alarme
        }

        // Initialisation de l'ActivityResultLauncher pour le sélecteur de sonnerie
        ringtonePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val ringtoneUri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                ringtoneUri?.let { uri ->
                    selectedPosition?.let { position ->
                        alarmList[position].soundUri = uri.toString()
                        alarmAdapter.notifyItemChanged(position)
                    }
                }
            }
        }

        // Gestion des permissions pour accéder aux sonneries
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openRingtonePicker()
            } else {
                Toast.makeText(this, "Permission refusée pour accéder aux sonneries.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openSoundPicker(position: Int) {
        selectedPosition = position  // Enregistrer la position de l'alarme sélectionnée
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        // Vérifier si la permission est déjà accordée
        if (checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            openRingtonePicker()
        } else {
            // Demander la permission si elle n'est pas accordée
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun openRingtonePicker() {
        // Lancer le sélecteur de sonnerie
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Sélectionner une sonnerie")
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
        }
        ringtonePickerLauncher.launch(intent)  // Ouvrir le sélecteur de sonnerie
    }

    private fun openTimePicker(position: Int? = null) {
        val intent = Intent(this, TimePickerActivity::class.java)
        startActivityForResult(intent, 100)  // Lancer l'activité de sélection de l'heure
    }

    // Récupérer l'heure sélectionnée dans TimePickerActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val updatedTime = data?.getStringExtra("updated_time") ?: return
            // Ajouter une alarme avec une date fixe (vous pouvez personnaliser cela)
            alarmList.add(Alarm(updatedTime, "Mon, 12 Dec", false))
            alarmAdapter.notifyItemInserted(alarmList.size - 1)
        }
    }
}
