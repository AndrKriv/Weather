package com.example.weather.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentsViewBinding<T : ViewBinding>(val initial: (View) -> T, val fragment: Fragment) :
    ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        binding ?: initial(thisRef.requireView()).also {
            binding = it
            thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
        }

    override fun onDestroy(owner: LifecycleOwner) {
        binding = null
        owner.lifecycle.removeObserver(this)
    }
}

inline fun <reified T : ViewBinding> Fragment.viewBinding(noinline initial: (View) -> T) =
    FragmentsViewBinding(initial, this)