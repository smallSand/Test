package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

	public static String[] readTXTLine(String path) throws Exception {
		File file = new File(path);
		String encoding = "GBK";
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
			// 考虑到编码格式 BufferedReader
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String [] str=lineTxt.split("\\^");
				System.out.println(str[0]+str[1]+str[2]);
			}
			read.close();
		} else {
			System.out.println("文件不存在");
		}
		return null;
	}
	
	public static File[] listFiles(String path) throws Exception{
		File file=new File(path);
		File[] result=null;
		if(file.isDirectory()){
			result=file.listFiles();
				for(File f:result){
					if(f.isDirectory()){
						System.out.println(f.getPath());
						listFiles(f.getPath());
					}else{
						if(f.getName().matches("^leaperror.log.*")){
							System.out.println(f.getName()); 
						}
					}
				}
		}
		return result;
	}
	
	public static byte[] URLtobyteArray(String url) throws Exception{
		byte[] arr=null;
		URL link = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) link.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36");
		// 尝试连接30s
		conn.setConnectTimeout(1000 * 3 * 60);
		conn.setReadTimeout(1000 * 3 * 60);
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		 arr = readInputStream(inputStream);
		return arr;
	}
	
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
}
