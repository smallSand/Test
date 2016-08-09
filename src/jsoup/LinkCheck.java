package jsoup;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.AnnotationUtil;
public class LinkCheck {

	private static List<String> urls=new ArrayList<String>();
	
	public static void main(String[] arg) throws Exception {
		Connection dbconn=JdbcUtil.getConnection();
		Map<String, String> map= AnnotationUtil.getInstance().loadVlaue(company.class, "value",  
        		LinkCheck.class.getName()); 
		LinkCheck LinkCheck=new LinkCheck();
		@SuppressWarnings("unchecked")
		Class<LinkCheck> LC=(Class<jsoup.LinkCheck>) LinkCheck.getClass();
		for(String company:map.keySet()){
			String methodname=map.get(company);
			Method method=LC.getMethod(methodname, Connection.class,String.class);
			try{
				System.out.println("----------------开始调用"+methodname+"--------------------");
				method.invoke(LinkCheck, dbconn,company);
				System.out.println("----------------结束调用"+methodname+"--------------------");
			}
			catch(Exception e){		
				System.err.println("++++++++++++方法"+method.getName()+"调用异常+++++++++++");
			}
		}
		dbconn.close();
	}

	public static void process(Connection dbconn,String company,String proname, String prourl) throws Exception {
		String insertSQl = "insert into tb_product_new(name,url,com) values(?,?,?)";
		PreparedStatement insertPre = dbconn.prepareStatement(insertSQl);
		insertPre.setString(1, proname);
		insertPre.setString(2, prourl);
		insertPre.setString(3, company);
		insertPre.executeUpdate();
	}

