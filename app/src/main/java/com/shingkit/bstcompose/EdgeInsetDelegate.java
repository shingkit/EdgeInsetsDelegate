package com.shingkit.bstcompose;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
// 三大金刚 错
// 手势 对
public class EdgeInsetDelegate {
    private Activity activity;

    private boolean everGivenInsetsToDecorView = false;

    public EdgeInsetDelegate(Activity activity) {
        this.activity = activity;
    }

    public void start() {
        // 不让decorview给状态栏导航栏留白
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
        everGivenInsetsToDecorView = false;
        // 防止miui系统上出现白色半透明导航栏
        ViewCompat.setOnApplyWindowInsetsListener(activity.getWindow().getDecorView(), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                Insets navigationInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                boolean isGestureNavigation = isGestureNavigation(navigationInset);
                if (!isGestureNavigation) {
                    // Let decorView draw the translucent navigationBarColor
                    ViewCompat.onApplyWindowInsets(activity.getWindow().getDecorView(), insets);
                    everGivenInsetsToDecorView = true;
                } else if (isGestureNavigation && everGivenInsetsToDecorView) {
                    ViewCompat.onApplyWindowInsets(activity.getWindow().getDecorView(), new WindowInsetsCompat.Builder()
                            .setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.of(navigationInset.left, navigationInset.top, navigationInset.right, 0)).build());
                }
                return insets;
            }
        });
    }

    private boolean isGestureNavigation(Insets navigationInset) {
        int threshold = (int) (20 * activity.getResources().getDisplayMetrics().density);
        return navigationInset.bottom <= Math.max(threshold, 44);
    }

    public static void applyBottomWindowInsetForScrollingView(View root, ViewGroup scrollingView) {
        scrollingView.setClipToPadding(false);
        int paddingTop = scrollingView.getPaddingTop();
        int paddingBottom = scrollingView.getPaddingBottom();
        int paddingLeft = scrollingView.getPaddingLeft();
        int paddingRight = scrollingView.getPaddingRight();
        ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                scrollingView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + insets.getSystemWindowInsetBottom());
                return null;
            }
        });
    }
}
