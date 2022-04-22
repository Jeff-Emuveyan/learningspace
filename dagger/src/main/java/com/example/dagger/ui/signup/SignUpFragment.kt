package com.example.dagger.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dagger.databinding.FragmentLoginBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.button.setOnClickListener {
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
       // findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)

    }
}