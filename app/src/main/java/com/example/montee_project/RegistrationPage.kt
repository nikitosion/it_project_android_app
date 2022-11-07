package com.example.montee_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentRegistrationPageBinding
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

private const val BASE_URL = "https://appmontee.herokuapp.com"
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

        // По нажатию на кнопку все поля поля проверяются на корректность, и если все данные корректны,
        // то пользователь регистрируется
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
                        setBody(
                            User(
                                null,
                                nameInput.text.toString(),
                                null,
                                emailInput.text.toString(),
                                passwordInput.text.toString()
                            )
                        )
                    }
                }
                Toast.makeText(requireContext(), "Вы успешно зарегестрированы", Toast.LENGTH_LONG)
                    .show()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
                transaction.commit()

            } else {
                if (nameInput.text?.length!! < 2) {
                    nameInput.error = "Слишком мало символов в имени!"
                }
                if (!passwordValidation(passwordInput.text.toString())) {
                    passwordInput.error = "Пароль не соотвествует требованиям!"
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString())
                        .matches()
                ) {
                    emailInput.error = "Адрес эл. почты неккоректен!"
                }
            }
        }

        // Переход на страницу со входом
        haveAccountButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}