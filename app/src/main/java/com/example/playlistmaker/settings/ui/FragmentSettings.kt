package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val vm by viewModel<FragmentSettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
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

}