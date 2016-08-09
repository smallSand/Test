package util;
import java.util.Arrays;

public class ReName {

	public static void main(String[] args) {
/*		File path =new File("E:/人身险_1");
		File[] files=path.listFiles();
		for(File f:files){
			if(f.getName().endsWith(".htm")){
				f.renameTo(new File(f.getPath()+"l"));
			}
		}*/
		int[] arr=new int[]{1,2,9,3,2,7,6};
		Arrays.sort(arr);
		for(int i:arr)System.out.println(i);
		System.out.println(java.util.UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
