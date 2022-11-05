package com.example.montee_project

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.*
import com.squareup.picasso.Picasso


class MyMealInstructionStepAdapter(private val instructionStepList: List<InstructionStepDB>, val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MyMealInstructionStepAdapter.MyMealInstructionStepViewHolder>() {

    class MyMealInstructionStepViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Функия для обработки нажатия
        fun bind(myInstructionStepDB: InstructionStepDB, itemClickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onClick(myInstructionStepDB)
            }
        }

        val instructionStepText: TextView = view.findViewById(R.id.instruction_step_text)
        val stepNum: TextView = view.findViewById((R.id.step_num))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMealInstructionStepViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.my_intruction_step_item, parent, false)

        return MyMealInstructionStepViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyMealInstructionStepViewHolder, position: Int) {
        val myInstructionStepDB = instructionStepList[position]

        val ingredient_item_string = holder.itemView.context.getString(R.string.instruction_step_num)
        holder.stepNum.text = String.format(ingredient_item_string, position + 1)
        holder.instructionStepText.text = myInstructionStepDB.step_text

        holder.bind(myInstructionStepDB, itemClickListener)
    }

    // Класс - обработчик нажатия
    class OnItemClickListener(val clickListener: (myInstructionStep: InstructionStepDB) -> Unit) {
        fun onClick(myInstructionStep: InstructionStepDB) = clickListener(myInstructionStep)
    }

    override fun getItemCount(): Int = instructionStepList.size
}