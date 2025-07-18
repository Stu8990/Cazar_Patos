package com.stuart.palma.cazarpatos

import android.app.Activity
import java.io.*

class InternalStorageManager(private val actividad: Activity) : FileHandler {

    private val nombreArchivo = "datosIngreso.txt"

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val contenido = "${datosAGrabar.first};${datosAGrabar.second}"
        try {
            actividad.openFileOutput(nombreArchivo, Activity.MODE_PRIVATE).use {
                it.write(contenido.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        return try {
            val contenido = actividad.openFileInput(nombreArchivo).bufferedReader().useLines { lines ->
                lines.joinToString("")
            }
            val partes = contenido.split(";")
            if (partes.size == 2) {
                partes[0] to partes[1]
            } else {
                "" to ""
            }
        } catch (e: IOException) {
            "" to ""
        }
    }
}
