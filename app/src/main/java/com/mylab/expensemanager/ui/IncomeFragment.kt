package com.mylab.expensemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mylab.expensemanager.IncomeListAdapter
import com.mylab.expensemanager.databinding.FragmentIncomeBinding
import com.mylab.expensemanager.datamodel.Expense
import com.mylab.expensemanager.datamodel.ExpenseSpec
import org.koin.android.ext.android.inject

private const val TAG = "IncomeFragment"
class IncomeFragment : Fragment() {
    lateinit var binding: FragmentIncomeBinding
    private val incomeViewModel: IncomeViewModel by inject()
    private var doubleList = mutableListOf<ExpenseSpec>()
    private var multipleList = mutableListOf<ExpenseSpec>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.incomeRegisterButton.setOnClickListener {
            findNavController().navigate(
                IncomeFragmentDirections.actionIncomeFragmentToIncomeEntryFragment(1)
            )

        }
        
        incomeViewModel.expenseSpecs.observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated: $it")
            val listAdapter = IncomeListAdapter()
            listAdapter.submitList(incomeViewModel.expenseSpecs.value)
            binding.incomeRecycler.adapter = listAdapter
//            listAdapter.notifyDataSetChanged()
            /*it.forEachIndexed { index, expenseSpec ->
                if (index < 2) doubleList.add(expenseSpec)
                Log.i("MyTag", doubleList.toString())
            }*/
        }



        binding.incomeModel = incomeViewModel
        binding.lifecycleOwner = this




    }


}