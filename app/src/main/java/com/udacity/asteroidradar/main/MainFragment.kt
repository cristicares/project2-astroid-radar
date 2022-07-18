package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    private var viewModelAdapter: AsteroidListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModelAdapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener{
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer { asteroid ->
            if (asteroid != null){
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidList.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.apply {
                viewModelAdapter?.submitList(asteroids)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_week_menu -> viewModel.updateAsteroidFilter(MenuOption.SHOW_WEEK)
            R.id.show_today_menu -> viewModel.updateAsteroidFilter(MenuOption.SHOW_TODAY)
            R.id.show_saved_menu -> viewModel.updateAsteroidFilter(MenuOption.SHOW_SAVED)
            else -> viewModel.updateAsteroidFilter(MenuOption.SHOW_SAVED)
        }
        return true
    }
}
