package com.example.hiketrack

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hiketrack.databinding.ActivityConfiguracionCuentaBinding
import com.example.hiketrack.databinding.ActivityConfiguracionSeguridadBinding

class ConfiguracionSeguridadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfiguracionSeguridadBinding

    private val PICK_CONTACT_REQUEST = 1


    val contactsPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted ->
            if (isGranted) {
                selectContact()
            } else {
                Toast.makeText(this, "Permiso de contactos no concedido", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionSeguridadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cambiarContactoEmergencia.setOnClickListener {
            contactsPermission.launch(android.Manifest.permission.READ_CONTACTS)
        }
    }

    private fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { contactUri ->
                val cursor = contentResolver.query(contactUri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val id = cursor.getString(idIndex)

                    val hasPhoneIndex =
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    val hasPhone = cursor.getString(hasPhoneIndex).toInt()

                    if (hasPhone > 0) {
                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(id),
                            null
                        )

                        if (phoneCursor != null && phoneCursor.moveToFirst()) {
                            val phoneNumberIndex =
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber = phoneCursor.getString(phoneNumberIndex)

                            binding.contactoSeleccionado.text = phoneNumber
                        }
                        phoneCursor?.close()
                    }
                }
                cursor?.close()
            }
        }

    }

    }