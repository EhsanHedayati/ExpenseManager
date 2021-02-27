package com.mylab.expensemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentStartBinding
import kotlinx.coroutines.*


class StartFragment : Fragment() {

    lateinit var binding: FragmentStartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startButton.visibility = View.GONE

        firstAnimation()
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Dispatchers.Main) {
                secondAnimation()
            }


            withContext(Dispatchers.Main) {
                enabledButton()
            }
        }

        binding.startButton.setOnClickListener {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToMainFragment())
        }


    }

    private fun firstAnimation() {
        val a: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
        a.reset()
        binding.tv1.startAnimation(a)
    }


    private suspend fun secondAnimation() {
        delay(2000)
        val a: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale)
        binding.tv2.startAnimation(a)

    }

    private suspend fun enabledButton() {
        delay(4000)
        binding.startButton.visibility = View.VISIBLE
    }


}





