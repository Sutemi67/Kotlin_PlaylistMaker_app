package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.app.IS_CHECKED_SP_KEY
import com.example.playlistmaker.app.IS_NIGHT_SP_KEY
import com.example.playlistmaker.app.LIGHT_SP_VALUE
import com.example.playlistmaker.app.NIGHT_SP_VALUE
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

        binding.backButton.setOnClickListener { finish() }

        binding.buttonAgreement.setOnClickListener {
            val url = Uri.parse(this.getString(R.string.link_public_offer))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
        binding.buttonSupport.setOnClickListener {
            vm.onSupportClick()
        }

        binding.buttonShare.setOnClickListener {
            vm.onShareClick()
        }

        val checkSharedPrefs = getSharedPreferences(IS_CHECKED_SP_KEY, MODE_PRIVATE)
        val nightModeSharedPrefs = getSharedPreferences(IS_NIGHT_SP_KEY, MODE_PRIVATE)

        nightThemeSwitcher.isChecked = checkSharedPrefs.getBoolean(IS_CHECKED_SP_KEY, false)

        nightThemeSwitcher.setOnClickListener {
            if (nightThemeSwitcher.isChecked) {
                setDefaultNightMode(MODE_NIGHT_YES)
                checkSharedPrefs.edit {
                    putBoolean(
                        IS_CHECKED_SP_KEY,
                        nightThemeSwitcher.isChecked
                    )
                }
                nightModeSharedPrefs.edit { putInt(IS_NIGHT_SP_KEY, NIGHT_SP_VALUE) }
            } else {
                setDefaultNightMode(MODE_NIGHT_NO)
                checkSharedPrefs.edit {
                    putBoolean(
                        IS_CHECKED_SP_KEY,
                        nightThemeSwitcher.isChecked
                    )
                }
                nightModeSharedPrefs.edit { putInt(IS_NIGHT_SP_KEY, LIGHT_SP_VALUE) }
            }
        }
    }
}