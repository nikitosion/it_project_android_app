package com.example.montee_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentLoginPageBinding
import com.example.montee_project.databinding.FragmentRegistrationPageBinding
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

private const val BASE_URL = "http://192.168.1.44:3000"
private const val POST_USER = "$BASE_URL/users/registrate_user"

class RegistrationPage : Fragment() {
    companion object {
        fun newInstance(): RegistrationPage {
            return RegistrationPage()
        }
    }

    private fun passwordValidation(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"

        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }

    private var _binding: FragmentRegistrationPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = binding.nameInput
        val emailInput = binding.emailInput
        val passwordInput = binding.passwordInput
        val registrationButton = binding.registrationButton
        val haveAccountButton = binding.haveAccountButton

        registrationButton.setOnClickListener {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()
                && passwordValidation(passwordInput.text.toString())
                && nameInput.text?.length!! >= 2
            ) {
                val client = HttpClient() {
                    install(ContentNegotiation) {
                        gson()
                    }
                }
                lifecycleScope.launch {
                    val User = client.post(POST_USER) {
                        contentType(ContentType.Application.Json)
                        setBody(User(null, nameInput.text.toString(), null, emailInput.text.toString(), passwordInput.text.toString()))
                    }
                }
                Toast.makeText(requireContext(), "Вы успешно зарегестрированы", Toast.LENGTH_LONG).show()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
                transaction.commit()

            } else {
                if (nameInput.text?.length!! < 2) {
                    nameInput.error = "Too few characters in name!"
                }
                if (!passwordValidation(passwordInput.text.toString())) {
                    passwordInput.error = "Password is not validated!"
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()) {
                    emailInput.error = "Email address is not correct!"
                }
            }
        }

        haveAccountButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}