package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    //TODO
    //* @param Activity * / public void hideSoftKeyboard (Activity Activity) {
    //InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService (Activity.INPUT_METHOD_SERVICE);
    //inputMethodManager.hideSoftInputFromWindow (activity.getCurrentFocus (). getWindowToken (), 0); } -

    val inputMethodManager:InputMethodManager? = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken , InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.isKeyboardOpen(): Boolean{
    val rootView = findViewById<View>(android.R.id.content)
    val visibleBounds = Rect()
    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = rootView.height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(50F).roundToLong()

    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    return this.isKeyboardOpen().not()
}
