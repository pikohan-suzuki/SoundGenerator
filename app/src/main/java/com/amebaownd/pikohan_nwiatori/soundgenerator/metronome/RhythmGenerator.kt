package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome


import java.lang.IllegalArgumentException

class RhythmGenerator(private val samplingRate: Int, private val bufferSize: Int) {

    private var tempo: Int = -1
    private var rhythm = 0
    private var t: Double = 0.toDouble()
    private var tInBeep = 0.toDouble()
    private var soundTime = samplingRate * 200 / 1000
    fun getNextRhythm(): Pair<Array<Double>, Map<Int, Int>> {
        val buffer = Array<Double>(bufferSize) { i: Int -> 0.toDouble() }
        val rhythmStartMap = mutableMapOf<Int, Int>()
        val tempoInSamplingRate = samplingRate * 60 / tempo
        if (tempo != -1) {
            for (i in 0 until bufferSize) {
                if (t * samplingRate % (tempoInSamplingRate * rhythm) < soundTime) {
                    buffer[i] = Math.sin(2 * Math.PI * tInBeep * 1960)
                    if (!rhythmStartMap.containsKey(0)) {
                        rhythmStartMap[0] = i
                    }
                } else if (t * samplingRate % tempoInSamplingRate < soundTime) {
                    buffer[i] = Math.sin(2 * Math.PI * tInBeep * 880)
                    for (j in 1 until rhythm) {
                        if ((t * samplingRate - tempoInSamplingRate * j) % (tempoInSamplingRate * rhythm) in 0 until soundTime) {
                            if (!rhythmStartMap.containsKey(j)) {
                                rhythmStartMap[j] = i
                            }
                        }
                    }
                } else {
                    buffer[i] = 0.toDouble()
                    tInBeep = 0.toDouble()
                }
                t += 1f / samplingRate
                tInBeep += 1f / samplingRate
            }
        } else {
            throw IllegalArgumentException("variable tempo has not initialized.")
        }
        return Pair(buffer, rhythmStartMap)
    }

    fun setTempo(tempo: Int) {
        this.tempo = tempo
    }

    fun setRhythm(rhythm: Int) {
        this.rhythm = rhythm
    }
}