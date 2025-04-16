package com.example.appmoney.ui.main.feature.categorytype.catExp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.data.model.Category
import com.example.appmoney.databinding.FragmentCatExpBinding
import com.example.appmoney.ui.common.helper.BundleHelper
import com.example.appmoney.ui.common.helper.Constant.BUNDLE_KEY_CATEGORY
import com.example.appmoney.ui.common.helper.DialogHelper
import com.example.appmoney.ui.main.feature.categorytype.viewPagger.CatTypeOnClickListener
import com.example.appmoney.ui.main.feature.categorytype.viewPagger.CategoryTypeAdapter
import com.example.appmoney.ui.common.helper.showApiResultToast
import com.example.appmoney.ui.main.feature.categorytype.CatTypeViewModel
import com.example.appmoney.ui.main.main_screen.AppScreen
import com.example.appmoney.ui.main.main_screen.ScreenHomeViewModel
import com.example.appmoney.ui.main.main_screen.navigateFragment


class CatExpFragment : Fragment(), CatTypeOnClickListener {

    private var _binding : FragmentCatExpBinding? = null
    private val bingding get() = _binding!!
    private  var adapter = CategoryTypeAdapter()

    private lateinit var viewModel: CatTypeViewModel
    private lateinit var sharedViewModel: ScreenHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatExpBinding.inflate(inflater,container, false)
        return bingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelOnwer = this.parentFragment ?: requireActivity()
        viewModel = ViewModelProvider(viewModelOnwer)[CatTypeViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[ScreenHomeViewModel::class.java]
        adapter.setListener(this)
        bingding.rcCatExp.layoutManager = LinearLayoutManager(requireContext())
        bingding.rcCatExp.adapter = adapter
        setupSwipeToDelete(bingding.rcCatExp,adapter)
        sharedViewModel.expList.observe(viewLifecycleOwner){ list->
            adapter.submitList(list)
        }
    }
    private fun setupSwipeToDelete(recyclerView: RecyclerView, adapter: CategoryTypeAdapter) {
        val itemTouchHelperCallback =
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
                        "Xóa danh mục",
                        "Bạn có chắc muốn xóa danh mục?",
                        onConfirm = {
                            viewModel.delCategory(
                                "Expenditure",item.idCat,
                                onSuccess = {
                                    showApiResultToast(true)
                                    val newList = adapter.currentList.toMutableList()
                                    newList.removeAt(position)
                                    adapter.submitList(newList)
                                },
                                onFailure = { showApiResultToast(false, it) })
                        },
                        onCancel = {
                            adapter.notifyItemChanged(position)
                        }
                    )
                }
            }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: Category) {
        val bundle = BundleHelper.addParam(BUNDLE_KEY_CATEGORY, item).build()
        requireActivity().navigateFragment(AppScreen.Fix, bundle)
    }
}