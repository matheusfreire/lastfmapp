package com.msf.lastfmapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.msf.lastfmapp.R
import com.msf.lastfmapp.databinding.ActivityMainBinding
import com.msf.lastfmapp.model.Error
import com.msf.lastfmapp.model.Results
import com.msf.lastfmapp.viewmodel.LastFmViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val lastFmViewModel: LastFmViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        showEmptyLayout(getString(R.string.empty), R.drawable.ic_music)
    }

    private fun showEmptyLayout(msg: String, @DrawableRes drawable: Int) {
        with(binding.msgLayout){
            msgEmptyLayout.text = msg
            imageEmptyLayout.setImageResource(drawable)
        }
    }

    private fun setUpRecyclerView() {
        with(binding.recyclerViewMusics) {
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        with(lastFmViewModel) {
            liveDataResults.observe(this@MainActivity) { musicResults ->
                handleMusicResults(musicResults)
            }
            liveDataLoading.observe(this@MainActivity) {
                handleLoadingViews(it)
            }
            liveDataError.observe(this@MainActivity) {
                handleErrorsView(it)
            }
        }
        binding.txtInputEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lastFmViewModel.fetchTracks(s.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
    }

    private fun handleMusicResults(musicResults: Results) {
        val adapter =
            MusicAdapter(musicResults.trackmatches.track, this@MainActivity)
        binding.recyclerViewMusics.adapter = adapter
    }

    private fun handleLoadingViews(it: Boolean) {
        binding.progressLastFm.isVisible = it
        binding.msgLayout.msgEmptyLayout.isVisible = !it
        binding.msgLayout.imageEmptyLayout.isVisible = !it
    }

    private fun handleErrorsView(it: Error) {
        binding.recyclerViewMusics.isVisible = false
        showEmptyLayout(it.message, R.drawable.ic_error)
        binding.msgLayout.msgEmptyLayout.isVisible = true
        binding.msgLayout.imageEmptyLayout.isVisible = true
    }
}