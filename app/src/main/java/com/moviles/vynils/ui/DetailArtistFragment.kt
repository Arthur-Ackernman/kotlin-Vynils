package com.moviles.vynils.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.moviles.vynils.R
import com.moviles.vynils.databinding.DetailArtistFragmentBinding
import com.moviles.vynils.models.Artist
import com.moviles.vynils.viewmodels.DetailArtistViewModel

class DetailArtistFragment : Fragment() {
    private var _binding: DetailArtistFragmentBinding? = null
    private val binding get() = _binding!!
    //private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: DetailArtistViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailArtistFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        //viewModelAdapter = AlbumsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_artist)
        val args: DetailArtistFragmentArgs by navArgs()
        Log.d("Args", args.artist.toString())
        viewModel = ViewModelProvider(this, DetailArtistViewModel.Factory(activity.application, args.artist)).get(
            DetailArtistViewModel::class.java)
        viewModel.artist.observe(viewLifecycleOwner, Observer<Artist> {
            it.apply {
                binding.artist = this
                Glide.with(binding.card.context).load(this.image).into((binding.artistImage))
                Log.d("viewmodel", viewModel.artist.value.toString())
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}