package db;

import java.sql.Connection;

public class ConnectionFactory {
	
	public static  Connection getConnection(String dbname){
		if(dbname.equals("insy_test")){
			return new  TestConnection().getConnection();
		}
		if(dbname.equals("lsl_test")){
			return new  Myconnetion().getConnection();
		}
		return null;
	}
}
