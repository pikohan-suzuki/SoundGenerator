package com.amebaownd.pikohan_nwiatori.soundgenerator.metronome

import java.lang.IllegalArgumentException

class RhythmGenerator (private val samplingRate:Int,private val bufferSize:Int){

    private var tempo :Int = -1
    private var rhythm = 0
    private var t: Double = 0.toDouble()
    private var soundTime = samplingRate * 200 /1000
    fun getNextRhythm():Array<Double>{
        val buffer = Array<Double>(bufferSize){i: Int -> 0.toDouble() }
        val tempoInSamplingRate = samplingRate * 60 / tempo
        if(tempo != -1){
            for(i in 0 until bufferSize){
                if(t*samplingRate % (tempoInSamplingRate * rhythm) <soundTime) {
                    buffer[i] = Math.sin(2*Math.PI * t * 1960)
                }else if(t*samplingRate % tempoInSamplingRate < soundTime){
                    buffer[i] = Math.sin(2*Math.PI * t *880)
                }else{
                    buffer[i] =0.toDouble()
                }
                t += 1f/samplingRate
            }
        }else{
            throw IllegalArgumentException("variable tempo has not initialized.")
        }
        return buffer
    }


    fun setTempo(tempo:Int){
        this.tempo=tempo
    }

    fun setRhythm(rhythm:Int){
        this.rhythm = rhythm
    }

    fun reset(){
        t=0.toDouble()
    }
}