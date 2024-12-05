package com.example.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private lateinit var binding: FragmentMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var tabAdapter: MediaViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabAdapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = tabAdapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
    }
}