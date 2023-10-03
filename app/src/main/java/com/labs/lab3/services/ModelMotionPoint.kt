package com.labs.lab3.services



class ModelMotionPoint(private var x0: Double, private var y0:Double) {
    private val g: Double = 9.81
    fun AngleShot(v0:Double, alphaDegree: Double, t:Int): Map<String,Double>{
        val alphaRadian = alphaDegree * Math.PI/180

        var newX: Double = x0;
        var newY: Double = y0;
        for(_t in 0..t){
            newX += v0* Math.cos(alphaRadian) *_t
            newY += v0 * Math.sin(alphaRadian) * _t - (g*_t*_t/2)

        }
        return  mapOf("x" to newX, "y" to newY)

    }
}