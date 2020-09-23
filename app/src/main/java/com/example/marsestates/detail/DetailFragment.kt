package com.example.marsestates.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marsestates.R
import com.example.marsestates.databinding.FragmentDetailBinding

/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        setHasOptionsMenu(true)
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val marsProperty = DetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
        // Create an instance of the ViewModel Factory.
        val viewModelFactory = DetailViewModelFactory(marsProperty, application)
        detailViewModel = ViewModelProvider(
            this, viewModelFactory).get(DetailViewModel::class.java)
        // Giving the binding access to the DetailViewModel.
        binding.viewModel = detailViewModel

        // Add an Observer to the state variable for Navigating when UpButton is tapped.
        detailViewModel.navToOverview.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToOverviewFragment()
                )
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                detailViewModel.doneNavigating()
            }
        })

        // Add an Observer to the state variable for selectedProperty
        detailViewModel.selectedProperty.observe(viewLifecycleOwner, {
            // Updating the UI's priceValueText accordingly
            when (it.isRental) {
                true -> binding.priceValueText.setTextColor(resources.getColor(R.color.beige, activity?.theme))
                false -> binding.priceValueText.setTextColor(resources.getColor(R.color.misty_rose, activity?.theme))
            }
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> detailViewModel.navBack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}