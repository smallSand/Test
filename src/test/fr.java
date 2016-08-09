package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import util.FileUtil;

public class fr {

	public static void main(String arg[]) throws Exception {
		//FileUtil.readTXTLine("E:/classify.txt");
		Document doc=Jsoup.connect("http://www.xintai.com/organization/findProducts.do").data("key","").post();
		System.out.println(doc.text());
	}
	

	
}
