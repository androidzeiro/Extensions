package net.nuvem.mobile.todolar.extensions

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics

fun Fragment.event(screenName: String, fragmentName: String, eventName: String, bundle: Bundle? = null) {
    FirebaseAnalytics.getInstance(this.requireActivity()).run {
        setCurrentScreen(
            this@event.requireActivity(),
            screenName,
            fragmentName
        )
        logEvent(eventName, bundle)
    }
}

fun Fragment.event(eventName: String, bundle: Bundle? = null) {
    FirebaseAnalytics.getInstance(this.requireActivity()).run {
        logEvent(eventName, bundle)
    }
}

fun Activity.event(screenName: String, fragmentName: String, eventName: String, bundle: Bundle? = null) {
    FirebaseAnalytics.getInstance(this).run {
        setCurrentScreen(
            this@event,
            screenName,
            fragmentName
        )
        logEvent(eventName, bundle)
    }
}

fun Activity.event(eventName: String, bundle: Bundle? = null) {
    FirebaseAnalytics.getInstance(this).run {
        logEvent(eventName, bundle)
    }
}