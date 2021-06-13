package com.msf.lastfmapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msf.lastfmapp.R
import com.msf.lastfmapp.model.Track

class MusicAdapter(private val musics: List<Track>, private val context: Context) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_music, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(getMusic(position)) {
            holder.musicTitle.text = name
            holder.musicArtist.text = artist

            Glide.with(context).load(albumPhoto()).placeholder(R.drawable.ic_music)
                .into(holder.imageAlbum)
        }
    }

    private fun getMusic(position: Int) = musics[position]

    override fun getItemCount(): Int = musics.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val musicTitle: TextView = view.findViewById(R.id.musicTitle)
        val musicArtist: TextView = view.findViewById(R.id.musicArtist)
        val imageAlbum: AppCompatImageView = view.findViewById(R.id.musicAlbum)
    }

}