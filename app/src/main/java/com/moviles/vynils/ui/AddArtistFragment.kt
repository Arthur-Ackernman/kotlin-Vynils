package com.moviles.vynils.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.moviles.vynils.R
import com.moviles.vynils.databinding.AddArtistFragmentBinding
import com.moviles.vynils.viewmodels.AddArtistViewModel

import kotlinx.coroutines.launch

class AddArtistFragment : Fragment() {
    private var _binding: AddArtistFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddArtistViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddArtistFragmentBinding.inflate(inflater, container, false)

        binding.addArtistCreationDate.setOnClickListener {
            showDatePickerDialog()
        }





        binding.lifecycleOwner = viewLifecycleOwner

        val root = binding.root
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireNotNull(this.activity) {

        }

        viewModel = ViewModelProvider(this,
            AddArtistViewModel.Factory(activity.application)).get(AddArtistViewModel::class.java)

        viewModel.eventNetworkError.observe(viewLifecycleOwner, androidx.lifecycle.Observer<Boolean> {
            if (it) showMessageError()

        })

        binding.buttonArtistAdd.setOnClickListener {
            viewModel.saveArtist()
            if (!viewModel.eventNetworkError.value!!) cleanFields()

        }

        binding.buttonArtistCancel.setOnClickListener {
            cleanFields()
        }


        addListener()
        addCollector()
        setDropDown()
        addCollector_hint()
    }

    private fun cleanFields() {
        binding.addArtistCreationDate.setText("")
        binding.addArtistImage.setText("")
        binding.addArtistName.setText("")
        binding.addArtistDescription.setText("")
        binding.autoText.setText("")
    }


    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener {
                datePicker, year, month, day ->
            val days = day.twoDigits()
            val months = (month + 1).twoDigits()

            val selectedDate = "$days/$months/$year"
            binding.addArtistCreationDate.setText(selectedDate)


        })

        newFragment.show(parentFragmentManager, "datePicker")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun Int.twoDigits() =
        if (this <= 9) "0$this" else this.toString()


    private fun showMessageError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(context, getString(R.string.error_add_artist), Toast.LENGTH_LONG).show()
            viewModel.onMessageShown()
        }
    }


    private fun addCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isAddEnabled.collect {
                    binding.buttonArtistAdd.isEnabled = it
                    Log.d("collector ", it.toString())
                }

            }
        }
    }

    private fun addCollector_hint() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artistype.collect {
                    val hint = it
                    when(hint) {
                        viewModel.listTypes[0] -> binding.addArtistCreationDate.hint = "Band creation date"
                        viewModel.listTypes[1] -> binding.addArtistCreationDate.hint = "Musician birthdate"
                        else -> binding.addArtistCreationDate.hint = ""
                    }
                }

            }
        }
    }

    private fun addListener() {
        binding.addArtistName.addTextChangedListener {
            viewModel.setArtistName(it.toString())
            Log.d("Listener", it.toString())
        }
        binding.addArtistDescription.addTextChangedListener {
            viewModel.setArtistDescription(it.toString())
            Log.d("Listener", it.toString())
        }
        binding.addArtistImage.addTextChangedListener {
            viewModel.setArtistImage(it.toString())
            Log.d("Listener", it.toString())
        }
        binding.addArtistCreationDate.addTextChangedListener {
            viewModel.setArtistCreationDate(it.toString())
            Log.d("Listener", it.toString())
        }

        binding.autoText.setOnItemClickListener { adapterView, view, i, l ->
            val text_i = (view as TextView).text.toString()
            viewModel.setType(text_i)
        }
    }

    private fun setDropDown() {
        // val items = listOf("Artist", "Musician")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_types, viewModel.listTypes)
        binding.autoText.setAdapter(adapter)

    }




}