package com.moviles.vynils.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.moviles.vynils.R
import com.moviles.vynils.databinding.CreateAlbumFragmentBinding
import com.moviles.vynils.models.Album
import com.moviles.vynils.models.NewAlbum
import com.moviles.vynils.viewmodels.CreateAlbumViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.seconds

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CreateAlbumFragment : Fragment() {
    private var _binding: CreateAlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateAlbumViewModel
    private lateinit var button: Button
    private lateinit var tfName: TextInputLayout
    private lateinit var tfCover: TextInputLayout
    private lateinit var tfReleaseDate: TextInputLayout
    private lateinit var tfDescription: TextInputLayout
    private lateinit var tfGenre: TextInputLayout
    private lateinit var tfRecordLabel: TextInputLayout
    private lateinit var teReleaseDate: TextInputEditText

    private val genres = listOf("Classical", "Salsa", "Rock", "Folk")
    private val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CreateAlbumFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        button = binding.bAdd
        tfName = binding.tfName
        tfCover = binding.tfCover
        teReleaseDate = binding.teReleaseDate
        tfReleaseDate = binding.tfReleaseDate
        tfDescription = binding.tfDescription
        tfGenre = binding.tfGenre
        tfRecordLabel = binding.tfRecordLabel

        var adapter = ArrayAdapter(requireContext(), R.layout.list_item, genres)
        (tfGenre.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        adapter = ArrayAdapter(requireContext(), R.layout.list_item, recordLabels)
        (tfRecordLabel.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        teReleaseDate.setOnClickListener() {
            showDatPicker()
        }

        button.setOnClickListener {
            var newAlbum = NewAlbum(
                tfName.editText?.text.toString(),
                tfCover.editText?.text.toString(),
                tfReleaseDate.editText?.text.toString(),
                tfDescription.editText?.text.toString(),
                tfGenre.editText?.text.toString(),
                tfRecordLabel.editText?.text.toString()
            )

            Toast.makeText(view.context, "Album Creted", Toast.LENGTH_LONG).show()
            clearError()
            if(validationForm(newAlbum)) viewModel.sendDataFromNetwork(newAlbum)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_album)
        viewModel = ViewModelProvider(this, CreateAlbumViewModel.Factory(activity.application)).get(
            CreateAlbumViewModel::class.java)
        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {
            it.apply {
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

    private fun showDatPicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        datePicker.show(childFragmentManager,"data_picker" )

        datePicker.addOnPositiveButtonClickListener { datePicked ->

            tfReleaseDate.editText?.setText(convertLongToTime(datePicked));

        }
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun validationForm(newAlbum: NewAlbum): Boolean {
        if(newAlbum.name.isEmpty()) {
            tfName.error = getString(R.string.create_album_name_error)
            return false
        }else if(newAlbum.cover.isEmpty()) {
            tfCover.error = getString(R.string.create_album_cover_error)
            return false
        } else if(newAlbum.releaseDate.isEmpty()) {
            tfReleaseDate.error = getString(R.string.create_album_date_error)
            return false
        } else if(newAlbum.description.isEmpty()) {
            tfDescription.error = getString(R.string.create_album_description_error)
            return false
        } else if(newAlbum.genre.isEmpty()) {
            tfGenre.error = getString(R.string.create_album_genre_error)
            return false
        } else if(newAlbum.recordLabel.isEmpty()) {
            tfRecordLabel.error = getString(R.string.create_album_record_error)
            return false
        } else return true
    }

    private fun clearError() {
        tfName.error = null
        tfCover.error = null
        tfReleaseDate.error = null
        tfDescription.error = null
        tfGenre.error = null
        tfRecordLabel.error = null
        tfRecordLabel.error = null
    }
}