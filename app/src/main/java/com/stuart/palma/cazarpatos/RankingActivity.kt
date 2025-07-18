package com.stuart.palma.cazarpatos

import Database.RankingPlayerDBHelper
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager


class RankingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        // Eliminar datos previos y probar CRUD
        RankingPlayerDBHelper(this).deleteAllRanking()
        RankingPlayerDBHelper(this).insertRanking(Player("Jugador1", 10))
        val hunted = RankingPlayerDBHelper(this).readDucksHuntedByPlayer("Jugador1")
        RankingPlayerDBHelper(this).updateRanking(Player("Jugador1", 5))
        RankingPlayerDBHelper(this).deleteRanking("Jugador1")

        // Grabar datos de prueba
        val jugadores = arrayListOf(
            Player("Stuart.Palma", 10),
            Player("Jugador2", 6),
            Player("Jugador3", 3),
            Player("Jugador4", 9)
        ).sortedByDescending { it.huntedDucks }
        jugadores.forEach { RankingPlayerDBHelper(this).insertRanking(it) }

        // Mostrar en RecyclerView
        val lista = RankingPlayerDBHelper(this).readAllRanking()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewRanking)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RankingAdapter(lista)
        recyclerView.setHasFixedSize(true)
    }

}

