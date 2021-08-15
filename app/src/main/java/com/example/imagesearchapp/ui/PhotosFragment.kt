package com.example.imagesearchapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.imagesearchapp.R
import com.example.imagesearchapp.adpater.UnsplashAdapter
import com.example.imagesearchapp.adpater.UnsplashStateAdapter
import com.example.imagesearchapp.databinding.FragmentPhotosBinding
import com.example.imagesearchapp.model.UnsplashPhotosResponse
import com.example.imagesearchapp.viewmodel.UnsplashViewModel
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearchapp.adpater.GridItemDecoration
import com.example.imagesearchapp.ui.Utils.Companion.WALLPAPER_VIEW_TYPE


class PhotosFragment : Fragment(R.layout.fragment_photos), UnsplashAdapter.OnClickListenerPhotoItem {

    private val viewModel: UnsplashViewModel by viewModels()
    private var isErrorLayoutShow = false
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private  val adapterUnsplash by lazy { UnsplashAdapter(this) }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.home_menu, menu)

        val searchViewItem = menu.findItem(R.id.app_bar_search)

        val searchView = searchViewItem.actionView as SearchView

        searchView.queryHint="Search Photo..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.isNotEmpty())
                        viewModel.searchPhotos(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
        )


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding= FragmentPhotosBinding.bind(view)

        setHasOptionsMenu(true)


        //set up recyclerView
        //the linearGrid value:
        // true is linearLayoutManger
        //false is gridLayoutManger
        initRecycler(2,linearGrid = true)


        //data into the recyclerView
        viewModel.getPhotos.observe(viewLifecycleOwner) {
            adapterUnsplash.submitData(lifecycle = viewLifecycleOwner.lifecycle, it)
        }

        //to refresh the layout when no connection at the start
        binding.btnRetry.setOnClickListener() {
            adapterUnsplash.refresh()
        }

       //the layout flow of the fragment when it start
        setUpLayoutFlow()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setUpLayoutFlow()
    {
        binding.apply {
            adapterUnsplash.addLoadStateListener {

                    loadState ->

                if (loadState.source.refresh is LoadState.Loading) {

                    if (!isErrorLayoutShow) {
                        println("1")
                        recyclerView.isInvisible = true
                        shimmerFrameLayout.isVisible = true
                        layoutError.isInvisible = true
                        shimmerFrameLayout.startShimmer()
                    } else {
                        println("2")
                        recyclerView.isInvisible = true
                        shimmerFrameLayout.isInvisible = true
                        btnRetry.isInvisible = true
                        progressBar.isVisible = true

                    }
                }

                if (loadState.source.refresh is LoadState.NotLoading) {
                    println("3")
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.isGone = true
                    layoutError.isInvisible = true
                    recyclerView.isVisible = true
                    progressBar.isInvisible = true
                }

                if (loadState.source.refresh is LoadState.Error) {
                    println("4")
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.isInvisible = true
                    recyclerView.isInvisible = true
                    isErrorLayoutShow = true
                    layoutError.isVisible = true
                    progressBar.isInvisible = true
                }

            }
        }
    }


    //the linearGrid value:
    //true is linearLayoutManger
    //false is gridLayoutManger
    private fun initRecycler(spanCount:Int,linearGrid:Boolean)
    {
        binding.recyclerView.apply {
            val stateAdapter=UnsplashStateAdapter { adapterUnsplash.retry() }
            adapter = adapterUnsplash.withLoadStateFooter(footer = stateAdapter)


            if (!linearGrid) {
                //progress bar at the mid when use gridlayout
                val gridLayoutManager = GridLayoutManager(context, spanCount)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        val viewType = adapterUnsplash.getItemViewType(position)
                        return if (viewType == WALLPAPER_VIEW_TYPE) 1
                        else spanCount
                    }
                }
                layoutManager = gridLayoutManager
            }
            val largePadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing) //16dp
            val smallPadding = resources.getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small) //4dp
            addItemDecoration(GridItemDecoration(largePadding, smallPadding))

        }
    }

    override fun onClickPhotoItem(photoItem: UnsplashPhotosResponse.Result) {
        photoItem.apply {
            val userProfileName = user?.username
            val userUploadPhoto = urls?.full
            val userProfileImage = user?.profileImage?.medium
            val userDescription = description

            val action = PhotosFragmentDirections.actionPhotosFragment2ToPhotoDetailFragment2(
                userProfileName, userDescription, userUploadPhoto, userProfileImage
            )
            findNavController().navigate(action)

        }
    }

}