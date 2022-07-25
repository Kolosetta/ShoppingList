package com.example.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.presentation.adapter.ShopListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var rvadapter: ShopListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            rvadapter.submitList(it) //Происходит в новом потоке
        }

        binding.floatingActionBtn.setOnClickListener {
            if (isVerticalMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)

            } else {
                supportFragmentManager.popBackStack()
                val fragment = ShopItemFragment.newInstanceAddItem()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.shop_item_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    private fun setupRecyclerView() {
        with(binding.shopRecyclerView) {
            //Вручную задаем размер пула холдеров для каждого типа view, чтобы всегда оставались свободные под отрисовку
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            rvadapter = ShopListAdapter()
            adapter = rvadapter
            setupRecyclerViewListeners(this)
        }

    }

    private fun setupRecyclerViewListeners(rvShopList: RecyclerView) {
        rvadapter.longClickListener = {
            viewModel.changeEnableState(it)
        }
        rvadapter.clickListener = {
            if (isVerticalMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                val fragment = ShopItemFragment.newInstanceEditItem(it.id)
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.shop_item_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        //Установка слушателей на свайпы через ItemTouchHelper
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = rvadapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun isVerticalMode(): Boolean = binding.shopItemContainer == null //Этот элемент существует только в альбомной ориентации

    override fun onEditFinished() {
        supportFragmentManager.popBackStack()
        Log.i("Kek", "Вызван shouldCloseScreen in main")
    }
}