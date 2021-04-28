package com.kipngetich.dbmcqs.utils

import scala.collection.mutable.ArrayBuffer
import com.kipngetich.dbmcqs.model.MCQ

object ReadFileUtil{
    def readJson():ArrayBuffer[MCQ] = {
        val mcqs = ArrayBuffer[MCQ]()
        val jsonString = os.read(os.pwd/"src"/"main"/"scala"/"resources"/"dbmcqs.json")
        val data = ujson.read(jsonString)
        var n = 1
        data("dbmcqs").arr.foreach(q=>{
           val question = q("question").toString().split("<br>").mkString("\n\t")
        //    println(s"$n $question")
        //    println(" a) " + q("choices")(0))
        //    println(" b) " + q("choices")(1))
        //    println(" c) " + q("choices")(2))
        //    println(" d) " + q("choices")(3))
        //    println()
        //    n+=1
           val temp = MCQ(question,q("choices")(0).toString,q("choices")(1).toString,q("choices")(2).toString,q("choices")(3).toString,q("answer").toString().toCharArray()(1))
           mcqs += temp
       })
mcqs
    }
}