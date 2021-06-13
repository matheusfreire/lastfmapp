package com.msf.lastfmapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.msf.lastfmapp.R
import com.msf.lastfmapp.databinding.ActivityMainBinding
import com.msf.lastfmapp.viewmodel.LastFmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val lastFmViewModel: LastFmViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onResume() {
        super.onResume()
        binding.txtInputEdit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastFmViewModel.fetchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }
}