package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.Comment
import com.squareup.picasso.Picasso


class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.findViewById(R.id.user_image)
        val userName: TextView = view.findViewById(R.id.user_name)
        val commentDate: TextView = view.findViewById(R.id.comment_date)
        val commentText: TextView = view.findViewById(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.comment_item, parent, false)

        return CommentViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]

        if (comment.user_image != null && comment.user_image != "") {
            Picasso.get().load(comment.user_image).fit().centerCrop()
                .placeholder(R.drawable.carbonara_image)
                .into(holder.userImage)
        }
        holder.userName.text = comment.user_name
        holder.commentDate.text = comment.date
        holder.commentText.text = comment.text
    }

    override fun getItemCount(): Int = commentList.size
}