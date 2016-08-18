package jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtil {

	private static List<String> ids = new ArrayList<String>();

	public static void main(String arg[]) throws Exception {
		Connection conn = getConnection();
		// String sql = "select * from product where prodtype like '%护理保险%'";
		// String parentid="54dca536dccb4e0295316c30353aeffe";//父类别id
		// String name="护理保险";//子类别
		File file = new File("E:/classify.txt");
		String encoding = "GBK";
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			// 考虑到编码格式 BufferedReader
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			int i = 0;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				i++;
				String[] str = lineTxt.split("\\^");
				process(conn, str[2], str[0], str[1]);
				System.out.println(i);
				System.out.println(String.format("sql:%s parentid:%s name:%s", str[2], str[0], str[1]));
			}
			read.close();

			// 更新产品状态2->1
			String updateSQL = "update product set isclassifi ='1',updatetime=now() where isclassifi ='2' and id=?";
			PreparedStatement updatePre = conn.prepareStatement(updateSQL);
			for (String id : ids) {
				updatePre.setString(1, id);
				updatePre.executeUpdate();
			}

		} else {
			System.out.println("文件不存在");
		}
		conn.close();
	}

	public static void process(Connection conn, String sql, String parentid, String name) throws Exception {
		String insertSQl = "insert into insyproductcategory(id,name,parentid,childid,createtime) values(?,?,?,?,now())";
		PreparedStatement pre = conn.prepareStatement(sql);
		PreparedStatement insertPre = conn.prepareStatement(insertSQl);
		ResultSet result = pre.executeQuery();
		while (result.next()) {
			insertPre.setString(1, java.util.UUID.randomUUID().toString().replaceAll("-", ""));
			insertPre.setString(2, name);
			insertPre.setString(3, parentid);
			insertPre.setString(4, result.getString("id"));
			if (!ids.contains(result.getString("id")))
				ids.add(result.getString("id"));
			insertPre.executeUpdate();
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			String url = "jdbc:postgresql://192.168.7.212:54320/insy_dev0801";
			String user = "postgres";
			String pwd = "longrise";
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	public static Connection getTestConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			String url = "jdbc:postgresql://192.168.7.212:54320/insy_test";
			String user = "postgres";
			String pwd = "longrise";
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			System.out.println(e);
		}
		return conn;
	}

	public static  List<String> getUrls(Connection conn){
		   List<String> list=new ArrayList<String>();
		   try {
			PreparedStatement pre=conn.prepareStatement("select url from tb_product_new");
			ResultSet rs=pre.executeQuery();
			while(rs.next()){
				String url=rs.getString("url");
				list.add(url);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	   }
}
