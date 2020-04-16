package com.example.airside_demo.utils


object Validation {

    fun isNotNull(mString: String?): Boolean {
        return if (mString == null) {
            false
        } else if (mString.equals("", ignoreCase = true)) {
            false
        } else if (mString.equals("N/A", ignoreCase = true)) {
            false
        } else if (mString.equals("[]", ignoreCase = true)) {
            false
        } else if (mString.equals("null", ignoreCase = true)) {
            false
        } else
            !mString.equals("{}", ignoreCase = true)
    }
}
