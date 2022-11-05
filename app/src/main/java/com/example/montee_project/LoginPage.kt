package com.example.montee_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentLoginPageBinding
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

private const val BASE_URL = "http://192.168.1.44:3000"
private const val LOGIN_USER = "$BASE_URL/users/login_user"

class LoginPage : Fragment() {

    companion object {
        fun newInstance() : LoginPage {
            return LoginPage()
        }
    }

    private var _binding: FragmentLoginPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login_input = binding.loginInput
        val passwordInput = binding.passwordInput
        val loginButton = binding.loginButton
        val noAccountButton = binding.noAccountButton

        loginButton.setOnClickListener {
            lifecycleScope.launch {
                val client = HttpClient() {
                    install(ContentNegotiation) {
                        gson()
                    }
                }
                if (login_input.text != null && passwordInput.text != null) {
                    val user = try {
                         client.get(LOGIN_USER) {
                            url {
                                parameters.append("email", login_input.text.toString())
                                parameters.append("password", passwordInput.text.toString())
                            }
                        }.body()
                    } catch (e: NoTransformationFoundException) {
                        User()
                    }
                    if (user.id != null) {
                        val sharedPref = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
                        sharedPref?.edit()?.putString("USER_ID", user.id)?.apply()

                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.nav_host_fragment, ProfilePage.newInstance())
                        transaction.commit()
                    }
                }
            }

        }

        noAccountButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, RegistrationPage.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}