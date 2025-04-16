package com.example.appmoney.ui.main.feature.categorytype

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.R
import com.example.appmoney.databinding.FragmentCategoryTypeBinding
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.categorytype.viewPagger.VPCatTypeAdapter
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator


class CatTypeFragment : Fragment() {
    private var _binding: FragmentCategoryTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var catTypeViewModel: CatTypeViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryTypeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catTypeViewModel = ViewModelProvider(this)[CatTypeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]

        binding.vpCatType.setCurrentItem(TabObject.tabPosition,true)
        setupViewPagerTransaction()
        setupTabSelected()

        binding.btnAddCategory.setOnClickListener {
            requireActivity().navigateFragment(AppScreen.Edit)
        }

        binding.btnBacktrans.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupTabSelected() {
        binding.tabCatType.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    catTypeViewModel.setTabSelected(it.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setupViewPagerTransaction() {
        val adapter = VPCatTypeAdapter(childFragmentManager, lifecycle)
        binding.vpCatType.adapter = adapter
        TabLayoutMediator(binding.tabCatType, binding.vpCatType) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.transaction_expenditure)
                1 -> tab.text = getString(R.string.transaction_income)
            }
        }.attach()


        catTypeViewModel.tabSelected.observe(viewLifecycleOwner){ tab ->
            binding.vpCatType.setCurrentItem(tab, true)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.vpCatType.currentItem = TabObject.tabPosition
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        binding.vpCatType.currentItem = TabObject.tabPosition
        sharedViewModel.getIncomeCat { showApiResultToast(false, it) }
        sharedViewModel.getExpenditureCat { showApiResultToast(false, it) }

        if (!hidden) {
            catTypeViewModel.setTabSelected(TabObject.tabPosition)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}