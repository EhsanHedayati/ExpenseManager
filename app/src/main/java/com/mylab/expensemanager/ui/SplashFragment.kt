package com.mylab.expensemanager.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mylab.expensemanager.R
import com.mylab.expensemanager.databinding.FragmentSplashBinding
import kotlin.concurrent.timer

private const val TAG = "SplashFragment"

class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        object : CountDownTimer(3000, 1000) {
            override fun onTick(counter: Long) {
                Log.i(TAG, "onTick: $counter")
            }

            override fun onFinish() {

                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToStartFragment())

            }

        }.start()


    }


}