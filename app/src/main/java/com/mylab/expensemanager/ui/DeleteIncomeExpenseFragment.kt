package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentDeleteIncomeExpenseBinding
import com.mylab.expensemanager.databinding.FragmentEditDeleteBinding
import org.koin.android.ext.android.inject


class DeleteIncomeExpenseFragment : Fragment() {
    lateinit var binding: FragmentDeleteIncomeExpenseBinding
    private val deleteIncomeExpenseViewModel: DeleteIncomeExpenseViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteIncomeExpenseBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteButton.setOnClickListener {


            val title = binding.expenseDeleteCost.text.toString().trim()
            if (title.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "نام گروه درآمدی یا هزینه ای درج گردد",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                deleteIncomeExpenseViewModel.existenceExpenseSpec(title)
                deleteIncomeExpenseViewModel.expenseSpec.observe(viewLifecycleOwner) {
                    try {
                        if (it.title == title) {
                            deleteIncomeExpenseViewModel.deleteExpenseSpec(title)
                            deleteIncomeExpenseViewModel.deleteExpense(title)
                            Toast.makeText(
                                requireContext(),
                                "با موفقیت حذف گردید",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            findNavController().navigateUp()
                        }

                    } catch (e: NullPointerException) {
                        Toast.makeText(
                            requireContext(),
                            "گروهی با عنوان درج شده یافت نگردید",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }

                }
            }

        }

    }

}