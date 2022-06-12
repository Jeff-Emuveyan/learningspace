package com.example.dagger.signup.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dagger.R
import com.example.dagger.databinding.FragmentLoginBinding
import com.example.dagger.databinding.FragmentWelcomeBinding
import javax.inject.Inject

//https://github.com/android/architecture-samples/tree/main/app/src/main/java/com/example/android/architecture/blueprints/todoapp
class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<WelcomeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() = with(binding){
        textView2.text = "Welcome, Your session ID is ${viewModel.getSessionId()}"
    }

}