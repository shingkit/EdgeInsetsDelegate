package com.shingkit.bstcompose

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.shingkit.bstcompose.ui.EdgeInsetDelegate2

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        com.shingkit.bstcompose.ui.EdgeInsetDelegate(this).start()
//        EdgeInsetDelegate(this).start()
        EdgeInsetDelegate2(this).start()
        setContentView(R.layout.main)
//        findViewById<TextView>(R.id.bottom).doOnApplyWindowInsets { windowInsets, padding, margin ->
//            findViewById<TextView>(R.id.bottom).updatePadding(bottom = padding.bottom + windowInsets.systemWindowInsetBottom)
//        }
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<TextView>(R.id.bottom)
        ) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }
}
