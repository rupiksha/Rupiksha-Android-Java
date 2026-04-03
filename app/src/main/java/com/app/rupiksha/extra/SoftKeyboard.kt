package com.app.rupiksha.extra

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class SoftKeyboard(private val activity: Activity) {

    fun setupUI(view: View) {
        try {
            if (view !is EditText) {
                view.setOnTouchListener { _, _ ->
                    hideSoftKeyboard()
                    hideSoftKeyboardFromView(view)
                    false
                }
            }

            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyboard() {
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }, 500)
    }

    fun hideSoftKeyboardFromView(view: View) {
        try {
            view.requestFocus()
            view.postDelayed({
                val keyboard =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(view.windowToken, 0)
            }, 200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideSoftKeyboard() {
        try {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            activity.currentFocus?.let {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun hideKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }

    fun edtRequestKeyboard(view: View, isOpen: Boolean) {
        view.requestFocus()
        view.postDelayed({
            val keyboard =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (isOpen) {
                keyboard.showSoftInput(view, 0)
            } else {
                keyboard.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }, 200)
    }
}
