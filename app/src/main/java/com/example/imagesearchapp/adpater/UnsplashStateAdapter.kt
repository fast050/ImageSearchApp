package com.example.imagesearchapp.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.UnsplashPhotoStateBinding

class UnsplashStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashStateAdapter.LoadStateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            UnsplashPhotoStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: UnsplashPhotoStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetryState.setOnClickListener() {
                retry.invoke()
            }
        }

        fun bind(load: LoadState) {
            binding.apply {

                progressBarState.isVisible = load is LoadState.Loading
                messageToUser.isVisible = load is LoadState.Error
                btnRetryState.isVisible = load is LoadState.Error

            }

        }
    }

}