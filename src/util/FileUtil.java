package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
}
