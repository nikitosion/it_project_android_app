package com.example.montee_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.montee_project.data_classes.InstructionStep


class InstructionAdapter(
    private val instructionStepList: List<InstructionStep>
) :
    RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    class InstructionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val instructionStepText: TextView = view.findViewById(R.id.instruction_step_text)
        val stepNum: TextView = view.findViewById((R.id.step_num))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstructionViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.my_intruction_step_item, parent, false)

        return InstructionViewHolder(layout)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val myInstructionStep = instructionStepList[position]

        val ingredient_item_string =
            holder.itemView.context.getString(R.string.instruction_step_num)
        holder.stepNum.text = String.format(ingredient_item_string, position + 1)
        holder.instructionStepText.text = myInstructionStep.step_text
    }

    override fun getItemCount(): Int = instructionStepList.size
}