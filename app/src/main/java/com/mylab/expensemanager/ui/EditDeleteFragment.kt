package com.mylab.expensemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alirezaafkar.sundatepicker.DatePicker
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentEditDeleteBinding
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.util.Utils
import org.koin.android.ext.android.inject
import java.util.*


class EditDeleteFragment : Fragment() {
    lateinit var binding: FragmentEditDeleteBinding
    private var dateMillie: Long? = null
    private val editDeleteViewModel: EditDeleteViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDeleteBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedDetails = EditDeleteFragmentArgs.fromBundle(requireArguments()).detailsData

        //binding.editDeleteTitle.setText(receivedDetails.title)
        receivedDetails.apply {
            binding.expenseTitle.text = title
            binding.expenseAmount.setText(amount.toString())
            binding.expenseDate.text = Utils.getPersianDate(date)
            dateMillie = date
            binding.expenseDesc.setText(desc)
        }

        binding.expenseDate.setOnClickListener {
            val minDate: Calendar = Calendar.getInstance()
            val maxDate: Calendar = Calendar.getInstance()
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10)
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10)


            val currentCalendar = Calendar.getInstance()
            activity?.let { it1 ->
                object : DatePicker.Builder() {}
                    .id(1)
                    .minDate(minDate)
                    .maxDate(maxDate)
                    .date(currentCalendar)
                    .build { id, calendar, day, month, year ->

                        dateMillie = calendar?.timeInMillis
                        binding.expenseDate.text = "$year / $month / $day"

                    }
                    .show(it1.supportFragmentManager, "")
            }
        }

        binding.editButton.setOnClickListener {

            val title = binding.expenseTitle.text.toString()
            val amount = binding.expenseAmount.text.toString()
            val desc = binding.expenseDesc.text.toString()
            val amountType = receivedDetails.amountType

            if (title.isNotEmpty() && amount.isNotEmpty() && dateMillie != null) {

                val expense = Expense( receivedDetails.id, title, amount.toLong(), dateMillie!!, desc, amountType)
                editDeleteViewModel.updateExpense(expense)
                Toast.makeText(requireContext(), "با موفقیت ویرایش گردید", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigateUp()

            } else {
                if (title.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "گروه هزینه ای یا درآمدی درج گردد",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (amount.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "مبلغ هزینه یا درآمد درج گردد",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

            }


        }

        binding.deleteButton.setOnClickListener {
            val id = receivedDetails.id
            val title = receivedDetails.title
            val amount = receivedDetails.amount
            val date = receivedDetails.date
            val desc = receivedDetails.desc
            val amountType = receivedDetails.amountType
            val expense = Expense(id, title, amount, date, desc, amountType)

            findNavController().navigate(EditDeleteFragmentDirections.actionEditDeleteFragmentToRoundedDialog(expense))


        }


    }


}