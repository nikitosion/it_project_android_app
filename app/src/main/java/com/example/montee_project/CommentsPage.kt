package com.example.montee_project

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Comment
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.data_classes.User
import com.example.montee_project.databinding.FragmentCommentsPageBinding
import com.squareup.picasso.Picasso
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.launch
import java.util.*

private const val ARG_PARAM1 = "meal_id"
private const val BASE_URL = "https://appmontee.herokuapp.com"
private const val GET_MEALS_BY_ID = "$BASE_URL/meals/get_meals_by_id"
private const val GET_COMMENTS = "$BASE_URL/comments/get_comment_by_meal_id"
private const val POST_COMMENT = "$BASE_URL/comments/add_comment"
private const val GET_USER_INFO = "$BASE_URL/users/get_user_info"

class CommentsPage : Fragment() {
    private var mealId: String? = null

    private var _binding: FragmentCommentsPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(mealId: String) =
            CommentsPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, mealId)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.backButton
        val mealName = binding.mealName
        val mealImage = binding.mealImage
        val commentsList = binding.commentsList
        val commentInput = binding.commentInput
        val commentSendButton = binding.commentSendButton

        // По нажатию на кнопку назад осуществляется переход на страницу с информацией о блюде
        backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.nav_host_fragment, MealReciepePage.newInstance(mealId.toString()))
            transaction.commit()
        }

        val client = HttpClient {
            install(ContentNegotiation) {
                gson()
            }
        }

        commentsList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        var meal: Meal
        var comments: List<Comment>

        lifecycleScope.launch {
            // Получаем информацию о блюде
            meal = try {
                client.get(GET_MEALS_BY_ID) {
                    url {
                        parameters.append("meal_id", mealId.toString())
                    }
                }.body()
            } catch (e: JsonConvertException) {
                Meal()
            }

            mealName.text = meal.name
            if (meal.image != null && meal.image != "") {
                Picasso.get().load(meal.image).placeholder(R.drawable.carbonara_image).fit()
                    .into(mealImage)
            }

            // Получаем комментарии на данное блюдо
            comments = try {
                client.get(GET_COMMENTS) {
                    url {
                        parameters.append("meal_id", mealId.toString())
                    }
                }.body()
            } catch (e: NoTransformationFoundException) {
                listOf()
            }

            commentsList.adapter = CommentAdapter(comments.filter { it.meal_id == mealId })
        }

        // Если пользователь зарегестрирован, то по нажатию на кнопку "Отправить" создаётся комментарий
        commentSendButton.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE)
            val userId = sharedPref?.getString("USER_ID", "")

            if (userId != "") {
                lifecycleScope.launch {
                    val user: User = try {
                        client.get(GET_USER_INFO) {
                            url {
                                parameters.append("user_id", userId.toString())
                            }
                        }.body()
                    } catch (e: JsonConvertException) {
                        User()
                    }

                    val newCommentsList: List<Comment> = try {
                        client.post(POST_COMMENT) {
                            contentType(ContentType.Application.Json)
                            setBody(
                                Comment(
                                    null,
                                    mealId,
                                    user.id,
                                    user.name,
                                    user.image,
                                    DateFormat.format("dd MM yyyy", Date()).toString(),
                                    commentInput.text.toString()
                                )
                            )
                        }.body()
                    } catch (e: JsonConvertException) {
                        listOf()
                    }
                    commentsList.adapter =
                        CommentAdapter(newCommentsList.filter { it.meal_id == mealId })
                }
            }
        }
    }
}