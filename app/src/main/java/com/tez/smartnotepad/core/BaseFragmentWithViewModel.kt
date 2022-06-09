package com.tez.smartnotepad.core


import androidx.viewbinding.ViewBinding

abstract class BaseFragmentWithViewModel<VB : ViewBinding, out VM : BaseViewModel>(
    private val inflate: Inflate<VB>
) : BaseFragment<VB>(inflate) {

    abstract val viewModel: VM

}