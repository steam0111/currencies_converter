package com.modern.currencies.extension

/**
 * remove first element matched by $[predicate]
 * @return true if element was deleted otherwise false
 */

inline fun <T> ArrayList<T>.removeFirst(predicate: (T) -> Boolean): Boolean {
    for (item in this) {
        if (predicate(item))
            return this.remove(item)
    }
    return false
}