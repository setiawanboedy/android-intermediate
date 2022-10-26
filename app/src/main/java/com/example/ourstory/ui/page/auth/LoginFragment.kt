package com.example.ourstory.ui.page.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ourstory.MainActivity
import com.example.ourstory.R
import com.example.ourstory.core.Status
import com.example.ourstory.databinding.FragmentLoginBinding
import com.example.ourstory.domain.request.LoginRequest
import com.example.ourstory.session.SessionManager
import com.example.ourstory.utils.snackBar
import com.example.ourstory.utils.textTrim
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener { loginRequest() }

    }

    private fun loginRequest() {
        val emailAddress = binding.editTextTextEmailAddress.textTrim()
        val password = binding.editTextTextPassword.textTrim()

        val request = LoginRequest(emailAddress, password)
        viewModel.login(request).observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnLogin.text = ""
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogin.text = getString(R.string.login)
                    binding.root.snackBar(res.message ?: getString(R.string.unknown_error))
                }
                Status.SUCCESS -> {
                    val data = res.data?.loginResult
                    if (data != null) {
                        session.setUser(data)
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.root.snackBar(getString(R.string.login_success))
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                    session.setLogin(true)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}