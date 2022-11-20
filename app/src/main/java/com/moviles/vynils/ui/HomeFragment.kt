package com.moviles.vynils.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moviles.vynils.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.homeAlbumImage.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAlbumFragment())
        }
        binding.homeArtistImage.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArtistFragment())
        }
        binding.homeCollectorImage.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCollectorFragment())
        }

        return binding.root
    }
}