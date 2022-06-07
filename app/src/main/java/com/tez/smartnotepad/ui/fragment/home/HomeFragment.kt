package com.tez.smartnotepad.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tez.smartnotepad.R
import com.tez.smartnotepad.databinding.FragmentHomeBaseBinding
import com.tez.smartnotepad.ui.fragment.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var _binding: FragmentHomeBaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(this)

        with(binding){

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

            topAppBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuProfile -> {
                        goProfileFragment()
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)

        Log.e("POSITION", position.toString())
        return when (position) {
            0 -> MyNotesFragment()
            1 -> SharedNotesFragment()
            else -> MyNotesFragment()
        }

    }
}
