package com.example.myapplication.dosukoityanko.presentation.view.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.dosukoityanko.R
import com.example.myapplication.dosukoityanko.databinding.FragmentTopBinding
import com.example.myapplication.dosukoityanko.presentation.view.likeList.LikeListFragment
import com.example.myapplication.dosukoityanko.presentation.view.restaurantList.RestaurantListFragment
import com.google.android.material.tabs.TabLayoutMediator

class TopFragment : Fragment() {

    data class FragmentParams(
        val fragment: Fragment,
        val tabTitle: String,
        val tabIcon: Int
    )

    private val fragmentList = listOf(
        FragmentParams(
            RestaurantListFragment(),
            "レストラン検索",
            R.drawable.ic_restaurant
        ),
        FragmentParams(
            LikeListFragment(),
            "お気に入り",
            R.drawable.ic_star
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTopBinding.inflate(inflater, container, false).let {
        it.lifecycleOwner = viewLifecycleOwner
        it.viewPager.apply {
            adapter = TopPagerAdapter()
            TabLayoutMediator(it.tabLayout, this, false, true) { tab, position ->
                tab.text = fragmentList[position].tabTitle
                tab.icon = ContextCompat.getDrawable(context, fragmentList[position].tabIcon)
            }.attach()
        }
        it.root
    }

    private inner class TopPagerAdapter : FragmentStateAdapter(this) {

        override fun createFragment(position: Int): Fragment = fragmentList[position].fragment

        override fun getItemCount(): Int = fragmentList.size
    }
}