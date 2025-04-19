package com.example.appmoney.ui.main.feature.input

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appmoney.R
import com.example.appmoney.data.model.Category
import com.example.appmoney.databinding.FragmentInputBinding
import com.example.appmoney.ui.common.helper.Constant.BUNDLE_KEY_TRANSACTION
import com.example.appmoney.ui.common.helper.TabObject
import com.example.appmoney.ui.common.helper.TimeFormat
import com.example.appmoney.ui.common.helper.TimeHelper
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.input.income.IncomeFragment
import com.example.appmoney.ui.main.feature.input.viewPagger.InputViewpagerAdapter
import com.example.appmoney.ui.main.feature.transactionhistory.TransactionDetail
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DateFormat
import java.util.Calendar


class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    private var calendar = Calendar.getInstance()

    private lateinit var viewModel : InputViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel
    private lateinit var adapter: InputViewpagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[InputViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]

        setupViewPager()
        setupObserver()
        setupTabSelected()
        setupDatePicker()
        binding.btnSave.setOnClickListener {
            addDateTrans()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()

        sharedViewModel.getExpenditureCat {
            showApiResultToast(false, it)
        }
        sharedViewModel.getIncomeCat {
            showApiResultToast(false, it)
        }
    }
// add Transaction----------------
    private fun addDateTrans() {
        val currentTab = viewModel.selectedTab.value ?: 0
        (adapter.map[currentTab] as? InputFragmentBehavior)?.getSelectedCategory()?.let {
            category ->
            val sDate = binding.tvDate.text.toString().trim()
            val sAmount = binding.edtMoney.text.toString().trim().toLong()
            val sNote = binding.edtNote.text.toString().trim()
            val typeTrans = if (TabObject.tabPosition == 0) {
                getString(R.string.cat_expenditure)
            } else {
                getString(R.string.cat_income)
            }
            viewModel.addTrans(category.idCat, sDate,sAmount,sNote,typeTrans,
                onSuccess = {
                    showApiResultToast(true)
                    binding.edtNote.setText("")
                    binding.edtMoney.setText("")
                },
                onFailure = {err ->
                    showApiResultToast(false,err)
                })
        }

    }
// Setup Observer ------------------------
    private fun setupObserver() {
        viewModel.selectedTab.observe(viewLifecycleOwner) { tab ->
            binding.Vp.currentItem = tab
        }
        viewModel.err.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErr()
            }
        }

    viewModel.state.observe(viewLifecycleOwner) {
        val dateString = TimeHelper.getByFormat(it.date, TimeFormat.Date)
        binding.apply {
            edtMoney.setText(it.amount.toString())
            edtNote.setText(it.note)
            tvDate.text = dateString
        }
    }
    }
// Setup Date-----------------------------
    private fun setupDatePicker() {
        updateDateText()
        binding.apply {
            btnPre.setOnClickListener { changeDate(-1) }
            btnAfter.setOnClickListener { changeDate(1) }
            tvDate.setOnClickListener { showDatePicker() }
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            calendar.timeInMillis = selection
            updateDateText()
        }
        datePicker.show(childFragmentManager,"DATE_PICKER")
    }

    private fun changeDate(day: Int) {
        calendar.add(Calendar.DAY_OF_MONTH,day)
        updateDateText()
    }

    private fun updateDateText() {
        binding.tvDate.text = TimeHelper.getByFormat(calendar, TimeFormat.Date)
    }
// setup Tab and ViewPager-----------------------------------
    private fun setupTabSelected() {

        binding.tabMoney.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.setTab(it.position)
                    if (it.position == 0) {
                        binding.tvTypeMoney.text = getString(R.string.expenditure_money)
                        binding.btnSave.text = getString(R.string.save_expenditure)

                    } else {
                        binding.tvTypeMoney.text = getString(R.string.income_money)
                        binding.btnSave.text = getString(R.string.save_income)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun setupViewPager() {
        adapter = InputViewpagerAdapter(childFragmentManager, lifecycle)
        binding.Vp.adapter = adapter

        TabLayoutMediator(binding.tabMoney, binding.Vp) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.expenditure_money)
                }

                1 -> {
                    tab.text = getString(R.string.income_money)
                }
            }
        }.attach()
    }
//-----------------------------------
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            binding.Vp.setCurrentItem(TabObject.tabPosition, true)

            val trans = arguments?.getSerializable(BUNDLE_KEY_TRANSACTION,TransactionDetail::class.java)
            trans?.let {

                val tab = if (it.typeTrans == "Expenditure") 0 else 1
                viewModel.setTab(tab)

                val timeMillis = TimeHelper.stringToTimestamp(it.date)?.toDate()
                val date = Calendar.getInstance().apply {
                    timeMillis?.let {
                        time = timeMillis
                    }
                }
                val newState = viewModel.state.value?.copy(
                    date = date,
                    note = it.note,
                    amount = it.amount,
                )
                viewModel.updateState(newState)

                val categoryId = it.categoryId
                (adapter.map[tab] as? InputFragmentBehavior)?.setSelectedCategoryById(categoryId)
            }
        }
    }
}