package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentTabsFollowBinding


/**
 * A simple [Fragment] subclass.
 * Use the [TabsFollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabsFollowFragment : Fragment() {

    private lateinit var binding: FragmentTabsFollowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabsFollowBinding.inflate(inflater, container, false)

        val viewPager: ViewPager = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout

        val pagerAdapter = TabsPagerAdapter(childFragmentManager)
        viewPager.adapter = pagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        val initialTabIndex = arguments?.getInt("initialTabIndex", 0) ?: 0
        viewPager.currentItem = initialTabIndex

        return binding.root
    }

    private inner class TabsPagerAdapter(fm: androidx.fragment.app.FragmentManager) :
        androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return 2 // Número de pestañas
        }

        override fun getItem(position: Int): Fragment {
            // Retorna el Fragment correspondiente a la pestaña
            return when (position) {
                0 -> FollowersFragment()
                1 -> FollowingFragment()
                else -> throw IllegalArgumentException("Invalid tab position")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            // Retorna el título de la pestaña
            return when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> null
            }
        }
    }

    companion object {
        fun newInstance(initialTabIndex: Int): TabsFollowFragment {
            val fragment = TabsFollowFragment()
            val args = Bundle()
            args.putInt("initialTabIndex", initialTabIndex)
            fragment.arguments = args
            return fragment
        }
    }
}