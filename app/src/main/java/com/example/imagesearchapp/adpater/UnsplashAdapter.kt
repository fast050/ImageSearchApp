package com.example.imagesearchapp.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.UnsplashPhotoItemBinding
import com.example.imagesearchapp.model.UnsplashPhotosResponse.Result
import com.example.imagesearchapp.ui.PhotosFragment
import com.example.imagesearchapp.ui.Utils.Companion.NETWORK_VIEW_TYPE
import com.example.imagesearchapp.ui.Utils.Companion.WALLPAPER_VIEW_TYPE



class UnsplashAdapter(private val onClick: PhotosFragment) : PagingDataAdapter<Result, UnsplashAdapter.PhotosViewHolder>(Diff) {

    private var lastPosition=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = UnsplashPhotoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PhotosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photoItem = getItem(position)
        if (photoItem != null) {
            holder.bind(photoItem)
        }

        //animation happen when user scroll down only
        if (holder.adapterPosition>lastPosition){
            val animation=android.view.animation.AnimationUtils.
            loadAnimation(holder.itemView.context,R.anim.scale_up)
            holder.itemView.startAnimation(animation)
        }

        lastPosition=holder.adapterPosition

    }

    inner class PhotosViewHolder(private val binding:UnsplashPhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener()
            {
                val position=getItem(bindingAdapterPosition)

                if (bindingAdapterPosition!=RecyclerView.NO_POSITION)
                {
                    if (position != null) {
                        onClick.onClickPhotoItem(position)
                    }
                }

            }
        }

        fun bind(photoItem: Result) {
            binding.apply {
                photoItem.apply {
                    userProfileName.text = photoItem.user?.username


                    Glide.with(itemView).load(photoItem.urls?.regular).error(R.drawable.ic_no_internet).into(imageUploadByUser)

                    Glide.with(itemView).load(photoItem.user?.profileImage?.medium)
                        .into(userProfileImage)

                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount){
            println("itemCount is $itemCount")
            NETWORK_VIEW_TYPE
        }else {
            WALLPAPER_VIEW_TYPE
        }
    }

    object Diff : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean = oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean = oldItem==newItem
    }


    interface OnClickListenerPhotoItem
    {
         fun onClickPhotoItem(photoItem: Result)
    }
}