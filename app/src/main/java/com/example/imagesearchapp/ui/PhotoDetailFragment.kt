package com.example.imagesearchapp.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imagesearchapp.R
import com.example.imagesearchapp.databinding.FragmentPhotoDetailBinding


class PhotoDetailFragment : Fragment(R.layout.fragment_photo_detail) {

    private var _binding:FragmentPhotoDetailBinding ?= null
    private val binding get() = _binding!!
    private val args:PhotoDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding= FragmentPhotoDetailBinding.bind(view)

        binding.apply {

            userProfileName.text=args.userName
            descriptionByUser.text=getString(R.string.Description, args.description)

            Glide.with(this@PhotoDetailFragment).load(args.urlPhotoUploadByUser)
                .listener(object :RequestListener<Drawable>
            {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    shimmerFrameLayout.isGone=true
                    userProfileImage.isVisible=true
                    userProfileName.isVisible=true
                    uploadImageByUser.isVisible=true
                    userProfileName.text="userName: Unknown "
                    descriptionByUser.text=getString(R.string.Description, "Unknown")

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.apply {

                        shimmerFrameLayout.isGone=true
                        userProfileImage.isVisible=true
                        userProfileName.isVisible=true
                        uploadImageByUser.isVisible=true
                        descriptionByUser.isVisible=args.description!=null && args.description!!.isNotEmpty()
                    }

                    return false
                }

            }).error(R.drawable.ic_no_internet)
            .into(uploadImageByUser)

            Glide.with(this@PhotoDetailFragment).load(args.urlUserProfile).into(userProfileImage)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}