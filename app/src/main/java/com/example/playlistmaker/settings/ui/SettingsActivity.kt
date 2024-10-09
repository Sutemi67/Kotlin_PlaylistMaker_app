package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val vm by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nightThemeSwitcher = binding.nightThemeSwitch

        binding.settingsToolbar.setNavigationOnClickListener { finish() }
        binding.buttonAgreement.setOnClickListener { vm.onAgreementClick() }
        binding.buttonSupport.setOnClickListener { vm.onLinkClick() }
        binding.buttonShare.setOnClickListener { vm.onShareClick() }

        nightThemeSwitcher.isChecked = vm.getCheckerPos()
        nightThemeSwitcher.setOnClickListener { vm.onThemeCheckerClick() }
    }
}