package com.example.montee_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.montee_project.data_classes.Ingredient
import com.example.montee_project.data_classes.InstructionStep
import com.example.montee_project.data_classes.Meal
import com.example.montee_project.databinding.FragmentSearchBinding
import java.util.*

class SearchPage : Fragment() {
    companion object {
        fun newInstance(): SearchPage {
            return SearchPage()
        }
    }

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchField
        val searchList = binding.searchList

        val meal_carbonara = Meal(
            "1", "Карбонара", null, 30, "лёгкая",
            1031, 104,
            listOf(
                Ingredient("1", 0.125),
                Ingredient("2", 0.05),
                Ingredient("3", 1.0),
                Ingredient("4", 0.025),
                Ingredient
                    ("5", 0.05),
                Ingredient("6", 0.05),
                Ingredient("7", 0.025),
                Ingredient("8", 2.0),
                Ingredient("9", 0.005),
                Ingredient("10", 0.005)
            ),
            703, 37, 30, 71, listOf("sports"), listOf("month", "season"), listOf(
                InstructionStep(
                    listOf(Ingredient("1")),
                    "Вскипятите воду в большой кастрюле и сварите пасту до состояния аль денте. Обычно для этого нужно варить ее на минуту меньше, чем указано на пачке."
                ),
                InstructionStep(
                    listOf(
                        Ingredient("2"),
                        Ingredient("4"),
                        Ingredient("3"),
                        Ingredient("5")
                    ),
                    "Пока паста варится, растопите на сковороде масло и обжарьте на нем мелко нарезанные лук, чеснок и бекон. До мягкости и до отчетливого чесночного и жаренобеконного запаха."
                ),
                InstructionStep(
                    listOf(
                        Ingredient("8"),
                        Ingredient("6"),
                        Ingredient("7"),
                        Ingredient("9"),
                        Ingredient("10")
                    ),
                    "Снимите сковороду с огня и в глубокой миске взбейте четыре яичных желтка со сливками и тертым пармезаном. Посолите и поперчите смесь, еще раз взбейте."
                ),
                InstructionStep(
                    listOf(Ingredient("10")),
                    "В готовые спагетти вывалите обжаренные с луком и чесноком кусочки бекона. Влейте смесь сливок, желтков и пармезана, перемешайте. И сразу подавайте, посыпав свеженатертым сыром и черным молотым перцем."
                )
            )
        )

        val meal_cesar = Meal("2", "Салат «Цезарь»", null, 30, "лёгкая",
            5046, 451, listOf(Ingredient("11", 0.1), Ingredient("12", 0.1),
                Ingredient("13", 0.1), Ingredient("14", 0.05), Ingredient("15", 0.03),
                Ingredient("16", 1.0), Ingredient("17", 0.05)
            ),245,20,11,14,
            listOf("sports", "vegan"), listOf("month", "morning"), listOf(
                InstructionStep(listOf(Ingredient("11")), "Промыть, просушить и нарвать на небольшие кусочки листья салата, отложить в холодильник."),
                InstructionStep(listOf(Ingredient("15"), Ingredient("16")), "В горячую сковородку положить сливочное масла. После того, как оно полностью расплавится и начнет шипеть, кинуть нарезанный на пластины зубчик чеснока."),
                InstructionStep(listOf(Ingredient("15"), Ingredient("16"), Ingredient("14")), "В ту же сковородку добавить еще сливочного масла и зубчик чеснока. В это время нарезать на небольшие кубики хлеб. Положить в сковороду и обжаривать до румяной корочки. Желательно непрерывно помешивать, чтобы не подгорело."),
                InstructionStep(listOf(Ingredient("12"), Ingredient("14"), Ingredient("17")), "Достать листья салата, туда же положить обжаренную куриную грудку, помидоры, нарезанные тонкой соломкой. Заправить соусом «Цезарь». Перемешать. Сверху положить получившиеся сухарики и натереть сыр.")
            ))

        val meal_boloneze = Meal("3", "Болоньезе", null, 110, "средняя", 985, 12,
            listOf(Ingredient("18", 0.05), Ingredient("19", 1.0), Ingredient("20", 0.05),
                Ingredient("21", 0.03), Ingredient("22", 0.1), Ingredient("23", 0.125),
                Ingredient("24", 0.15), Ingredient("25", 0.2), Ingredient("26", 20.0),
                Ingredient("27", 0.125), Ingredient("28", 0.05), Ingredient("29",0.05)
            ),
            778, 26, 57, 44, listOf("sports"), listOf("month"), listOf(
                InstructionStep(listOf(Ingredient("18"), Ingredient("19"), Ingredient("20"), Ingredient("28"), Ingredient("21")), "Разогрейте оливковое масло в большой сковороде и добавьте измельченные чеснок, лук, морковь и сельдерей. Тушите на маленьком огне 5 минут до мягкости овощей."),
                InstructionStep(listOf(Ingredient("23"), Ingredient("24"), Ingredient("22"), Ingredient("26")), "Увеличьте огонь и добавьте фарш. Перемешайте деревянной лопаткой, разломав комочки. Добавьте говяжий бульон, вино, помидоры и петрушку. Доведите до кипения, уменьшите огонь и тушите 1,5 часа, иногда помешивая. Посолите и добавьте специи."),
                InstructionStep(listOf(Ingredient("28")), "Перед подачей сварите спагетти, слейте и подавайте с пармезаном.")
            ))

        val meal_sirniki = Meal("4", "Сырники", null, 30, "лёгкая", 5890, 351,
            listOf(Ingredient("30", 0.125), Ingredient("31", 0.025), Ingredient("32", 1.0),
                Ingredient("33", 37.5), Ingredient("34", 12.5), Ingredient("35", 1.0), Ingredient("36", 0.0)
            ),
            516, 27, 21, 55, listOf("sports", "vegan"), listOf("morning", "interesting"), listOf(
                InstructionStep(listOf(Ingredient("30"), Ingredient("32"), Ingredient("31"), Ingredient("35"), Ingredient("33")), "Творог нужно брать обязательно сухой и рассыпчатый, из мягкого не получится. Чем выше процент жирности, тем вкуснее будут сырники. Размять творог вилкой, смешать с яйцом, сахаром и ванилином. Затем добавить 90 г муки и перемешать."),
                InstructionStep(listOf(Ingredient("33")), "Сырная масса не должна быть ни слишком сухой, ни слишком влажной. Смоченными в воде руками взять немного массы и скатать из нее шарик, затем сдавить шарик ладонями и придать сырнику плоскую форму. Обвалять сформованные сырники в муке."),
                InstructionStep(listOf(Ingredient("36")), "На сковороде разогреть масло и на среднем огне обжарить сырники с обеих сторон до золотистой корочки. Готовые сырники выложить на бумажные полотенца, чтобы избавиться от лишнего жира. Подавать со сметаной.")
            ))

        val meals: MutableList<Meal> = mutableListOf(meal_carbonara, meal_cesar, meal_boloneze, meal_sirniki,
            meal_boloneze, meal_carbonara)

        searchList.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = MealAdapter(meals)
        searchList.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }
}