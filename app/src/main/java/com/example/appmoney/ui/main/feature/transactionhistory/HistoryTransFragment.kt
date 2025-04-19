package com.example.appmoney.ui.main.feature.transactionhistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.R
import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.Transaction
import com.example.appmoney.data.model.TransAndCat
import com.example.appmoney.databinding.FragmentHistoryTransBinding
import com.example.appmoney.ui.common.helper.BundleHelper
import com.example.appmoney.ui.common.helper.Constant.BUNDLE_KEY_TRANSACTION
import com.example.appmoney.ui.common.helper.DialogHelper
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.input.InputFragment
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment
import java.util.Calendar


class HistoryTransFragment : Fragment(),TransDetailListener {
    private var _binding: FragmentHistoryTransBinding? = null
    private val binding get() = _binding!!
    private val adapter = HistoryTransAdapter()
    private lateinit var viewModel: HistoryTransViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryTransBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HistoryTransViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]
        binding.apply {
            rcHistoryTrans.layoutManager = LinearLayoutManager(requireContext())
            rcHistoryTrans.adapter = adapter
            adapter.setListener(this@HistoryTransFragment)
        }
        setupDeleteTrans(binding.rcHistoryTrans,adapter)
        setupSearch()
        setupDateDiaLog()

        viewModel.transactionsDetail.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        binding.searchView.queryHint = getString(R.string.hint_search)
    }
// setupDigalog Date--------------
    private fun updateText() {
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val date = "$month/$year"
        binding.tvDate.text = "Tháng $date"

    val exps = sharedViewModel.expList.value ?: emptyList()
    val incs = sharedViewModel.incomeList.value ?: emptyList()
    val categories = mutableListOf<Category>()
    categories.addAll(exps)
    categories.addAll(incs)

    viewModel.getTransByMonth(date,categories) { err ->
        showApiResultToast(false, err)
    }
    }

    private fun setupDateDiaLog() {
        binding.tvDate.setOnClickListener {
            MonthYearDialog(requireContext()){ month, year ->
                calendar.set(Calendar.MONTH,month-1)
                calendar.set(Calendar.YEAR,year)
                updateText()
            }.show()
        }
        binding.btnPre.setOnClickListener {
            calendar.add(Calendar.MONTH,-1)
            updateText()
        }
        binding.btnAfter.setOnClickListener {
            calendar.add(Calendar.MONTH,1)
            updateText()
        }
    }

// Setup search Transaction---------
    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.filterTrans(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.filterTrans(it)
                }
                return true
            }
        })
    }
// Delete Transaction------------
    private fun setupDeleteTrans(recyclerView: RecyclerView, adapter: HistoryTransAdapter) {
        val itemTouchHelperCallBack =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val item = adapter.currentList[position]
                    DialogHelper.showRequestDialog(
                        requireContext(),
                        "Xóa giao dịch",
                        "Bạn có muốn xóa giao dịch này không",
                        onConfirm = {
                            viewModel.delTrans(
                                item.idTrans,
                                onSuccess = {
                                    showApiResultToast(true)
                                    val newList = adapter.currentList.toMutableList()
                                    newList.removeAt(position)
                                    adapter.submitList(newList)
                                },
                                onFailure = { showApiResultToast(false, it)})
                        },
                        onCancel = {adapter.notifyItemChanged(position)})
                }
            }
        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        updateText()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(trans: TransactionDetail) {
        val bundle = BundleHelper.addParam(BUNDLE_KEY_TRANSACTION,trans).build()
        Log.d("InputFragment", "transaction: $trans")
        requireActivity().navigateFragment(AppScreen.Input,bundle)
    }
}