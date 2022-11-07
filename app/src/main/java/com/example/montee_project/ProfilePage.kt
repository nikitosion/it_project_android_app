package com.example.montee_project

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentProfilePageBinding
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch

private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_USER_INFO = "$BASE_URL/users/get_user_info"

class ProfilePage : Fragment() {

    companion object {
        fun newInstance(): ProfilePage {
            return ProfilePage()
        }
    }

    private var _binding: FragmentProfilePageBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userImage = binding.userPhoto
        val userName = binding.userNameText
        val userEmail = binding.userEmailText
        val editButton = binding.editUserInfoButton

        val myMealButton = binding.myMealButton
        val myFoodsButton = binding.myFoodsButton
        val exitButton = binding.exitButton

        val sharedPref = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
        userId = sharedPref?.getString("USER_ID", "")

        val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        var user: User

        if (userId == null || userId == "") {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
            transaction.commit()
        } else {
            lifecycleScope.launch {
                user = try {
                    // TODO: Исправить!!! Добавить id
                    client.get(GET_USER_INFO).body()
                } catch (e: JsonConvertException) {
                    User()
                }
                if (user.image != "") {
                    Picasso.get().load(user.image).into(userImage)
                }
                userName.text = user.name
                userEmail.text = user.email
            }
        }

        myMealButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MyMealsPage.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        myFoodsButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MyFoodsPage.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        editButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, UserInformation.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        exitButton.setOnClickListener {
            if (sharedPref != null) {
                sharedPref.edit().remove("USER_ID").apply()
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, LoginPage.newInstance())
            transaction.commit()
        }
    }
}