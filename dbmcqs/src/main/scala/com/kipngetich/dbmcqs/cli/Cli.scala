package com.kipngetich.dbmcqs.cli

import scala.util.matching.Regex
import scala.io.StdIn
import com.kipngetich.dbmcqs.dao.UserDao
import java.sql.PreparedStatement
import java.io.Console
import com.kipngetich.dbmcqs.dao.MCQDao
import scala.util.control.Breaks._

class Cli{

var active = true
val commandArgPattern: Regex = "(\\w+)\\s*(.*)".r
def openCli():Unit = {
    while(active){
        printWelcome()
        menu()

    }
}
    def printWelcome():Unit = {
        println("Welcome to DB Test")
        println("1. Do you have an account?: y/n")
        println("2. Enter X to exit")
        
    }

   
    var loggedin = false
    def menu():Unit = {
 var canLogin = false
        var user = ""
        val input = StdIn.readLine()
        breakable{
            while(!canLogin){
            input match {
                case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("y")=>{
                    canLogin = true
                }
                case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("n")=>{
                   canLogin =  UserDao.createUser()
                }
                case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("x")=>{
                   active = false
                   println("You have successfully exited the system")
                   break()
                }
                case _=>{
                    println("Invalid choice")
                    break()
                }
            }
        }
 val loggedUser =  UserDao.validateUser()
      if(loggedUser!=""){
          var continueLoopMenu = true
          
          while(continueLoopMenu){
              println("Choose an option\n" +
            "1. Take test\n" +
            "2. View your your highest score\n" +
            "3. Delete you account\n" +
            "4. Change your password\n" +
            "5. Exit")
              val choice = StdIn.readLine()
              var correct_answer = 0
              var incorrect_answer = 0
             


              choice match {
                  case commandArgPattern(cmd,arg) if cmd.equals("1")=>{
                      val mcqs = MCQDao.getMCQs()
                      
                      var n = 1
                      mcqs.foreach(mcq=>{
                          var q = mcq.question
                          var a = mcq.choiceA
                          var b = mcq.choiceB
                          var c = mcq.choiceC
                          var d = mcq.choiceD
                          var answer = mcq.answer
                          println(s"$n. $q")
                          println(s"  a. $a")
                          println(s"  b. $b")
                          println(s"  c. $c")
                          println(s"  d. $d")
                          val selection = StdIn.readLine()
                          if(answer == selection.toCharArray()(0)) correct_answer+=1 else incorrect_answer+=1
                          println()
                          n+=1
                      })
                      val total = correct_answer+incorrect_answer
                      println(s"You scored $correct_answer/$total")
                      val id = UserDao.getUserId(loggedUser)
                      MCQDao.saveScore(correct_answer,incorrect_answer,id)
                  }
                  case commandArgPattern(cmd,arg) if cmd.equals("2")=>{
                      val user_id = UserDao.getUserId(loggedUser)
                      val max = MCQDao.getMaxSCore(user_id)
                      println(s"Your max score is $max / 10")
                  }
                  case commandArgPattern(cmd,arg) if cmd.equals("3")=>{
                      println("Confirm account deletion: y/N")
                      val input = StdIn.readLine()
                      input match {
                          case commandArgPattern(cmd,arg) if cmd.equalsIgnoreCase("y")=>{
                               UserDao.deleteUser(loggedUser)
                               active = false
                               //break()
                               canLogin = false
                               println(s"$loggedUser has been deleted")
                               break()
                               }
                               case _=>{
                                   println("You account is safe")
                               }
                      }
                      
                  }
                  case commandArgPattern(cmd,arg) if cmd.equals("4")=>{
                      val  console:Console = System.console()
                      val password = console.readPassword("password: ").mkString;
                      val verifypassword = console.readPassword("verify password: ").mkString;
                      if(password.equals(verifypassword)){
                          UserDao.changePassword(loggedUser,password)
                      }else{
                          println("password does not match")
                      }
                      
                  }
                  case commandArgPattern(cmd,arg) if cmd.equals("5")=>{
                     continueLoopMenu = false
                  }
                  case _=>{
                      println("Invalid choice")
                  }
              }
              
          }
      }else{
           println(s"Invalid credentials")
      }

    }

        }
        
     

    // def createUser():Unit = {
    //     println("Please create an account")
    //     print("username: ")
    //     val username = StdIn.readLine()
    //     val  console:Console = System.console()
    //     print("password: ")
    //     val password = console.readPassword("password: ").mkString
    //     print("verifypassword: ")
    //     val verifypassword = console.readPassword("password: ").mkString
    //     if(password.equals(verifypassword)) {
    //         UserDao.saveUser(username,password)
    //         canLogin = true
    //     }

    // }

    
}

