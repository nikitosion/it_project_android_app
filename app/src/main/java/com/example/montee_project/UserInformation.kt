package com.example.montee_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentUserInformationBinding
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_USER_INFO = "$BASE_URL/users/get_user_info"
private const val EDIT_USER_PASSWORD = "$BASE_URL/users/edit_user_password"
private const val EDIT_USER_IMAGE = "$BASE_URL/users/edit_user_image"

class UserInformation : Fragment() {

    companion object {
        fun newInstance() : UserInformation {
            return UserInformation()
        }
    }

    private fun passwordValidation(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"

        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }

    private var _binding: FragmentUserInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInformationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileImage = binding.profileImage
        val imageLinkInput = binding.imageLinkInput
        val nameInput = binding.nameInput
        val emailInput = binding.emailInput
        val newPasswordInput = binding.newPasswordInput
        val confirmButton = binding.confirmButton

        var user = User()

        val client = HttpClient() {
            install(ContentNegotiation) {
                gson()
            }
        }

        lifecycleScope.launch {
            val sharedPref = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val userId = sharedPref?.getString("USER_ID", "")

            user = try {
                client.get(GET_USER_INFO) {
                    url {
                        parameters.append("user_id", userId.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                User()
            }
            if (user.image != "") {
                Picasso.get().load(user.image).into(profileImage)
            }
            nameInput.setText(user.name)
            emailInput.setText(user.email)
        }

        confirmButton.setOnClickListener {
            if (newPasswordInput.text.toString() != "" && passwordValidation(newPasswordInput.text.toString())) {
                lifecycleScope.launch {
                     val userNewPass: User = try {
                        client.post(EDIT_USER_PASSWORD) {
                            url {
                                parameters.append("id", user.id.toString())
                                parameters.append("new_password", newPasswordInput.text.toString())
                            }
                        }.body()
                    } catch (e: JsonConvertException) {
                        User()
                    }
                    if (userNewPass != User()) {
                        Toast.makeText(
                            activity,
                            "Пароль успешно изменён!",
                            Toast.LENGTH_LONG
                        ).show()
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.add(R.id.nav_host_fragment, ProfilePage.newInstance())
                        transaction.commit()
                    } else {
                        Toast.makeText(
                            activity,
                            "Что-то пошло не так!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                newPasswordInput.error = "Пароль не соответсвует требованиям или поле пустое!"
            }
            if (imageLinkInput.text.toString() != "") {
                lifecycleScope.launch {
                    val newImageUser: User = try {
                        client.post(EDIT_USER_IMAGE) {
                            url {
                                parameters.append("id", user.id.toString())
                                parameters.append("image", imageLinkInput.text.toString())
                            }
                        }.body()
                    } catch (e: JsonConvertException) {
                        User()
                    }
                    Toast.makeText(requireContext(), "Изображение успешно изменено", Toast.LENGTH_SHORT).show()
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.nav_host_fragment, ProfilePage.newInstance())
                    transaction.commit()
                }
            }
        }
    }
}