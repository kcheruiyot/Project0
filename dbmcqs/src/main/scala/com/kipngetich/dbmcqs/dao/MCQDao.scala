package com.kipngetich.dbmcqs.dao

import scala.collection.mutable.ArrayBuffer
import com.kipngetich.dbmcqs.model.MCQ
import com.kipngetich.dbmcqs.utils.ReadFileUtil
import com.kipngetich.dbmcqs.utils.ConnectionUtil
import java.sql.Connection
import java.sql.PreparedStatement
import org.postgresql.util.PSQLException
import scala.util.control.Breaks._

object MCQDao {
  //  def getQuestions():ArrayBuffer = {
//
  //  }

  def readWriteJson():Unit = {
      val mcqs:ArrayBuffer[MCQ] = ReadFileUtil.readJson()
      mcqs.foreach(mcq=>{
          val column = "question, choicea, choiceb, choicec, choiced, answer"
          val q = mcq.question
          val a = mcq.choiceA;
          val b = mcq.choiceB;
          val c = mcq.choiceC;
          val d = mcq.choiceD;
          val answer = mcq.answer
          val value = s"'$q','$a', '$b', '$c', '$d','$answer'"
         // println(answer)
         var stmt:PreparedStatement= null
         try {
        stmt = ConnectionUtil.getConnection.prepareStatement(s"insert into mcqs ($column) values($value)")
          stmt.executeUpdate()
         

         }
         catch{
             case e:PSQLException =>{
// println("Entry already exists")
             }
            
         }
         finally {
 stmt.close()
         }
      })
      
  }


def getMCQs():ArrayBuffer[MCQ] = {
  val mcqs = ArrayBuffer[MCQ]()
  var stmt:PreparedStatement= null
  try{
    stmt = ConnectionUtil.getConnection.prepareStatement(s"select * from  mcqs;")
          stmt.execute()
          val rs = stmt.getResultSet()
          while(rs.next) {
            val question = rs.getString("question")
            val choicea = rs.getString("choicea")
            val choiceb = rs.getString("choiceb")
            val choicec = rs.getString("choicec")
            val choiced = rs.getString("choiced")
            val answer= rs.getString("answer").toCharArray()(0)
            val temp = new MCQ(question,choicea,choiceb,choicec,choiced,answer);
            mcqs += temp
    }
        

  }catch{
case e:PSQLException =>{
 println("Try again")
             }
  }finally{
     stmt.close()
  }
  mcqs
}
def saveScore(correct_answer:Int, incorrect_answer:Int,fk:Int):Unit={
   var stmt:PreparedStatement= null
   try{

     stmt = ConnectionUtil.getConnection.prepareStatement(s"insert into scores (correct_answers,incorrect_answers,user_id) values($correct_answer, $incorrect_answer,$fk);")
     stmt.executeUpdate()
   }
   catch{
case e:PSQLException =>{
 println(s"$e")
             }
   }
   finally{
     stmt.close
   }

}


def getMaxSCore(user_id:Int):Int = {
  var max = 0
  var stmt:PreparedStatement= null
   try{

     stmt = ConnectionUtil.getConnection.prepareStatement(s"select max(correct_answers) from scores where user_id=?;")
     stmt.setInt(1,user_id)
     stmt.execute()
      val rs = stmt.getResultSet()
      if(rs.next()) max=rs.getInt(1)
   }
   catch{
case e:PSQLException =>{
 println(s"$e")
             }
   }
   finally{
     stmt.close
   }
   max

}

}

