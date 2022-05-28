package com.example.uitests_hilt.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uitests_hilt.R
import com.example.uitests_hilt.databinding.FragmentListOfCitiesBinding
import com.example.uitests_hilt.model.dto.Query
import com.example.uitests_hilt.model.dto.QueryType
import com.example.uitests_hilt.model.dto.Result
import com.example.uitests_hilt.model.dto.ui.UIStateType
import com.example.uitests_hilt.model.entity.CityEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListOfCitiesFragment : Fragment() {

    private val listOfCitiesViewModel by viewModels<ListOfCitiesViewModel>()
    private var _binding: FragmentListOfCitiesBinding? = null
    private val binding get() = _binding!!
    private var cityAdapter: CityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setUpUI(Result(UIStateType.DEFAULT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        listOfCitiesViewModel.uiState.onEach {
            setUpUI(it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        listOfCitiesViewModel.longRunningTaskResult.onEach {
            setUpUI(Result(UIStateType.COMPLETED_LONG_TASK, message = it))
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        getCitiesByPageNumber()
    }

    private fun setUpUI(result: Result) = with(binding) {
        when(result.type) {
            UIStateType.LOADING -> { uiStateLoading() }
            UIStateType.SUCCESS -> { uiStateSuccess(result.cities) }
            UIStateType.NO_RESULT -> { uiStateNoResult() }
            UIStateType.NETWORK_ERROR -> { uiStateNetworkError() }
            UIStateType.LOADING_LONG_TASK -> { uiStateLoadingLongRunningTask() }
            UIStateType.COMPLETED_LONG_TASK -> { uiStateCompletedLongRunningTask(result.message) }
            UIStateType.DEFAULT -> { uiStateDefault() }
        }
    }

    private fun uiStateLoading() = with(binding) {
        tvInfo.visibility = View.INVISIBLE
        tvInfo.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    private fun uiStateSuccess(cities: List<CityEntity>?) = with(binding) {
        if (cities == null) return@with
        tvInfo.visibility = View.GONE
        progressBar.visibility = View.INVISIBLE

        cityAdapter?.cities?.addAll(cities)
        cityAdapter?.notifyDataSetChanged()
    }

    private fun uiStateNoResult() = with(binding) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.setTextColor(resources.getColor(R.color.black))
        tvInfo.text = getString(R.string.msg_the_end)
        tvInfo.isEnabled = false
        progressBar.visibility = View.INVISIBLE
    }

    private fun uiStateNetworkError() = with(binding) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.setTextColor(resources.getColor(R.color.red))
        tvInfo.text = getString(R.string.msg_network_error)
        tvInfo.isEnabled = true
        tvInfo.setOnClickListener {
            getCitiesByPageNumber(listOfCitiesViewModel.getNextPageNumber())
        }
        progressBar.visibility = View.INVISIBLE
    }

    private fun uiStateLoadingLongRunningTask() = with(binding) {
        progressBarTwo.visibility = View.VISIBLE
        button.visibility = View.INVISIBLE
    }

    private fun uiStateCompletedLongRunningTask(successMessage: String?) = with(binding) {
        progressBarTwo.visibility = View.GONE
        button.visibility = View.VISIBLE
        button.text = successMessage ?: ""
    }

    private fun uiStateDefault() = with(binding) {
        setUpRecyclerView()
        setUpSearchView()
        progressBarTwo.visibility = View.GONE
        button.setOnClickListener {
            uiStateLoadingLongRunningTask()
            listOfCitiesViewModel.doLongRunningTask()
        }
    }

    private fun getCitiesByPageNumber(pageNumber: Int = 1) =
        listOfCitiesViewModel.getCities(Query(QueryType.PAGE_NUMBER, pageNumber))

    private fun setUpRecyclerView() = with(binding.recyclerView) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager = linearLayoutManager
        cityAdapter = CityAdapter(requireContext(), mutableListOf()) { navigateToMap(it) }
        adapter = cityAdapter
        setHasFixedSize(true)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    getCitiesByPageNumber(listOfCitiesViewModel.getNextPageNumber())
                }
            }
        })
    }

    private fun setUpSearchView() = with(binding) {
        tvSearch.doAfterTextChanged {
            observeDebounce {
                handleSearchOperation(it.toString())
            }
        }

        tvSearchParent.setEndIconOnClickListener {
            tvSearch.text?.clear()
            getCitiesByPageNumber()
        }
    }

    private fun navigateToMap(cityEntity: CityEntity) {
        val latitude = cityEntity.lat?.toFloat()
        val longitude = cityEntity.lng?.toFloat()
        val cityName = cityEntity.name
        val countryName = cityEntity.countryName
        if (latitude == null || longitude == null || cityName == null || countryName == null) return
        val action = ListOfCitiesFragmentDirections.
            actionListOfCitiesFragmentToMapOfCitiesFragment(latitude, longitude, cityName, countryName)
        findNavController().navigate(action)
    }

    private fun observeDebounce(delay: Long = 1500, function: () -> Unit) = lifecycleScope.launch {
        delay(delay)
        function()
    }

    private fun handleSearchOperation(searchWord: String) {
        if (searchWord.isEmpty() || searchWord.isBlank()) {
            // for cases where the user cleared the search box manually:
            getCitiesByPageNumber()
        } else {
            cityAdapter?.cities?.clear()
            cityAdapter?.notifyDataSetChanged()
            listOfCitiesViewModel.getCities(Query(QueryType.CITY_NAME, searchWord))
        }
    }
}