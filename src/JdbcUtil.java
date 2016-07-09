import java.sql.Connection;
import java.sql.DriverManager;
public class JdbcUtil {
	
	public static void main(String arg[]) throws Exception{
		Connection conn=getConnection();
		conn.close();
	}
	   public static Connection getConnection (){
		   Connection conn=null;
		   try
	        {
	        Class.forName("org.postgresql.Driver").newInstance();
	        String url="jdbc:postgresql://192.168.7.212:54320/lsl_test";
	        String user="postgres";
	        String pwd="longrise";
	        conn=DriverManager.getConnection(url,user,pwd);
	        }
	         catch(Exception e)
	        {
	        System.out.println(e);
	        }
		return conn;
	}
}
