package com.odifek.scanme

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.odifek.scanme.databinding.FragmentHomeBinding
import com.odifek.scanme.utils.safeNavigate

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        with(FragmentHomeBinding.bind(view)) {
            buttonTextRecognition.setOnClickListener {
                navController.safeNavigate(HomeFragmentDirections.actionHomeFragmentToScanTextFragment())
            }
        }
    }
}