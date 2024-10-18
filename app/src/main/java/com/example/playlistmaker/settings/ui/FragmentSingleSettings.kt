package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSingleSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSingleSettings : Fragment() {
    private lateinit var binding: FragmentSingleSettingsBinding
    private val vm by viewModel<FragmentSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAgreement.setOnClickListener { vm.onAgreementClick() }
        binding.buttonSupport.setOnClickListener { vm.onLinkClick() }
        binding.buttonShare.setOnClickListener { vm.onShareClick() }

        binding.nightThemeSwitch.isChecked = vm.getCheckerPos()
        binding.nightThemeSwitch.setOnClickListener { vm.onThemeCheckerClick() }
    }

    companion object {

//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FragmentSingleSettings().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}