package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection implements DBconnection {
	@Override
	public Connection getConnection() {
		   Connection conn=null;
		   try
		   {
			   Class.forName("org.postgresql.Driver").newInstance();
			   String url="jdbc:postgresql://192.168.7.212:54320/insy_test";
			   String user="postgres";
			   String pwd="longrise";
			   conn=DriverManager.getConnection(url,user,pwd);
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		   return conn;
	}

}
