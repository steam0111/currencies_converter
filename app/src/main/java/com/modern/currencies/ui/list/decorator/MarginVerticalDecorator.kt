package com.modern.currencies.ui.list.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * add margin for items
 *
 * @param marginResId - general margin
 */
fun RecyclerView.addMarginVerticalItemDecorator(
    marginResId: Int = 0
) {
    this.addItemDecoration(
        MarginVerticalItemDecorator(
            marginResId,
            marginResId,
            marginResId,
            marginResId
        )
    )
}

class MarginVerticalItemDecorator(
    private var leftResId: Int = 0,
    private var topResId: Int = 0,
    private var rightResId: Int = 0,
    private var bottomResId: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        fun getResFromDimen(resId: Int) =
            if (resId == 0) 0 else parent.resources.getDimension(resId).toInt()

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = getResFromDimen(topResId)
        }

        outRect.left = getResFromDimen(leftResId)
        outRect.right = getResFromDimen(rightResId)
        outRect.bottom = getResFromDimen(bottomResId)
    }
}