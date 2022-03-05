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

        viewModel.getUserFormLocal().onEach {
            binding.tvTotal.text = "Three Total size is ${it.size}"
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        Log.e("UsersFragment", "This code won't be blocked")
        observeFlow()
    }

    private fun observeFlow() = lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // We call repeatOnLifecycle() so that the flow does not continue collecting endlessly.
            // Note that repeatOnLifecycle() will block lifecycleScope.launch() until the fragment is
            // destroyed https://proandroiddev.com/kotlin-flows-in-android-summary-8e092040fb3a.
            // So if you want to call another code or coroutine inside lifecycleScope.launch(), put them
            // all inside repeatOnLifecycle() and do:

            launch { // If we have only one flow, We don't really need this launch(), but I want to demonstrate how to call
                // multiple flows.
                viewModel.getFromRemote().collect {
                    Log.e("TestLog", it.firstName ?: "")
                }
            }

            /*launch {
                // other blocks of code.
                viewModel.getUser().collect{
                    binding.tvTotal.text = "Two Total size is ${it.size}"
                }
            }*/
        }
    }

    private fun saveUser() {
        val name = binding.etName.text.toString()
        val age = binding.editTextNumber.text?.toString()?.toInt() ?: 0
        viewModel.save(UserEntity(firstName = name, age = age))
    }

}