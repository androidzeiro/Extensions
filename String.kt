package net.nuvem.mobile.todolar.extensions

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.Normalizer
import java.util.regex.Pattern

fun String.removeMasks() =
    this.replace(".", "")
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
        .replace(" ", "")
        .replace("/", "")

fun String.stripAccents(): String {
    if (this.isEmpty()) return this
    val decomposed = StringBuilder(Normalizer.normalize(this, Normalizer.Form.NFD))
    decomposed.forEachIndexed { i, c ->
        when (c) {
            '\u0141' -> {
                decomposed.deleteCharAt(i)
                decomposed.insert(i, 'L')
            }
            '\u0142' -> {
                decomposed.deleteCharAt(i)
                decomposed.insert(i, 'l')
            }
        }
    }
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(decomposed).replaceAll("")
}

fun String.to0800(): String? {
    val regex = Regex("(0800)([0-9]{3})([0-9]{4})")
    return if (regex.matches(this)) {
        val match = regex.matchEntire(this)
        match?.let { "${it.groupValues[1]}-${it.groupValues[2]}-${it.groupValues[3]}" }
    } else {
        null
    }
}

fun String.toPhoneNumber(): String? {
    val regex = Regex("0?([1-9][0-9])(9?)([0-9]{4})([0-9]{4})")
    return if (regex.matches(this)) {
        val match = regex.matchEntire(this)
        match?.let { "(${it.groupValues[1]}) ${it.groupValues[2]}${it.groupValues[3]}-${it.groupValues[4]}" }
    } else {
        null
    }
}

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

fun String?.isValidCNPJ(): Boolean {
    if(this == null) return false

    if(this.length != 14){
       return false
    }

    // Verifica sequência de números repetidos
    if(!(0..9).map { it.toString().repeat(14) }
              .map { this == it }
              .all { !it }){
        return false
    }

    return validarDigitoVerificadorCNPJ(true, this)
            && validarDigitoVerificadorCNPJ(false, this)
}

/**
 *  Verificar dígito do CNPJ.
 *
 * @param[primeiroDigito] True para verificar o primeiro dígito. False para verificar o segundo dígito.
 *
 * @return True se for válido.
 */
private fun validarDigitoVerificadorCNPJ(primeiroDigito: Boolean, cnpj: String): Boolean {
    val posicaoInicial = when (primeiroDigito) {
        true -> 11
        else -> 12
    }
    val peso = when (primeiroDigito) {
        true -> 0
        false -> 1
    }
    val deslocamentoPeso = (posicaoInicial downTo 0).fold(0) { acc, pos ->
        val peso = 2 + ((11 + peso - pos) % 8)
        val num = cnpj[pos].toString().toInt()
        val deslocamentoPeso = acc + (num * peso)
        deslocamentoPeso
    }
    val digitoEsperado = when (val result = deslocamentoPeso % 11) {
        0, 1 -> 0
        else -> 11 - result
    }

    val digitoAtual = cnpj[posicaoInicial + 1].toString().toInt()

    return digitoEsperado == digitoAtual
}

fun String.toFormData(name: String): MultipartBody.Part {
    val body = RequestBody.create(MediaType.parse("plain/text"), this)
    return MultipartBody.Part.createFormData(name, null, body)
}
