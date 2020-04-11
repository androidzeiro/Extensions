package br.com.redetodolar.todolar.extensions

import java.text.NumberFormat
import java.util.*

val Double.toCurrency: String
    get() = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)