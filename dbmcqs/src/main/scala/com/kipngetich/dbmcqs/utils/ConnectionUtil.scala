package com.kipngetich.dbmcqs.utils

import java.sql.Connection
import java.sql.DriverManager

object ConnectionUtil{
    var conn: Connection = null;
    def getConnection:Connection = {


    if (conn == null || conn.isClosed()) {
        classOf[org.postgresql.Driver].newInstance()
      //missing a bit of documentation from the java code:
      // getConnection takes a url, username, password
      // hardcoding these at the moment, though this is v bad practice
      conn =  DriverManager.getConnection(
      "jdbc:postgresql://localhost:5432/dbmcqs",
      "postgres",
      "322192"
    )
    }

        conn
    }
}