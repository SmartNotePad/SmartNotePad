package com.tez.smartnotepad.ui.fragment.home

import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tez.smartnotepad.R
import com.tez.smartnotepad.core.BaseFragment
import com.tez.smartnotepad.databinding.FragmentHomeBaseBinding
import com.tez.smartnotepad.ui.fragment.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBaseBinding>(
        FragmentHomeBaseBinding::inflate
    ) {

    private val viewPagerAdapter = ViewPagerAdapter(this)

    override fun initContentsOfViews() {
        with(binding) {
            viewPager.adapter = viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                val tabText = when (position) {
                    0 -> {
                        "My Notes"
                    }
                    1 -> {
                        "Shared Notes"
                    }
                    else -> {
                        "Unknown Tab"
                    }
                }
                tab.text = tabText
            }.attach()
        }
    }

    override fun initListener() {
        with(binding) {
            topAppBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuProfile -> {
                        goProfileFragment()
                        true
                    }
                    else -> {
                        super.onOptionsItemSelected(item)
                    }
                }
            }
        }
    }

    private fun goProfileFragment() {
        val profileFragment = ProfileFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, profileFragment)
        transaction.addToBackStack(ProfileFragment::class.java.simpleName)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }
}

private class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyNotesFragment()
            1 -> SharedNotesFragment()
            else -> MyNotesFragment()
        }
    }
}
