package dingshaoshuai.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewStubProxy
import dingshaoshuai.base.R
import dingshaoshuai.base.databinding.BasePlaceholderViewBinding

class PlaceholderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: BasePlaceholderViewBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.base_placeholder_view, this, true)

    val loadingViewStub: ViewStubProxy = binding.loadingViewStub
    val emptyViewStub: ViewStubProxy = binding.emptyViewStub
    val errorViewStub: ViewStubProxy = binding.errorViewStub
}