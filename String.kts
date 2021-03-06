package br.com.raphael.everis.extensions

import android.util.Patterns
import java.lang.Exception
import java.util.regex.Pattern
import kotlin.String

fun String?.isValidCPF(): Boolean {
    val pesoCPF = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
    fun calcularDigito(str: String, peso: IntArray): Int {
        var deslocamentoPeso = 0
        var indice = str.length - 1
        var digito: Int
        while (indice >= 0) {
            digito = Integer.parseInt(str.substring(indice, indice + 1))
            deslocamentoPeso += digito * peso[peso.size - str.length + indice]
            indice--
        }
        deslocamentoPeso = 11 - deslocamentoPeso % 11
        return if (deslocamentoPeso > 9) 0 else deslocamentoPeso
    }
    if (this == null || this.length != 11) return false
    val digito1 = calcularDigito(this.substring(0, 9), pesoCPF)
    val digito2 = calcularDigito(this.substring(0, 9) + digito1, pesoCPF)
    return this == this.substring(0, 9) + digito1.toString() + digito2.toString()
}

fun String?.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this ?: "").matches()

fun String?.isNumeric(): Boolean {
    if (this == null) return false

    return try {
        this.replace(".", "").replace("-","").toDouble()
        true
    } catch (e: Exception) {
        false
    }
}

fun String?.isValidPassword(): Boolean {
    if (this == null) return false

    val str = this
    var valid = true

    // A senha deve conter pelo menos um número
    var exp = ".*[0-9].*"
    var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
    var matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    // A senha deve conter pelo menos uma letra maiúscula
    exp = ".*[A-Z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    //A senha deve conter pelo menos uma letra minúscula
    exp = ".*[a-z].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    // A senha deve conter pelo menos um caractere especial
    // Caracteres especiais permitidos : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
    exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
    pattern = Pattern.compile(exp)
    matcher = pattern.matcher(str)
    if (!matcher.matches()) {
        valid = false
    }

    return valid
}