package com.stuart.palma.cazarpatos

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private val players: List<Player>)
    : RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTV: TextView = itemView.findViewById(R.id.tvPlayerName)
        val ducksTV: TextView = itemView.findViewById(R.id.tvDucksHunted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        holder.nameTV.text = player.username
        holder.ducksTV.text = player.huntedDucks.toString()
    }

    override fun getItemCount() = players.size
}

