package dev.giuseppedarro.comanda.core.utils

/**
 * Converts cents (Int) to formatted currency string (String)
 * Example: 350 → "€3,50"
 */
fun Int.toPriceString(): String {
    val euros = this / 100
    val cents = this % 100
    return "€$euros,${cents.toString().padStart(2, '0')}"
}

/**
 * Converts cents (Int) to USD currency string with dollar sign (String)
 * Example: 350 → "$3.50"
 */
fun Int.toPriceStringUSD(): String {
    val euros = this / 100
    val cents = this % 100
    return "\$${euros}.${cents.toString().padStart(2, '0')}"
}

/**
 * Converts a currency price string to cents (Int)
 * Example: "3.50" or "3,50" → 350
 */
fun String.toPriceCents(): Int {
    val cleanPrice = this.replace(",", ".").toDoubleOrNull() ?: 0.0
    return (cleanPrice * 100).toInt()
}

/**
 * Converts cents (Int) to double for calculations
 * Example: 350 → 3.50
 */
fun Int.toPriceDouble(): Double {
    return this / 100.0
}
