package com.labs.lab3.services



class ModelMotionPoint(private var x0: Double, private var y0:Double) {
    private val g: Double = 9.81
    fun AngleShot(v0:Double, alphaDegree: Double): Map<String,Double>{
        val alphaRadian = alphaDegree * Math.PI/180

        var newX: Double = x0;
        var newY: Double = y0;
        var _t: Int = 0
        while(newY >= 0.0){
            newX += v0* Math.cos(alphaRadian) *_t
            newY += v0 * Math.sin(alphaRadian) * _t - (g*_t*_t/2)
            _t += 1

        }
        return  mapOf("x" to Math.round(newX*100)/100.0, "y" to Math.round(newY*100)/100.0, "t" to _t*1.0)

    }

    fun freeFall(h0: Double,):Double{
        var y:Double = h0
        var t: Double = 0.0
        while(y >= 0){
            y -= (g * Math.pow(t,2.0))/2
            t+= 1
        }
        return t
    }
}