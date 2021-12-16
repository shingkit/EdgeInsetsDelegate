package com.shingkit.bstcompose.ui
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import com.shingkit.bstcompose.doOnApplyWindowInsets
// 三大金刚 对
// 手势 状态栏 错
class EdgeInsetDelegate2(private val activity: Activity) {

    private var everGivenInsetsToDecorView = false

    fun start() {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
//        activity.window.statusBarColor = Color.TRANSPARENT
        everGivenInsetsToDecorView = false
        activity.window.decorView.doOnApplyWindowInsets { windowInsets, _, _ ->
            val navigationBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val isGestureNavigation = isGestureNavigation(navigationBarsInsets)
            if (!isGestureNavigation) {
                // Let decorView draw the translucent navigationBarColor
                ViewCompat.onApplyWindowInsets(activity.window.decorView, windowInsets)
                everGivenInsetsToDecorView = true
            } else if (isGestureNavigation && everGivenInsetsToDecorView) {
                // Let DecorView remove navigationBarColor once it has bean drawn
                val noBottomInsets = WindowInsetsCompat.Builder()
                    .setInsets(
                        WindowInsetsCompat.Type.navigationBars(),
                        Insets.of(
                            navigationBarsInsets.left,
                            navigationBarsInsets.top,
                            navigationBarsInsets.right,
                            0
                        )
                    )
                    .build()
                ViewCompat.onApplyWindowInsets(activity.window.decorView, noBottomInsets)
            }

        }
    }

    private fun isGestureNavigation(navigationBarsInsets: Insets): Boolean {
        val threshold = (20 * activity.resources.displayMetrics.density).toInt()
        // 44 is the fixed height of the iOS-like navigation bar (Gesture navigation), in pixels!
        return navigationBarsInsets.bottom <= threshold.coerceAtLeast(44) // 20dp or 44px
    }

    fun smoothSoftKeyboardTransition(rootView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ViewCompat.setWindowInsetsAnimationCallback(rootView,
                object : WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

                    private var isImeVisible = false

                    override fun onPrepare(animation: WindowInsetsAnimationCompat) {
                        super.onPrepare(animation)
                        isImeVisible = ViewCompat.getRootWindowInsets(rootView)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
                    }

                    override fun onProgress(
                        insets: WindowInsetsCompat,
                        runningAnimations: MutableList<WindowInsetsAnimationCompat>
                    ): WindowInsetsCompat {
                        val typesInset = insets.getInsets(WindowInsetsCompat.Type.ime())
                        if (!isImeVisible) {
                            // fooView.translationY = fooView.height - typesInset.bottom + ...
                        }
                        return insets
                    }

                    override fun onEnd(animation: WindowInsetsAnimationCompat) {
                        super.onEnd(animation)
                        // fooView.translationY = 0f
                    }
                })
        }
    }
}