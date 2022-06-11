package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.ViewModelFactory
import com.udacity.asteroidradar.adapters.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(requireContext())
    }

    private lateinit var mBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMainBinding.inflate(inflater)
        mBinding.lifecycleOwner = this

        mBinding.viewModel = viewModel

        setHasOptionsMenu(true)

        val adapter = AsteroidAdapter()
        mBinding.asteroidRecycler.adapter = adapter

        adapter.itemClickListener = {
            val bundle = bundleOf("selectedAsteroid" to it)
            findNavController().navigate(R.id.action_showDetail, bundle)
        }

        viewModel.asteroidList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
