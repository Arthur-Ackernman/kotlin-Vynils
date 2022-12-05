package com.moviles.vynils.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.moviles.vynils.R
import com.moviles.vynils.databinding.DetailCollectorFragmentBinding
import com.moviles.vynils.models.Collector
import com.moviles.vynils.viewmodels.DetailCollectorViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DetailCollectorFragment : Fragment() {
    private var _binding: DetailCollectorFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailCollectorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailCollectorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_collectors)
        val args: DetailCollectorFragmentArgs by navArgs()
        Log.d("Args", args.collectorId.toString())
        viewModel = ViewModelProvider(this, DetailCollectorViewModel.Factory(activity.application, args.collectorId)).get(
            DetailCollectorViewModel::class.java)
        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> {
            it.apply {
                binding.collector = this
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}