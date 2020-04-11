package br.com.redetodolar.todolar.extensions

import android.app.Activity
import br.com.redetodolar.todolar.App

val Activity.app: App
    get() = application as App