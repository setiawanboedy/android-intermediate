package com.example.ourstory.ui.page.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ourstory.R
import com.example.ourstory.core.Status
import com.example.ourstory.databinding.FragmentRegisterBinding
import com.example.ourstory.request.RegisterRequest
import com.example.ourstory.session.Preferences
import com.example.ourstory.utils.snackBar
import com.example.ourstory.utils.textTrim
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var prefs: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener { loginRequest() }
        binding.tvLogin.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
    }

    private fun loginRequest() {
        val emailAddress = binding.editTextTextEmailAddress.textTrim()
        val password = binding.editTextTextPassword.textTrim()
        val name = binding.editTextName.textTrim()
        val confirm = binding.editTextTextConfirmPassword.textTrim()

        val request = RegisterRequest(name, emailAddress, password)

        if (password == confirm)
            viewModelSetup(request)
    }

    private fun viewModelSetup(request: RegisterRequest) {
        viewModel.register(request).observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnRegister.text = ""
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnRegister.text = getString(R.string.register)
                    binding.root.snackBar(res.message ?: getString(R.string.unknown_error))
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.root.snackBar(getString(R.string.register_success))
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        prefs.clearPw()
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}