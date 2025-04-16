package com.example.appmoney.ui.main.feature.transactionhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.model.TransAndCat
import com.example.appmoney.databinding.FragmentHistoryTransBinding
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel


class HistoryTransFragment : Fragment() {
    private var _binding: FragmentHistoryTransBinding? = null
    private val binding get() = _binding!!
    private val adapter = HistoryTransAdapter()
    private lateinit var viewModel: HistoryTransViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryTransBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rcHistoryTrans.layoutManager = LinearLayoutManager(requireContext())
            rcHistoryTrans.adapter = adapter

        }
        viewModel = ViewModelProvider(this)[HistoryTransViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]
        viewModel.transactionsDetail.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

    }

    override fun onResume() {
        super.onResume()
        val exps = sharedViewModel.expList.value ?: emptyList()
        val incs = sharedViewModel.incomeList.value ?: emptyList()
        val categories = mutableListOf<Category>()
        categories.addAll(exps)
        categories.addAll(incs)

        viewModel.getTrans(categories) { err ->
            showApiResultToast(false, err)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}