package com.amebaownd.pikohan_nwiatori.soundgenerator.sound

class SoundGenerator(private val bufferSize: Int, private val sampleRate: Int) {

    private var t = 0.toDouble()
    fun getNext(hz: Int): Array<Double> {
        val buffer = Array<Double>(bufferSize) { i: Int -> 0.toDouble() }
        for (i in 0 until bufferSize) {
            val sin = Math.sin(2 * Math.PI * t * hz)
            buffer[i] = sin
            t += 1f / sampleRate
        }
        return buffer
    }
}