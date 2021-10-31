package io.github.geeksforgeinc.blackscreen.utils

import android.widget.CompoundButton

/**
 * @param silent set isSilent property, used to indicate setChecked should not be handled by OnCheckedChangeListener
 */
fun CompoundButton.setChecked(checked: Boolean, silent: Boolean) {
    if (silent) {
        if (isChecked != checked) {
            // use tag to store the state
            tag = true
            isChecked = checked
        }
    }
    else {
        isChecked = checked
    }
}

/**
 * @return true if OnCheckedChangeListener should not handle this callback
 */
val CompoundButton.isSilent: Boolean
    get() {
        val value = tag != null
        if (value) tag = null
        return value
    }