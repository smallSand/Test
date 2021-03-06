package jsoup;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupTest implements Runnable {
	private String conName;
	private String coneName;
	private String no;
	private String pageNo;

	public JsoupTest(String pageNo) {
		this.pageNo = pageNo;
	}

	private static int ecount = 0;

	public static void main(String arg[]) throws Exception {
		

		/*
		 * for(int i=0;i<158;i++){ Runnable jsoupTest=new
		 * JsoupTest(String.valueOf(i)); Thread thread = new Thread(jsoupTest);
		 * thread.start(); }
		 */

		
		  Connection conn = JdbcUtil.getConnection(); 
		  String sql ="select * from tb_product_new where name like '%东吴%'"; 
		  PreparedStatement pre =conn.prepareStatement(sql); 
		  ResultSet result = pre.executeQuery();
		  while(result.next())
		  { 
			  String id = result.getString("id").trim();
			  String url = result.getString("url").trim();
			  String com = result.getString("com").trim(); 
			  String name = result.getString("name").trim(); 
			  try{
				  downLoadFromUrl(url, name, "E:/new/"+com);
			  }
			  catch(Exception e){
				  System.out.println("-------------erro:id-------------------"+id);
				  e.printStackTrace();
			  }
			  System.out.println(result.getRow());
		  }
		 	
		
		
		/*String url = "http://www.anbang-life.com/gkxxpl/jbxx/zscp/index.htm";
		Document doc = Jsoup.connect(url).get();
		// System.out.println(doc);
		Elements Elements = doc.getElementsByTag("tr");
		String filename=null;
		Connection conn=JdbcUtil.getConnection();
		PreparedStatement pre = conn.prepareStatement("insert into tb_ab(id,name,isale,pdfurl)values(?,?,?,?)");
		for (Element tr : Elements) {
			Elements tds = tr.getElementsByTag("td");
			for (int i = 0; i < tds.size(); i++) {
					pre.setString(i+1, tds.get(i).text());
					if(i==1){
						filename=tds.get(i).text();
					}
					if (tds.get(i).text().equals("查看")) {
						pre.setString(i+1, "http://www.anbang-life.com"+tds.get(i).getElementsByTag("a").attr("href").replace("../../..", ""));
						try{
							downLoadFromUrl("http://www.anbang-life.com"+tds.get(i).getElementsByTag("a").attr("href").replace("../../..", ""),filename,"e:/ab");
						}catch (Exception e){
							System.out.println("下载失败"+"http://www.anbang-life.com"+tds.get(i).getElementsByTag("a").attr("href").replace("../../..", ""));
						}
					}
			}
			pre.executeUpdate();
		}
		conn.close();*/
		/*
		 * StringBuilder erro = new StringBuilder(); StringBuilder info = new
		 * StringBuilder(); int i = 1; while (result.next()) {
		 * System.out.println(i); String url =
		 * result.getString("produrl").trim() + "F"; String prodname =
		 * result.getString("prodname").trim(); String company =
		 * result.getString("company").trim(); String issale =
		 * result.getString("issale").trim(); try { if (issale.equals("在售")) {
		 * downLoadFromUrl(url, prodname, "e:/prod/" + company + "/在售/");
		 * info.append(url); } if (issale.equals("停售")) { downLoadFromUrl(url,
		 * prodname, "e:/prod/" + company + "/停售/"); info.append(url); } if
		 * (issale.equals("停用")) { downLoadFromUrl(url, prodname, "e:/prod/" +
		 * company + "/停用/"); info.append(url); } } catch (Exception e) {
		 * erro.append(url); } i++; }
		 * 
		 * File errLog = new File("e:/prod/erro.txt"); File infoLog = new
		 * File("e:/prod/info.txt"); FileWriter fwe = new FileWriter(errLog);
		 * FileWriter fwi = new FileWriter(infoLog); if(erro.length()>1){
		 * fwe.write(erro.toString()); fwe.flush(); } if(info.length()>1){
		 * fwi.write(info.toString()); fwi.flush(); } if(fwe!=null) fwe.close();
		 * if(fwi!=null) fwi.close();
		 */

		// File path=new File("e:/prod/中法人寿保险有限责任公司/在售/");

		// process("中国人寿保险股份有限公司", "中国人寿","337517");
		/*
		 * process("新光海航人寿保险有限责任公司", "新光海航人寿"); process("中意人寿保险有限公司", "中意人寿");
		 * process("中银三星人寿保险有限公司", "中银三星人寿"); process("中英人寿保险有限公司", "中英人寿");
		 * process("中邮人寿保险股份有限公司", "中邮保险"); process("珠江人寿保险股份有限公司", "珠江人寿");
		 */
		
		
	}

	public static void porcessAll(String pageNo) throws Exception {
		List<Prod> list = new ArrayList<Prod>();
		// String conName="华汇人寿保险股份有限公司"; //公司全称
		// String coneName="华汇人寿"; //公司简称
		StringBuilder sb = new StringBuilder();

		sb.append("pageNo=").append(pageNo).append("&pageCount=157").append("&prodTermsShow.prodName=")
				// .append(URLEncoder.encode(proName,"gb2312"))
				.append("&prodTermsShow.insComName=").append("&prodTermsShow.insItemCode=")
				.append("&prodTermsShow.saleStatus=").append("&prodTermsShow.specialAttri=")
				.append("&prodTermsShow.insType=").append("&prodTermsShow.insPerdType=")
				// .append("&prodTypeCodeOne=ProdTypeCode_00")
				// .append("&prodTypeCodeWwo=ProdTypeCode_02")
				// .append("&prodTermsShow.prodTypeCode=ProdTypeCode_00_02")
				.append("&prodTermsShow.prodDesiCode=").append("&pageSize=100").append("&goToPage=");
		String param = sb.toString();
		String html = sendPost("http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQueryResult.do", param);

		File f = new File("e:/prod/" + pageNo + ".html");
		FileWriter fw = new FileWriter(f);
		fw.write(html);
		fw.flush();
		if (fw != null)
			fw.close();

		Document doc = Jsoup.parse(html);
		Elements common = doc.getElementsByClass("common1");
		for (int i = 0; i < common.size(); i++) {
			Prod prod = new Prod();
			try {
				System.out.println("-" + i + "-----------------------begin--------------------------");
				String onclick = doc.getElementById("detailed" + (i + 1)).attr("onclick");
				// 产品的id
				String uuid = onclick.substring(10, onclick.length() - 2).replace("'", "").split(",")[0];
				// get请求详细信息
				Document docdetail = Jsoup.connect("http://www.iachina.cn/IC/tkk/02/" + uuid + ".html").timeout(30000)
						.get();
				Elements tr = docdetail.getElementsByClass("biaoge").first().getElementsByTag("tr");
				// 产品的详情信息
				for (int j = 0; j < tr.size(); j++) {
					if (j == 0) {
						prod.setCompany(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 1) {
						prod.setProdname(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 2) {
						prod.setProdtype(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 3) {
						prod.setDesigntype(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 4) {
						prod.setSpecialattr(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 5) {
						prod.setApproveway(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 6) {
						prod.setProtectetime(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 7) {
						prod.setPayway(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 8) {
						prod.setItemcode(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 9) {
						prod.setIssale(tr.get(j).getElementsByTag("td").get(1).text());
						;
					} else if (j == 10) {
						prod.setOutsaletime(tr.get(j).getElementsByTag("td").get(1).text());
					}
					System.out.print(tr.get(j).getElementsByTag("td").get(0).text());
					System.out.println(tr.get(j).getElementsByTag("td").get(1).text());
				}
				// 获取产品PDF
				// http://www.iachina.cn/IC/tkk/03/ecd6e585-df4a-401f-ae36-7aac4bdb772f_TERMS.PDF
				System.out.println("产品条款URL:http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF");
				System.out.println("-" + i + "-----------------------end--------------------------");
				prod.setProdurl("http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF");
				prod.setUuid(java.util.UUID.randomUUID().toString().replace("-", ""));
				list.add(prod);
			} catch (Exception e) {
				ecount++;
				if (ecount > 100)
					System.exit(0);
				e.printStackTrace();
			}
		}
		save(list);
	}

	public static void process(String conName, String coneName, String no) throws Exception {
		List<Prod> list = new ArrayList<Prod>();
		// String conName="华汇人寿保险股份有限公司"; //公司全称
		// String coneName="华汇人寿"; //公司简称
		StringBuilder sb = new StringBuilder();
		sb.append("pageNo=1").append("&pageCount=1").append("&prodTermsShow.prodName=")
				// .append(URLEncoder.encode(proName,"gb2312"))
				.append("&prodTermsShow.insComName=").append(URLEncoder.encode(conName, "gb2312"))
				.append("&prodTermsShow.insItemCode=").append("&prodTermsShow.saleStatus=")
				.append("&prodTermsShow.specialAttri=").append("&prodTermsShow.insType=")
				.append("&prodTermsShow.insPerdType=")
				// .append("&prodTypeCodeOne=ProdTypeCode_00")
				// .append("&prodTypeCodeWwo=ProdTypeCode_02")
				// .append("&prodTermsShow.prodTypeCode=ProdTypeCode_00_02")
				.append("&prodTermsShow.prodDesiCode=").append("&pageSize=1000").append("&goToPage=");
		String param = sb.toString();
		String html = sendPost("http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQueryResult.do", param);
		Document doc = Jsoup.parse(html);
		Elements common = doc.getElementsByClass("common1");

		StringBuilder cotent = new StringBuilder();
		StringBuilder erro = new StringBuilder();
		File basepath = new File("e:/prod/" + coneName);
		if (!basepath.exists()) {
			basepath.mkdirs();
		}
		File f = new File("e:/prod/" + coneName + "/" + coneName + ".txt");
		File errLog = new File("e:/prod/" + coneName + "/erro.txt");
		FileWriter fw = new FileWriter(f);
		FileWriter errLogfw = new FileWriter(errLog);
		String prodname = null;
		for (int i = 0; i < common.size(); i++) {
			Prod prod = new Prod();
			try {
				cotent.append("-" + i + "-----------------------begin--------------------------");
				cotent.append(System.getProperty("line.separator"));
				System.out.println("-" + i + "-----------------------begin--------------------------");
				String onclick = doc.getElementById("detailed" + (i + 1)).attr("onclick");
				// 产品的id
				String uuid = onclick.substring(10, onclick.length() - 2).replace("'", "").split(",")[0];
				// 产品名称
				prodname = common.get(i).getElementsByTag("td").get(2).text();
				// get请求详细信息
				Document docdetail = Jsoup.connect("http://www.iachina.cn/IC/tkk/02/" + uuid + ".html")
						.timeout(1000 * 3 * 60).get();
				Elements tr = docdetail.getElementsByClass("biaoge").first().getElementsByTag("tr");
				// 产品的详情信息
				for (int j = 0; j < tr.size(); j++) {
					if (j == 0) {
						prod.setCompany(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 1) {
						prod.setProdname(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 2) {
						prod.setProdtype(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 3) {
						prod.setDesigntype(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 4) {
						prod.setSpecialattr(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 5) {
						prod.setApproveway(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 6) {
						prod.setProtectetime(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 7) {
						prod.setPayway(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 8) {
						prod.setItemcode(tr.get(j).getElementsByTag("td").get(1).text());
					} else if (j == 9) {
						prod.setIssale(tr.get(j).getElementsByTag("td").get(1).text());
						;
					} else if (j == 10) {
						prod.setOutsaletime(tr.get(j).getElementsByTag("td").get(1).text());
					}
					cotent.append(tr.get(j).getElementsByTag("td").get(0).text());
					cotent.append(tr.get(j).getElementsByTag("td").get(1).text());
					cotent.append(System.getProperty("line.separator"));

					System.out.print(tr.get(j).getElementsByTag("td").get(0).text());
					System.out.println(tr.get(j).getElementsByTag("td").get(1).text());
				}
				// 获取产品PDF
				// http://www.iachina.cn/IC/tkk/03/ecd6e585-df4a-401f-ae36-7aac4bdb772f_TERMS.PDF
				if (prod.getIssale().substring(0, prod.getIssale().length() - 1).equals("在售")) {
					downLoadFromUrl("http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF", prodname,
							"e:/prod/" + coneName + "/在售/");
				} else if (prod.getIssale().substring(0, prod.getIssale().length() - 1).equals("停用")) {
					downLoadFromUrl("http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF", prodname,
							"e:/prod/" + coneName + "/停用/");
				} else if (prod.getIssale().substring(0, prod.getIssale().length() - 1).equals("停售")) {
					downLoadFromUrl("http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF", prodname,
							"e:/prod/" + coneName + "/停售/");
				}
				// downLoadFromUrl("http://www.iachina.cn/IC/tkk/03/"+uuid+"_TERMS.PDF",prodname+".pdf","e:/prod/"+coneName+"/");
				System.out.println("产品条款URL:http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF");
				System.out.println("-" + i + "-----------------------end--------------------------");

				prod.setProdurl("http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF");
				prod.setNo(no);
				prod.setUuid(java.util.UUID.randomUUID().toString().replace("-", ""));
				list.add(prod);
				cotent.append("产品条款URL:http://www.iachina.cn/IC/tkk/03/" + uuid + "_TERMS.PDF");
				cotent.append(System.getProperty("line.separator"));
				cotent.append("-" + i + "-----------------------end--------------------------");
				cotent.append(System.getProperty("line.separator"));
			} catch (Exception e) {
				ecount++;
				erro.append("获取" + coneName + "---" + prodname + "失败,连接超时!第" + ecount + "次失败" + e.getMessage());
				if (ecount > 100)
					System.exit(0);
				erro.append(System.getProperty("line.separator"));
				e.printStackTrace();
			}
		}
		if (cotent.length() >= 0)
			;
		fw.write(cotent.toString());
		if (erro.length() >= 0)
			;
		errLogfw.write(erro.toString());
		fw.flush();
		errLogfw.flush();
		if (fw != null)
			fw.close();
		if (errLogfw != null)
			errLogfw.close();
		save(list);
	}

	public static void save(List<Prod> list) throws Exception {
		// 写入数据库
		System.out.println("~~~~~~~~~~~~~~~~开始写入数据库~~~~~~~~~~~~~~");
		Connection conn = JdbcUtil.getConnection();
		for (Prod prod : list) {
			PreparedStatement pre = conn.prepareStatement(
					"insert into tb_prod_all(company,prodname,prodtype,designtype,specialattr ,approveway ,protectetime ,payway,itemcode ,issale,outsaletime , produrl,no,uuid) values"
							+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pre.setString(1, prod.getCompany().substring(0, prod.getCompany().length() - 1));
			pre.setString(2, prod.getProdname().substring(0, prod.getProdname().length() - 1));
			pre.setString(3, prod.getProdtype().substring(0, prod.getProdtype().length() - 1));
			pre.setString(4, prod.getDesigntype().substring(0, prod.getDesigntype().length() - 1));
			pre.setString(5, prod.getSpecialattr().substring(0, prod.getSpecialattr().length() - 1));
			pre.setString(6, prod.getApproveway().substring(0, prod.getApproveway().length() - 1));
			pre.setString(7, prod.getProtectetime().substring(0, prod.getProtectetime().length() - 1));
			pre.setString(8, prod.getPayway().substring(0, prod.getPayway().length() - 1));
			pre.setString(9, prod.getItemcode().substring(0, prod.getItemcode().length() - 1));
			pre.setString(10, prod.getIssale().substring(0, prod.getIssale().length() - 1));
			pre.setString(11, prod.getOutsaletime().substring(0, prod.getOutsaletime().length() - 1));
			pre.setString(12, prod.getProdurl().substring(0, prod.getProdurl().length() - 1));
			pre.setString(13, prod.getNo());
			pre.setString(14, prod.getUuid());
			pre.executeUpdate();
		}
		conn.close();
		System.out.println("~~~~~~~~~~~~~~~~写入完成~~~~~~~~~~~~~~");
	}

	public static String sendPost(String url, String param) throws Exception  {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Charset", "gb2312");
			// 发送POST请求必须设置如下两行
			conn.setConnectTimeout(1000 * 3 * 60);
			conn.setReadTimeout(1000 * 3 * 60);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		// 使用finally块来关闭输出流、输入流
			if (out != null) {
					out.close();
			}
			if (in != null) {
					in.close();
			}
		return result;
	}

	public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.82 Safari/537.36");
		// 尝试连接30s
		conn.setConnectTimeout(1000 * 3 * 60);
		conn.setReadTimeout(1000 * 3 * 60);
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		File file = new File(saveDir + File.separator + fileName + ".pdf");
		File file1 = new File(saveDir + File.separator + fileName + "1.pdf");
		File file2 = new File(saveDir + File.separator + fileName + "2.pdf");
		File file3 = new File(saveDir + File.separator + fileName + "3.pdf");
		File file4 = new File(saveDir + File.separator + fileName + "4.pdf");
		File file5 = new File(saveDir + File.separator + fileName + "5.pdf");
		File file6 = new File(saveDir + File.separator + fileName + "6.pdf");
		FileOutputStream fos = null;
		if (file.exists()) {
			if (file1.exists()) {
				if (file2.exists()) {
					if (file3.exists()) {
						if (file4.exists()) {
							if (file5.exists()) {
								if (file6.exists()) {
								} else {
									fos = new FileOutputStream(file6);
									fos.write(getData);
								}
							} else {
								fos = new FileOutputStream(file5);
								fos.write(getData);
							}
						} else {
							fos = new FileOutputStream(file4);
							fos.write(getData);
						}
					} else {
						fos = new FileOutputStream(file3);
						fos.write(getData);
					}
				} else {
					fos = new FileOutputStream(file2);
					fos.write(getData);
				}
			} else {
				fos = new FileOutputStream(file1);
				fos.write(getData);
			}
		} else {
			fos = new FileOutputStream(file);
			fos.write(getData);
		}
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		System.out.println("info:" + url + " download success");
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

	@Override
	public void run() {
		try {
			porcessAll(pageNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
