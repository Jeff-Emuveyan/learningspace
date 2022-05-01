package com.example.dagger.signup.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dagger.R
import com.example.dagger.databinding.FragmentLoginBinding
import javax.inject.Inject

class SignUpFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SignUpViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Obtaining the application component graph from SignUpActivity and instantiate
        // the @Inject fields with objects from the graph
        (activity as SignUpActivity).applicationComponent.inject(this)
        // Now you can access SignUpViewModel here and onCreateView too
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() = with(binding) {
        tvAssignedAppId.text = "Application ID is: ${viewModel.getApplicationId().toString()}"
        tvAssignedUserId.text = "User ID is: ${viewModel.getUserId().toString()}"
        button.setOnClickListener {
            validateAndSignUpUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateAndSignUpUser() {
        val userName = binding.editTextTextPersonName.text.toString()
        val age = binding.editTextNumber.text.toString()
        if (userName.isEmpty() || age.isEmpty()) {
            Toast.makeText(requireContext(), "Missing Input", Toast.LENGTH_LONG).show()
        } else {
            val ageAsInt = age.toInt()
            sighUpUser(userName, ageAsInt)
        }
    }

    private fun sighUpUser(name: String, age: Int) {
        viewModel.sighUpUser(name, age)
        // We are using 'un-safe args' just to be fast. In Production code, use safe-args.
        findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)

    }
}