	@company("安邦人寿保险股份有限公司")
	public static void save_ABRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.anbang-life.com/gkxxpl/jbxx/zscp/index.htm").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("tr");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			Elements ps = e.getElementsByTag("p");
			if(i>=1){
			    System.out.println(ps.get(1).text());
			    System.out.println(host+ps.get(3).getElementsByTag("a").first().attr("href").substring(8));
			    process(dbconn,company,ps.get(1).text(),host+ps.get(3).getElementsByTag("a").first().attr("href").substring(8));
			}
		}
	}
	@company("安邦养老保险股份有限公司")
	public static void save_ABYN(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.anbangannuity.com/gkxxpl/jbxx/gsgk/index.htm").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href = e.attr("href");
			if (href.contains(".pdf") && e.text().equals("下载") == false) {
				String url = "";
				if (href.startsWith("..")) {
					url = host + href.substring(8);
				} else {
					url = href;
				}
				System.out.println(url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),url);
			}
		}
		
	}
	
	@company("北大方正人寿保险有限公司")
	public static void save_BDFZ(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.pkufi.com/node/18057").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if (e.text().equals("条款")) {
				String url = e.attr("href");
				System.out.println(url);
				String[] arr=url.split("/");
				System.out.println(arr[arr.length-1]);
				process(dbconn,company,arr[arr.length-1], url);
			}
		}
	}
	@company("渤海人寿保险股份有限公司")
	public static void save_BHRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.bohailife.net/xxpl/hlwbx/hlwbxxxpl.shtml").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if (e.text().equals("查看条款")) {
				String url = e.attr("href");
				System.out.println(host+url);
				String[] arr=url.split("/");
				System.out.println(arr[arr.length-1]);
				process(dbconn,company,arr[arr.length-1],host + url);
			}
		}
	}
	@company("东吴人寿保险股份有限公司")
	public static void save_DWRS(Connection dbconn,String company) throws Exception {
		String[] str=new String[]{"http://www.soochowlife.net/dwopeninfos/dwbaseinfos/dwsalepros/index.jsp","http://www.soochowlife.net/dwopeninfos/dwbaseinfos/dwtscp/index.jsp"};
		for(String s:str){
		org.jsoup.Connection conn = Jsoup.connect(s).timeout(500000);
		// 停售
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String url = e.attr("href");
			if (url.endsWith(".pdf")) {
				System.out.println(host+url.substring(8));
				String[] arr=url.split("/");
				System.out.println(arr[arr.length-1]);
				process(dbconn,company,arr[arr.length-1],host+url.substring(8));
			}
		}
		}
	}
	@company("富德生命人寿保险股份有限公司")
	public static void save_FDSM(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sino-life.com/Product.txt?_=1470220839437").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		String json=doc.select("body").text();
		JSONObject obj=new JSONObject(json);
		JSONArray arr=obj.getJSONArray("Products");
		for(int i=0;i<arr.length();i++){
			System.out.println(arr.getJSONObject(i).getString("name"));
			System.out.println(host+arr.getJSONObject(i).getString("href"));
			process(dbconn,company,arr.getJSONObject(i).getString("name"),host+arr.getJSONObject(i).getString("href"));
		}
	}
	@company("工银安盛人寿保险有限公司")
	public static void save_GYAS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("https://www.icbc-axa.com/about_icbc_axa/sd/organization.jsp").timeout(500000);
		conn.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection","keep-alive");
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String url = e.attr("href");
			if (url.endsWith(".pdf")) {
				System.out.println(host+url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),host + url);
			}
		}
	}
	@company("光大永明人寿保险有限公司")
	public static void save_GDYM(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sunlife-everbright.com/tabid/788/Default.aspx").timeout(500000);
		conn.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection","keep-alive");
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String url = e.attr("href");
			if (url.endsWith(".pdf")) {
				System.out.println(host+url);
				String[] arr=url.split("/");
				System.out.println(arr[arr.length-1]);
				process(dbconn,company,arr[arr.length-1],host + url);
			}
		}
	}
	@company("国华人寿保险股份有限公司")
	public static void save_GHRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.95549.cn/pages/intro/xxpl04_detail02.shtml").timeout(500000);
		conn.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection","keep-alive");
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String url = e.attr("href");
			if (url.endsWith(".pdf")) {
				System.out.println(url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),url);
			}
		}
	}
	@company("合众人寿保险股份有限公司")
	public static void save_HZRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.unionlife.com.cn/unionlife/public/basic/hlwbxxx/hlwcpxx/index.html").timeout(500000);
		conn.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection","keep-alive");
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String url = e.attr("href");
			if (url.endsWith(".pdf")&&e.text().contains("条款")) {
				System.out.println(host+url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),host + url);
			}
		}
	}
	@company("和谐健康保险股份有限公司")
	public static void save_HXJK(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.hexiehealth.com/xxpl/jbxx/zscp/index.htm").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("tr");

		for (int i = 0; i < elements.size(); i++) {
			
			Element e = elements.get(i);
			Elements tds = e.getElementsByTag("td");
			if(i>=1){
			    System.out.println(tds.get(1).text());
			    System.out.println(host+tds.get(4).getElementsByTag("a").attr("href").substring(8));
			    process(dbconn,company,tds.get(1).text(),host+tds.get(4).getElementsByTag("a").attr("href").substring(8));
			}
		}
	}
	@company("恒安标准人寿保险有限公司")
	public static void save_HABZ(Connection dbconn,String company) throws Exception {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://www.hengansl.com/cha/2304450.html");
		queue.offer("http://www.hengansl.com/cha/2304452.html");
		queue.offer("http://www.hengansl.com/cha/2304451.html");
		queue.offer("http://www.hengansl.com/cha/2304453.html");
		queue.offer("http://www.hengansl.com/cha/49308255.html");
		queue.offer("http://www.hengansl.com/cha/2304453.html");
		String s;
		while((s=queue.poll())!=null){
		
		org.jsoup.Connection conn = Jsoup.connect(s).timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href = e.attr("href");
			if (href.contains(".pdf") && e.text().equals("下载") == false) {
				String url = "";
				if (href.startsWith("..")) {
					url = host + href.substring(8);
				} else {
					url = href;
				}
				System.out.println(url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),url);
			}
		}
		}
	}
	@company("恒大人寿保险有限公司")
	public static void save_HDRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.lifeisgreat.com.cn/portal/info/getInfo.jspa?categoryId=1000&channelType=1").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href = e.attr("href");
			if (href.contains(".pdf") && e.text().contains(("保险"))) {
				String url = "";
				if (href.startsWith("..")) {
					url = host + href.substring(5);
				} else {
					url = href;
				}
				System.out.println(url);
				System.out.println(e.text());
				process(dbconn,company,e.text(),url);
			}
		}
		
	}
	@company("弘康人寿保险股份有限公司")
	public static void save_HKRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.hongkang-life.com/product/toShowContent.do?extends1=partnerInfo").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("tr");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(i>=1){
				if(e.text().contains("查看条款 ")){
				    System.out.println(e.select("td").get(0).text());
				    System.out.println(host+e.getElementsByTag("a").first().attr("href"));
					process(dbconn,company,e.select("td").get(0).text(),host+e.getElementsByTag("a").first().attr("href"));

				}
			}
		}
	}
	@company("华汇人寿保险股份有限公司")
	public static void save_HHRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sciclife.com/base_survey/_content/13_05/02/1367479651970_1.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(href.endsWith(".pdf")){
				System.out.println(e.text());
				System.out.println(host+href);
				process(dbconn,company,e.text(),host+href);
			}
		}

	}
	
	@company("华夏人寿保险股份有限公司")
	public static void save_HXRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.hxlife.com/publish/main/1080/1081/1088/20121219100610665359475/index.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")&&e.text().equals("点击下载")==false){
			    System.out.println(e.text());
			    System.out.println(host+e.attr("href"));
				process(dbconn,company,e.text(),host+e.attr("href"));
			}
		}
	}
    //汇丰人寿保险有限公司
	public static void save_HFRS(Connection dbconn,String company) throws Exception {
	}
	@company("吉祥人寿保险股份有限公司")
	public static void save_JXRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.jxlife.com.cn/clauseManage/query.do").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("tr");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(i>=1){
				    System.out.println(e.select("td").get(1).text());
				    System.out.println(e.getElementsByTag("a").first().attr("href"));
					process(dbconn,company,e.select("td").get(1).text(),e.getElementsByTag("a").first().attr("href"));
			}
		}
	}
	@company("建信人寿保险有限公司")
	public static void save_JXRSBX(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.ccb-life.com.cn/gkxxpl/jbxx/gsgk/108.shtml?timestamp=1468821212049").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(href.endsWith(".pdf")){
				if(href.startsWith("..")) href=host+href.substring(8);
				System.out.println(e.text());
				System.out.println(href);
				process(dbconn,company,e.text(),href);
			}
		}
	}
	@company("交银康联人寿保险有限公司")
	public static void save_JYKL(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.bocommlife.com/sites/main/twainindex/xxpl.htm?columnid=297&page=1").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("tr");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(i>=1&&e.select("td").get(0).text().matches("[0-9].*")){
				    System.out.println(e.select("td").get(1).text());
				    System.out.println(host+e.getElementsByTag("a").first().attr("href"));
					process(dbconn,company,e.select("td").get(1).text(),host+e.getElementsByTag("a").first().attr("href"));
			}
		}
	}
	
	@company("君康人寿保险股份有限公司")
	public static void save_JKRS(Connection dbconn,String company) throws Exception {
		for(int j=1;j<19;j++){
		org.jsoup.Connection conn = Jsoup.connect("http://www.zdlife.com/front/productItems.do?pageNo="+j).timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("tr");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(i>=1&&e.select("td").get(0).text().contains("条款")){
				    System.out.println(e.select("td").get(0).text());
				    System.out.println(host+"/front/"+e.getElementsByTag("a").first().attr("href"));
					process(dbconn,company,e.select("td").get(0).text(),host+"/front/"+e.getElementsByTag("a").first().attr("href"));
			}
		}
		}
	}
	@company("君龙人寿保险有限公司")
	public static void save_JLRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.kdlins.com.cn/info!detail.action").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(e.text().contains("君龙")&&href.endsWith(".pdf")){
				System.out.println(e.text());
				System.out.println(host+"/"+href);
				process(dbconn,company,e.text(),host+"/"+href);
			}
		}
	}
	@company("昆仑健康保险股份有限公司")
	public static void save_KLJK(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.kunlunhealth.com/templet/default/xxpl.jsp?id=1375").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(href.contains("/templet/default/ShowArticle_xxpl.jsp?id=")){
				Elements as =Jsoup.connect("http://"+host+href).get().getElementsByTag("a");
					for(Element a:as){
						if(a.attr("href").endsWith(".pdf")){
							System.out.println(e.text());
							System.out.println(host+a.attr("href"));
							process(dbconn,company,e.text(),host+a.attr("href"));
						}
					}
			}
		}
	}
	
	@company("利安人寿保险股份有限公司")
	public static void save_LARS(Connection dbconn,String company) throws Exception {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsgxcp/");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsgxcp/index_1.shtml");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsybcp/");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsybcp/index_1.shtml");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zstxcp/");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zstxcp/index_1.shtml");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsxqdcp/");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsxqdcp/index_1.shtml");
		queue.offer("http://www.lianlife.cc/xxpl/jbxx/zscp/zsjdcp/");
		String url;
		while((url=queue.poll())!=null){
		org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(href.endsWith(".pdf")){
				System.out.println(e.text());
				System.out.println(host+"/"+href.substring(12));
				process(dbconn,company,e.text(),host+"/"+href.substring(12));
			}
		}
		}
	}
	@company("陆家嘴国泰人寿保险有限责任公司")
	public static void save_LJZGT(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("https://www.cathaylife.cn/publish/main/113/index.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			String href=e.attr("href");
			if(href.endsWith(".pdf")&&e.text().equals("点击查看")==false){
				System.out.println(e.text());
				System.out.println(host+href);
				process(dbconn,company,e.text(),host+href);
			}
		}
	}
	@company("民生人寿保险股份有限公司")
	public static void save_MSRS(Connection dbconn,String company) throws Exception {
		for(int i=1;i<7;i++){
			org.jsoup.Connection conn = Jsoup.connect("http://www.minshenglife.com/templet/default/ShowClass.jsp?id=991&pid=990&pn="+i).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = conn.get();
			Elements elements = doc.getElementsByTag("a");
	
			for (int j = 0; j < elements.size(); j++) {
				Element e = elements.get(j);
				String href=e.attr("href");
				if(href.contains("/templet/default/ShowArticle.jsp")){
					Elements as =Jsoup.connect("http://"+host+href).get().getElementsByTag("a");
						for(Element a:as){
							if(a.attr("href").endsWith(".doc")||a.attr("href").endsWith(".pdf")||a.attr("href").endsWith(".docx")){
								System.out.println(e.text());
								System.out.println(host+a.attr("href"));
								process(dbconn,company,e.text(),host+a.attr("href"));
								break;
							}
						}
				}
			}
		}
		
		for(int i=1;i<3;i++){
			org.jsoup.Connection conn = Jsoup.connect("http://www.minshenglife.com/templet/default/ShowClass.jsp?id=992&pid=990&pn="+i).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = conn.get();
			Elements elements = doc.getElementsByTag("a");
	
			for (int j = 0; j < elements.size(); j++) {
				Element e = elements.get(j);
				String href=e.attr("href");
				if(href.contains("/templet/default/ShowArticle.jsp")){
					Elements as =Jsoup.connect("http://"+host+href).get().getElementsByTag("a");
					for(Element a:as){
						if(a.attr("href").endsWith(".doc")||a.attr("href").endsWith(".pdf")||a.attr("href").endsWith(".docx")){
							System.out.println(e.text());
							System.out.println(host+a.attr("href"));
							process(dbconn,company,e.text(),host+a.attr("href"));
							break;
						}
					}
				}
			}
		}
	}
	
	@company("农银人寿保险股份有限公司")
	public static void save_NYRS(Connection dbconn,String company) throws Exception {
		for(int j=1;j<9;j++){
		org.jsoup.Connection conn = Jsoup.connect("http://www.abchinalife.cn/cms-web/front/abchinalife/xxpl/jbxx/cpmljtk/zscpmljtk/zscpmljtk@"+j+".html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")){
				    System.out.println(e.text());
				    System.out.println(host+e.attr("href"));
					process(dbconn,company,e.text(),host+e.attr("href"));
			}
		}
		}
		for(int j=1;j<4;j++){
			org.jsoup.Connection conn = Jsoup.connect("http://www.abchinalife.cn/cms-web/front/abchinalife/xxpl/jbxx/cpmljtk/tscpmljtk/tscpmljtk@"+j+".html").timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = conn.get();
			Elements elements = doc.getElementsByTag("a");
	
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if(e.attr("href").endsWith(".pdf")){
					System.out.println(e.text());
					System.out.println(host+e.attr("href"));
					process(dbconn,company,e.text(),host+e.attr("href"));
				}
			}
		}
	}
	
	@company("平安健康保险股份有限公司")
	public static void save_PAJK(Connection dbconn,String company) throws Exception {
		for(int j=1;j<4;j++){
		org.jsoup.Connection conn = Jsoup.connect("http://health.pingan.com/gongkaixinxipilu/baoxianchanpinmulujitiaokuan_"+j+".shtml").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")){
				    System.out.println(e.text());
				    System.out.println(e.attr("href"));
					process(dbconn,company,e.text(),e.attr("href"));
			}
		}
		}
	}
	
	@company("平安养老保险股份有限公司")
	public static void save_PAYN(Connection dbconn,String company) throws Exception {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://paylx.pingan.cn/informationDisclosure/insuranceProductList.shtml");
		queue.offer("http://paylx.pingan.cn/informationDisclosure/insuranceProductList_2.shtml");
		queue.offer("http://paylx.pingan.cn/informationDisclosure/grwtcp/productRecordItemsList.shtml");
		String url;
		while((url=queue.poll())!=null){
		org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.getElementsByTag("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")){
				    System.out.println(e.text());
				    System.out.println(e.attr("href"));
					process(dbconn,company,e.text(),e.attr("href"));
			}
		}
		}
	}
	@company("前海人寿保险股份有限公司")
	public static void save_QHRS(Connection dbconn,String company) throws Exception {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/496/20150604143510770901389/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/497/20150609110608960839474/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/496/20150608101711743337460/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/497/20140220160222580952616/index.html");
		String url;
		while((url=queue.poll())!=null){
		org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("tr");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			Elements tds = e.getElementsByTag("td");
			if(i>=1){
			    System.out.println(tds.get(0).text());
			    System.out.println(host+tds.get(1).getElementsByTag("a").attr("href"));
				process(dbconn,company,tds.get(0).text(),host+tds.get(1).getElementsByTag("a").attr("href"));
			}
		}
		}
	}
	@company("瑞泰人寿保险有限公司")
	public static void save_RTRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")){
			    System.out.println(e.text());
			    System.out.println("http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/"+e.attr("href"));
				process(dbconn,company,e.text(),"http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/"+e.attr("href"));
			}
		}
	}
	@company("上海人寿保险股份有限公司")
	public static void save_SHRS(Connection dbconn,String company) throws Exception {
		String url;
		for(int j= 1;j<4;j++){
			if(j==1){
				url="http://www.shanghailife.com.cn/cpzx/cptk/zscp/";
			}else{
				url="http://www.shanghailife.com.cn/cpzx/cptk/zscp/index_"+j+".shtml";
			}
		org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".shtml")&&e.attr("href").startsWith("http://www.shanghailife.com.cn/cpzx/cptk/zscp/")){
				Elements as=Jsoup.connect(e.attr("href")).get().select("a");
				for(Element a:as){
					if(a.text().equals("保险条款")){
						System.out.println(e.text());
						System.out.println(a.attr("href"));
						process(dbconn,company,e.text(),a.attr("href"));
					}
						
				}
			}
		}
		}
	}
	@company("太保安联健康保险股份有限公司")
	public static void save_TBAL(Connection dbconn,String company) throws Exception {
		String url;
		for(int j= 1;j<4;j++){
			if(j==1){
				url="http://health.cpic.com.cn/jkx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/index.shtml";
			}else{
				url="http://health.cpic.com.cn/jkx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/index_"+j+".shtml";
			}
		org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")&&e.text().equals("点击下载")==false){
			    System.out.println(e.text());
			    System.out.println(host+e.attr("href"));
				process(dbconn,company,e.text(),host+e.attr("href"));
			}
		}
		
		}
	}
	@company("太平人寿保险有限公司")
	public static void save_TPRS(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://life.cntaiping.com/info-bxcp/").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")&&e.text().equals("点击下载")==false){
			    System.out.println(e.text());
			    System.out.println(e.attr("href"));
				process(dbconn,company,e.text(),e.attr("href"));
			}
		}
	}
	@company("太平养老保险股份有限公司")
	public static void save_TPYN(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://tppension.cntaiping.com/info-bxcp/").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")&&e.text().equals("点击下载")==false){
				System.out.println(e.text());
				System.out.println(e.attr("href"));
				process(dbconn,company,e.text(),e.attr("href"));
			}
		}
	}
	@company("泰康人寿保险股份有限公司")
	public static void save_TKRS(Connection dbconn,String company) throws Exception {
		Queue<String> queue = new LinkedList<String>();
		for(int i=1;i<10;i++){
			queue.offer("http://www.taikang.com/publicinfo/464455/tab1189/889379/ffb20838-"+i+".shtml");
		}
		for(int i=1;i<4;i++){
			queue.offer("http://www.taikang.com/publicinfo/464455/tab1189/889383/b489ad98-"+i+".shtml");
		}
		for(int i=1;i<6;i++){
			queue.offer("http://www.taikang.com/publicinfo/464455/tab1189/889387/731ae22c-"+i+".shtml");
		}
		for(int i=1;i<7;i++){
			queue.offer("http://www.taikang.com/publicinfo/464455/tab1189/889391/8e96a08d-"+i+".shtml");
		}
		for(int i=1;i<6;i++){
			queue.offer("http://www.taikang.com/publicinfo/464455/tab1189/889391/2cdec68d-"+i+".shtml");
		}
		String url;
		while((url=queue.poll())!=null){
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = conn.get();
			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href=e.attr("href");
				if(href.startsWith("/")){
					href=host+href;
				}
				if(href.endsWith(".pdf")&&e.text().equals("查看条款")==false&&urls.contains(href)==false){
					System.out.println(href);
					System.out.println(e.text());
					process(dbconn,company,e.text(),e.attr("href"));
					urls.add(href);
				}
			}
		}
	}
	@company("泰康养老保险股份有限公司")
	public static void save_TKYL(Connection dbconn,String company) throws Exception {
		String url;
		for(int j=1;j<10;j++){
			if(j==1){ 
				 url="http://tkyl.pension.taikang.com/cms/static/xxpl/cpxxpl/list.html";
			}else{
				url="http://tkyl.pension.taikang.com/cms/static/xxpl/cpxxpl/list_"+j+".html";
			}
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = conn.get();
			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if(e.attr("href").endsWith(".pdf")&&e.text().equals("点击下载")==false){
					System.out.println(e.getElementsByTag("p").text());
					System.out.println(host+e.attr("href"));
					process(dbconn,company,e.getElementsByTag("p").text(),host+e.attr("href"));
				}
			}
		}
	}
	
	@company("同方全球人寿保险有限公司")
	public static void save_TFQQ(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.aegonthtf.com/info/xxpl.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = conn.get();
		Element element1 = doc.getElementById("tk-cont1");
		Element element2 = doc.getElementById("tk-cont2");
		Elements elements1 = element1.getElementsByTag("tr");
		Elements elements2 = element2.getElementsByTag("tr");
		for (int i = 0; i < elements1.size(); i++) {
			Element e = elements1.get(i);
			if(i>=1){
				Elements tds=e.getElementsByTag("td");
				try{
					System.out.println(tds.get(0).text());
					System.out.println(host+"/info/"+tds.get(2).getElementsByTag("a").attr("href"));
					process(dbconn,company,tds.get(0).text(),host+"/info/"+tds.get(2).getElementsByTag("a").attr("href"));
				}catch(Exception e1){
				}
			}
		}
		
		for (int i = 0; i < elements2.size(); i++) {
			Element e = elements2.get(i);
			if(i>=1){
				Elements tds=e.getElementsByTag("td");
				try{
					System.out.println(tds.get(0).text());
					System.out.println(host+"/info/"+tds.get(4).getElementsByTag("a").attr("href"));
					process(dbconn,company,tds.get(0).text(),host+"/info/"+tds.get(4).getElementsByTag("a").attr("href"));
				}catch(Exception e1){
				}
			}
		}
		
	}
	@company("新光海航人寿保险有限责任公司")
	public static void save_XGHH(Connection dbconn,String company) throws Exception {
		org.jsoup.Connection conn = Jsoup.connect("http://www.skl-hna.com/info/infobase/tiaokuan/").timeout(500000);
		Document doc = conn.get();
		Elements elements = doc.select("a");
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			if(e.attr("href").endsWith(".pdf")){
				System.out.println(e.text());
				System.out.println("http://www.skl-hna.com/info/infobase/tiaokuan/"+e.attr("href"));
				process(dbconn,company,e.text(),"http://www.skl-hna.com/info/infobase/tiaokuan/"+e.attr("href"));
			}
		}
	}
}
