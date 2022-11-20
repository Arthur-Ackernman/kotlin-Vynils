package com.moviles.vynils.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.moviles.vynils.R
import com.moviles.vynils.databinding.ArtistItemBinding
import com.moviles.vynils.models.Artist
import com.moviles.vynils.ui.ArtistFragmentDirections

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    var artists :List<Artist> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false)
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        var artist: Artist? = null
        holder.viewDataBinding.also {
            artist = artists[position]
            it.artist = artist
        }
        holder.loadImage(artists[position])

        holder.viewDataBinding.root.setOnClickListener {
            val action = ArtistFragmentDirections.actionArtistFragmentToDetailArtistFragment(artist!!)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item

        }

        fun loadImage(artistModel: Artist){

            Glide.with(viewDataBinding.ivArtistImage.context)
                .load(artistModel.image.toUri().buildUpon().scheme("https").build())
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(viewDataBinding.ivArtistImage)
        }

    }
}