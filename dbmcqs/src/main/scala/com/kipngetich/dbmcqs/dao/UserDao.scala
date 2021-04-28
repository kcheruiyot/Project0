package com.kipngetich.dbmcqs.dao

import java.sql.PreparedStatement
import com.kipngetich.dbmcqs.utils.ConnectionUtil
import org.postgresql.util.PSQLException
import scala.io.StdIn
import java.io.Console

object UserDao{
    def saveUser(username:String, password:String):Unit={
        var stmt:PreparedStatement= null
        try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"insert into users (username, password) values('$username','$password')")
            stmt.executeUpdate()
        }
        catch {
             case e:PSQLException =>{
                 println("Entry already exists")
             }
       }
        finally{
 stmt.close()
        }
    }

    def validateUser():String = {
        var user = ""
         println("Please login to your account:")
         val  console:Console = System.console()
        print("username: ")
         val username = StdIn.readLine()
       // print("password: ")
        val password = console.readPassword("password: "); 
        //println(password.mkString)
        // val password = StdIn.readLine()
        var stmt:PreparedStatement= null
                try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"select * from users where username=? and password=?;")
   stmt.setString(1,username)
   stmt.setString(2,password.mkString)
   stmt.execute()
   val rs = stmt.getResultSet()
   val z = rs.next()
   val returnedUsername = rs.getString("username")
     val returnedPassword = rs.getString("password")
      if(username.equals(returnedUsername)){
          user = username
      }
        }
        catch {
             case e:PSQLException =>{
               // println("Entry already exists")
            }
      }
       finally{
 stmt.close()
       }
       user
    }


    def createUser():Boolean = {
        var canLogin = false
        println("Please create an account")
        print("username: ")
        val username = StdIn.readLine()
        var stmt:PreparedStatement= null
                try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"select * from users where username=?;")
            stmt.setString(1,s"$username")
            stmt.execute()
             val rs = stmt.getResultSet()
             
             if(rs.next()){
         println("user already exist")

             }
             else{
        val  console:Console = System.console()
        
        val password = console.readPassword("password: ").mkString
        val verifypassword = console.readPassword("verify password: ").mkString
        if(password.equals(verifypassword)) {
            saveUser(username,password)
            canLogin =true
            
        }
        else{
           println("password missmatch")
        }
             }
            
                }
                catch{
                    case e:PSQLException =>{
              println(s"$e") 
            }
                }
                finally{
                    stmt.close()
                }
        
canLogin
    }


    def getUserId(username:String):Int = {
        var stmt:PreparedStatement= null
        var id = 0
        try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"select * from users where username=?;")
            stmt.setString(1,s"$username")
            stmt.execute()
            val rs = stmt.getResultSet()
            val x = rs.next()
            id = rs.getInt("user_id")
                 
        }catch{
                   case e:PSQLException =>{
               println(s"$e")
            }
        }finally{
            stmt.close()
        }
        id
    }

    def deleteUser(username:String):Unit = {
        var stmt:PreparedStatement= null

        try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"delete from users where username = '$username';")
            stmt.execute()     
        }catch{
                   case e:PSQLException =>{
               println(s"$e")
            }
        }finally{
            stmt.close()
        }
    }

    def changePassword(username:String, password:String):Unit = {
        var stmt:PreparedStatement= null

        try{
            stmt = ConnectionUtil.getConnection.prepareStatement(s"update users set password ='$password' where username = '$username';")
            stmt.executeUpdate()   
        }catch{
                   case e:PSQLException =>{
               println(s"$e")
            }
        }finally{
            stmt.close()
        }
    }
}