package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsActivity : AppCompatActivity() {

    //    private val vm by viewModel<SettingsViewModel>()
    private lateinit var vm: SettingsViewModel
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

        vm = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                SettingsInteractor(
                    SettingsRepository(this)
                )
            )
        )[SettingsViewModel::class.java]

        binding.backButton.setOnClickListener { finish() }
        binding.buttonAgreement.setOnClickListener { vm.onAgreementClick() }
        binding.buttonSupport.setOnClickListener { vm.onLinkClick() }
        binding.buttonShare.setOnClickListener { vm.onShareClick() }

        vm.isChecked.observe(this) {
            Log.e("theme", "changing to $it in observer")
            nightThemeSwitcher.isChecked = !it
            when (!it) {
                true -> setDefaultNightMode(MODE_NIGHT_YES)
                else -> setDefaultNightMode(MODE_NIGHT_NO)
            }
        }

        nightThemeSwitcher.setOnClickListener {
            vm.onThemeCheckerClick()
        }
    }
}