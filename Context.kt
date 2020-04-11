package net.nuvem.mobile.todolar.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

fun Context.createVectorCompatDrawable(drawableId: Int, colorId: Int?): Drawable {
    var icon = VectorDrawableCompat.create(this.resources, drawableId, this.theme) as Drawable
    icon = DrawableCompat.wrap(icon)
    if (colorId != null) {
        DrawableCompat.setTint(icon.mutate(), ContextCompat.getColor(this, colorId))
    }
    return icon
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) = Toast.makeText(this, message, duration).show()
