package com.seamfix.features.users

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.seamfix.core.model.table.UserEntity
import com.seamfix.features.databinding.UsersFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    /*** ViewModel to be Injected by Hilt ***/
    private val viewModel: UsersViewModel by viewModels()

    private var _binding: UsersFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = UsersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.button.setOnClickListener {
            saveUser()
        }

        //***** ( 2 ) The second style of collecting flows:
        viewModel.getUserFormLocal().onEach {
            binding.tvTotal.text = "Three Total size is ${it.size}"
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        Log.e("UsersFragment", "This code won't be blocked")

        fetchEmployees()
        observeFlow()
    }

    //***** ( 1 ) The first style fo collecting flows:
    private fun observeFlow() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // We call repeatOnLifecycle() so that the flow does not continue collecting endlessly.
            // Note that repeatOnLifecycle() will block lifecycleScope.launch() until the fragment is
            // destroyed https://proandroiddev.com/kotlin-flows-in-android-summary-8e092040fb3a.
            // So if you want to call another code or coroutine inside lifecycleScope.launch(), put them
            // all inside repeatOnLifecycle() and do:

            launch { // If we have only one flow, We don't really need this launch(), but I want to demonstrate how to call
                // multiple flows: We use multiple launch blocks inside the main lifecycleScope.launch{} block.
                viewModel.getFromRemote().collect {
                    Log.e("TestLog", it.firstName ?: "")
                }
            }

            /*launch {
                // ANOTHER block of code.
                viewModel.getUser().collect{
                    binding.tvTotal.text = "Two Total size is ${it.size}"
                }
            }*/
        }
    }

    /**
     * State flow:
     * https://myungpyo.medium.com/stateflow-and-sharedflow-33603d83aff9
     *
     * A stateflow is best for collecting states eg: a UIState, etc.
     * When collecting data from a stateflow, the flow will only emit a new data ONLY IF the new data
     * is actually different from the previous one.
     * In the example below, we are emitting employees from the UserRepository. We are making sure to
     * change the value of the employee name and salary before emitting because the stateflow will
     * only emit a new employee object as long as the new employee object is different from the last one it emitted.
     * So if you go to the UserRepository and hard code the emit line inside the while() loop of getEmployeeFromRemote() to:
     * emit(EmployeeResponse(name = "Jane", salary = 100L))
     * The flow will emit just twice:
     * 1st - will be the default value
     * 2nd - will be EmployeeResponse(name = "Jane", salary = 100L).
     * After this, the flow will not emit anything again no matter how many times the while() runs because
     * you made the emit to emit duplicate values. Therefore the stateflow will ignore any instruction to
     * emit until the value to be emitted changes to something like this for example:
     * emit(EmployeeResponse(name = "Jerry", salary = 6600L)).
     *
     * State flow is used to collecting UIState related objects
     * **/
    private fun fetchEmployees() {
        viewModel.employee.onEach {
            Log.e("UsersFragment", "State emitting...")
            binding.tvLatestEmployee.text = it?.name ?: "NULL VALUE"
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        viewModel.fetchEmployees()
    }

    private fun saveUser() {
        val name = binding.etName.text.toString()
        val age = binding.editTextNumber.text?.toString()?.toInt() ?: 0
        viewModel.save(UserEntity(firstName = name, age = age))
    }

}