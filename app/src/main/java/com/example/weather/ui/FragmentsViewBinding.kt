package com.example.weather.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentsViewBinding<T:ViewBinding>(val initial: (View) -> T, val fragment: Fragment):
    ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver{

    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T = binding ?: initial(thisRef.requireView()).also {
        binding = it
        thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
    }

//      _binding?.let { return it }
//
//        if (_binding == null) {
//            _binding = initial(thisRef.requireView())
//        }
//        return _binding!!
//        return this._binding ?: initial(thisRef.requireView()).also {
//            this._binding = it
//        }

    override fun onDestroy(owner: LifecycleOwner){
        binding = null
        owner.lifecycle.removeObserver(this)
    }
}

inline fun <reified T : ViewBinding> Fragment.viewBinding(noinline initial:(View)->T) = FragmentsViewBinding(initial,this)


//fun <T> Fragment.viewBinding(initialise: () -> T): ReadOnlyProperty<Fragment, T> =
//    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
//
//        private var binding: T? = null
//
//        // This is called JUST before onDestroyView in a Fragment as a limitation of the lifecycle
//        //  library. Do not try to access this property in onDestroyView, as it would
//        //  implicitly call the initialise function again and provide a new value.
//        override fun onDestroy(owner: LifecycleOwner) {
//            binding = null
//            this@viewBinding.lifecycle.removeObserver(this)
//            super.onDestroy(owner)
//        }
//
//        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
//            binding
//                ?: initialise().also {
//                    binding = it
//                    this@viewBinding.viewLifecycleOwner.lifecycle.addObserver(this)
//                }
//    }
