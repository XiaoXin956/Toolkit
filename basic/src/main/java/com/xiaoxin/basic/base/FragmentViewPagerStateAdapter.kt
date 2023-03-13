package com.xiaoxin.basic.base

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xiaoxin.basic.bean.TitleFragmentMenu
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

/**
 * @author: Admin
 * @date: 2022-03-08
 */
class FragmentViewPagerStateAdapter : FragmentStateAdapter {
    var titleFragmentMenus: List<TitleFragmentMenu>

    constructor(
        fragmentActivity: FragmentActivity,
        titleFragmentMenus: List<TitleFragmentMenu>
    ) : super(fragmentActivity) {
        this.titleFragmentMenus = titleFragmentMenus
    }

    constructor(
        fragment: Fragment,
        titleFragmentMenus: List<TitleFragmentMenu>
    ) : super(fragment) {
        this.titleFragmentMenus = titleFragmentMenus
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        titleFragmentMenus: List<TitleFragmentMenu>
    ) : super(fragmentManager, lifecycle) {
        this.titleFragmentMenus = titleFragmentMenus
    }

    override fun createFragment(position: Int): Fragment {
        return titleFragmentMenus[position].fragment
    }

    override fun getItemCount(): Int {
        return titleFragmentMenus.size
    }

}