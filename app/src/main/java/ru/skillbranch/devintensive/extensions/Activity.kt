package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputMethodManager:InputMethodManager? = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken , InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.isKeyboardOpen(): Boolean {
    var r: Rect = Rect()
    val rootView = window.decorView
    rootView.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootView.height
    var heightDiff = screenHeight - (r.bottom - r.top)

    return heightDiff > 128
}

fun Activity.isKeyboardClosed(): Boolean {
    var r: Rect = Rect()
    val rootView = window.decorView
    rootView.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootView.height
    var heightDiff = screenHeight - (r.bottom - r.top)

    return heightDiff < 128
}