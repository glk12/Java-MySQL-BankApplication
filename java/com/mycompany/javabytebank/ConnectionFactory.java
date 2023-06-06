package com.mycompany.javabytebank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
    
    public Connection recuperarConexao(){
        try{
          
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=1212");       
            
        }catch(SQLException e){
            
           throw new RuntimeException(e);
        }
    }
    
}
