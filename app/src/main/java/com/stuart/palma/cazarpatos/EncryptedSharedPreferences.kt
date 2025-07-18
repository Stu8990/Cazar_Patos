package com.stuart.palma.cazarpatos

import android.app.Activity
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedPreferencesManager(val actividad: Activity) : FileHandler {

    private val fileName = "encrypted_prefs"
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        fileName,
        masterKeyAlias,
        actividad,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        encryptedPrefs.edit().apply {
            putString(LOGIN_KEY, datosAGrabar.first)
            putString(PASSWORD_KEY, datosAGrabar.second)
            apply()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        val email = encryptedPrefs.getString(LOGIN_KEY, "") ?: ""
        val clave = encryptedPrefs.getString(PASSWORD_KEY, "") ?: ""
        return email to clave
    }
}
