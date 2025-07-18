package com.stuart.palma.cazarpatos

import android.app.Activity
import android.os.Environment
import java.io.*

class ExternalStorageManager(private val actividad: Activity) : FileHandler {

    private val fileName = "DatosExternos.txt"

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val contenido = "${datosAGrabar.first};${datosAGrabar.second}"
        try {
            val archivo = File(actividad.getExternalFilesDir(null), fileName)
            archivo.writeText(contenido)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        return try {
            val archivo = File(actividad.getExternalFilesDir(null), fileName)
            val contenido = archivo.readText()
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
