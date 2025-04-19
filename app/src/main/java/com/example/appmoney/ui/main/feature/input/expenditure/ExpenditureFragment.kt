package com.example.appmoney.ui.main.feature.input.expenditure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.CategoryType
import com.example.appmoney.databinding.FragmentExpenditureBinding
import com.example.appmoney.ui.common.adapter.CategoryAdapter
import com.example.appmoney.ui.common.adapter.CategoryListener
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.input.InputFragmentBehavior
import com.example.appmoney.ui.main.feature.input.InputViewModel
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment


class ExpenditureFragment : Fragment(),CategoryListener, InputFragmentBehavior {
    private var _binding : FragmentExpenditureBinding? = null
    private val binding get() = _binding!!
    private val adapter = CategoryAdapter()

    private lateinit var viewModel: ExpenditureViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenditureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setListener(this)

        binding.apply {
            rcExpenditure.layoutManager = GridLayoutManager(requireContext(),4)
            rcExpenditure.adapter = adapter
        }
        initViewModel()
        observer()
    }
    private fun observer() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapter.submitList(categories?.toMutableList())
        }

        sharedViewModel.expList.observe(viewLifecycleOwner) {
            viewModel.init(it)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireParentFragment())[ExpenditureViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(category: Category) {
        if (category.categoryType == CategoryType.BUTTON) {
            requireActivity().navigateFragment(AppScreen.Category)
        }
        else {
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