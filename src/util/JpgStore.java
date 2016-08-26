package util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class JpgStore {

	public static void main(String[] args) throws Exception {
		Connection con=getConection();
		PreparedStatement insertPre=con.prepareStatement("insert into YM_image(guide,title,image) values(?,?,?)");
		File file = new File("F:/优美图");
		File[] files = file.listFiles();
		int c=0;
		for(File guides:files){
			String guid=guides.getName();
			File titles=new File(guides.getAbsolutePath());
			for(File title:titles.listFiles()){
				String t=title.getName();
				File image=new File(title.getAbsolutePath());
				for(File i:image.listFiles()){
					c++;
					insertPre.setString(1, guid);
					insertPre.setString(2, t);
					insertPre.setBlob(3,  new FileInputStream(i));
					insertPre.executeUpdate();
					System.out.println(c);
				}
			}
		}
		con.close();

	}

	public static Connection getConection() {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/test?"
				+ "user=root&password=&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static byte[] File2byte(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}