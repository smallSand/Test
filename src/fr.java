import java.io.File;

public class fr {

	public static void main(String arg[]) throws Exception {

		listFiles("C:/Users/Administrator/Desktop/2016-06-22");
	}
	
	private static File[] listFiles(String path) throws Exception{
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
