package zbi.trunkstrop.core

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

class BaseActivity(@LayoutRes layout: Int) : AppCompatActivity(layout) {
    constructor(): this(0)
}