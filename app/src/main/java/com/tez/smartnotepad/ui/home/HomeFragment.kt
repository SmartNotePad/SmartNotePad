package com.tez.smartnotepad.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tez.smartnotepad.R
import com.tez.smartnotepad.ui.profile.ProfileFragment


class HomeFragment : Fragment() {

    private lateinit var demoCollectionAdapter: DemoCollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_base, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        demoCollectionAdapter = DemoCollectionAdapter(this)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = demoCollectionAdapter

        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)

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


        val materialToolbar = view.findViewById<MaterialToolbar>(R.id.topAppBar)

        materialToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuProfile -> {
                    Toast.makeText(requireContext(), "Dıklandım", Toast.LENGTH_SHORT).show()
                    goProfileFragment()
                    true
                }
                else -> super.onOptionsItemSelected(item)
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

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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
