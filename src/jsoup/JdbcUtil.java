package jsoup;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class JdbcUtil {
	public static void main(String arg[]) throws Exception{
		Connection conn=getTestConnection();
		//String sql = "select * from product where prodtype like '%护理保险%'";
		//String parentid="54dca536dccb4e0295316c30353aeffe";//父类别id
		//String name="护理保险";//子类别
		File file = new File("E:/classify_add.txt");
		String encoding = "UTF-8";
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			// 考虑到编码格式 BufferedReader
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			int i=0;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				i++;
				String [] str=lineTxt.split("\\^");
				process(conn,str[2],str[0],str[1]);
				System.out.println(i);
				System.out.println(String.format("sql:%s parentid:%s name:%s",str[2],str[0],str[1]));
			}
			read.close();
		} else {
			System.out.println("文件不存在");
		}
		conn.close();
	}
	
	public static void process(Connection conn,String sql,String parentid,String name) throws Exception{
		String insertSQl ="insert into insyproductcategory(id,name,parentid,childid) values(?,?,?,?)";
		PreparedStatement pre = conn.prepareStatement(sql);
		PreparedStatement insertPre = conn.prepareStatement(insertSQl);
		ResultSet result = pre.executeQuery();
		while(result.next()){
			insertPre.setString(1,java.util.UUID.randomUUID().toString().replaceAll("-", "") );
			insertPre.setString(2, "");
			insertPre.setString(3, parentid);
			insertPre.setString(4, result.getString("id"));
			insertPre.executeUpdate();
		}
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
	   public static Connection getTestConnection (){
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
			   System.out.println(e);
		   }
		   return conn;
	   }
}
