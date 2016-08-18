package test;
import org.junit.Test;
public class fr {

	public static void main(String arg[]) throws Exception {
		//FileUtil.readTXTLine("E:/classify.txt");
	}
	

	@Test
	public void getUUID(){
		for(int i=0;i<252;i++)
		System.out.println(java.util.UUID.randomUUID().toString().replace("-", ""));
	}
}
