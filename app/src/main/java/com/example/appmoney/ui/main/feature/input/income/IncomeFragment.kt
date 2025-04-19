package com.example.appmoney.ui.main.feature.input.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryType
import com.example.appmoney.databinding.FragmentIncomeBinding
import com.example.appmoney.ui.main.feature.categorytype.CatTypeViewModel
import com.example.appmoney.ui.common.adapter.CategoryAdapter
import com.example.appmoney.ui.common.adapter.CategoryListener
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.input.InputFragmentBehavior
import com.example.appmoney.ui.main.feature.input.InputViewModel
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment


class IncomeFragment : Fragment(), CategoryListener, InputFragmentBehavior {
    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = CategoryAdapter()

    private lateinit var viewModel: IncomeViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setListener(this)

        binding.apply {
            rcIncome.layoutManager = GridLayoutManager(requireContext(), 4)
            rcIncome.adapter = adapter
        }
        initViewModel()
        observer()
    }

    private fun observer() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories?.toMutableList())
        }

        sharedViewModel.incomeList.observe(viewLifecycleOwner) {
            viewModel.init(it)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireParentFragment())[IncomeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.getIncomeCat {
            showApiResultToast(false, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(category: Category) {
        if (category.categoryType == CategoryType.BUTTON) {
            requireActivity().navigateFragment(AppScreen.Category)
        } else {
            viewModel.onSelectedCategory(category)
        }
    }

    override fun setSelectedCategoryById(categoryId: String) {
        viewModel.setSelectedCategoryById(categoryId)
    }

    override fun getSelectedCategory(): Category? {
        return viewModel.getSelectedCat()
    }
}