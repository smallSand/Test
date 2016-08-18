package jsoup;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AnnotationUtil;

public class LinkCheck {
	
	private static List<String> urls = new ArrayList<String>();
	
	public static final Logger LOG = LoggerFactory.getLogger(LinkCheck.class);
	
	public static void main(String[] arg) throws Exception {
		Connection dbconn = JdbcUtil.getConnection();
		urls=JdbcUtil.getUrls(dbconn);
		Map<String, String> map = AnnotationUtil.getInstance().loadVlaue(company.class, "value",LinkCheck.class.getName());
		LinkCheck LinkCheck = new LinkCheck();
		@SuppressWarnings("unchecked")
		Class<LinkCheck> LC = (Class<jsoup.LinkCheck>) LinkCheck.getClass();
		for (String company : map.keySet()) {
			String methodname = map.get(company);
			Method method = LC.getMethod(methodname, Connection.class, String.class);
			try {
				LOG.info(("----------------开始调用" + methodname + "--------------------"));
				method.invoke(LinkCheck, dbconn, company);
				LOG.info("----------------结束调用" + methodname + "--------------------");
			} catch (Exception e) {
				LOG.error(("++++++++++++方法" + method.getName() + "调用异常+++++++++++" + e.getMessage()));
			}
		}
		dbconn.close();

	}

	public static void process(Connection dbconn, String company, String proname, String prourl) {
		if(urls.contains(prourl)){
			String insertSQl = "insert into tb_product_new(name,url,com) values(?,?,?)";
			PreparedStatement insertPre;
			try {
				insertPre = dbconn.prepareStatement(insertSQl);
				insertPre.setString(1, proname);
				insertPre.setString(2, prourl);
				insertPre.setString(3, company);
				insertPre.executeUpdate();
			} catch (SQLException e) {
				LOG.error(e.getMessage() + insertSQl + company + proname + prourl);
			}
		}
	}

	@company("安邦人寿保险股份有限公司")
	public static void save_ABRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.anbang-life.com/gkxxpl/jbxx/zscp/index.htm")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("tr");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				Elements ps = e.getElementsByTag("p");
				if (i >= 1) {
					LOG.info(ps.get(1).text());
					LOG.info(host + ps.get(3).getElementsByTag("a").first().attr("href").substring(8));
					process(dbconn, company, ps.get(1).text(),
							host + ps.get(3).getElementsByTag("a").first().attr("href").substring(8));
				}
			}
		}
	}

	@company("安邦养老保险股份有限公司")
	public static void save_ABYN(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.anbangannuity.com/gkxxpl/jbxx/gsgk/index.htm")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
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
					LOG.info(url);
					LOG.info(e.text());
					process(dbconn, company, e.text(), url);
				}
			}
		}

	}

	@company("北大方正人寿保险有限公司")
	public static void save_BDFZ(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.pkufi.com/node/18057").timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.text().equals("条款")) {
					String url = e.attr("href");
					LOG.info(url);
					String[] arr = url.split("/");
					LOG.info(arr[arr.length - 1]);
					process(dbconn, company, arr[arr.length - 1], url);
				}
			}
		}
	}

	@company("渤海人寿保险股份有限公司")
	public static void save_BHRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.bohailife.net/xxpl/hlwbx/hlwbxxxpl.shtml")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.text().equals("查看条款")) {
					String url = e.attr("href");
					LOG.info(host + url);
					String[] arr = url.split("/");
					LOG.info(arr[arr.length - 1]);
					process(dbconn, company, arr[arr.length - 1], host + url);
				}
			}
		}
	}

	@company("东吴人寿保险股份有限公司")
	public static void save_DWRS(Connection dbconn, String company) {
		String[] str = new String[] { "http://www.soochowlife.net/dwopeninfos/dwbaseinfos/dwsalepros/index.jsp",
				"http://www.soochowlife.net/dwopeninfos/dwbaseinfos/dwtscp/index.jsp" };
		for (String s : str) {
			org.jsoup.Connection conn = Jsoup.connect(s).timeout(500000);
			// 停售
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}

			if (doc != null) {
				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String url = e.attr("href");
					if (url.endsWith(".pdf")) {
						LOG.info(host + url.substring(8));
						String[] arr = url.split("/");
						LOG.info(arr[arr.length - 1]);
						process(dbconn, company, arr[arr.length - 1], host + url.substring(8));
					}
				}
			}
		}
	}

	@company("富德生命人寿保险股份有限公司")
	public static void save_FDSM(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sino-life.com/Product.txt?_=1470220839437")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		String json = doc.select("body").text();
		JSONObject obj = new JSONObject(json);
		JSONArray arr = obj.getJSONArray("Products");
		for (int i = 0; i < arr.length(); i++) {
			LOG.info(arr.getJSONObject(i).getString("name"));
			LOG.info(host + arr.getJSONObject(i).getString("href"));
			process(dbconn, company, arr.getJSONObject(i).getString("name"),
					host + arr.getJSONObject(i).getString("href"));
		}
	}

	@company("工银安盛人寿保险有限公司")
	public static void save_GYAS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("https://www.icbc-axa.com/about_icbc_axa/sd/organization.jsp")
				.timeout(500000);
		conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection", "keep-alive");
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String url = e.attr("href");
				if (url.endsWith(".pdf")) {
					LOG.info(host + url);
					LOG.info(e.text());
					process(dbconn, company, e.text(), host + url);
				}

			}
		}
	}

	@company("光大永明人寿保险有限公司")
	public static void save_GDYM(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sunlife-everbright.com/tabid/788/Default.aspx")
				.timeout(500000);
		conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection", "keep-alive");
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String url = e.attr("href");
				if (url.endsWith(".pdf")) {
					LOG.info(host + url);
					String[] arr = url.split("/");
					LOG.info(arr[arr.length - 1]);
					process(dbconn, company, arr[arr.length - 1], host + url);
				}
			}
		}
	}

	@company("国华人寿保险股份有限公司")
	public static void save_GHRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.95549.cn/pages/intro/xxpl04_detail02.shtml")
				.timeout(500000);
		conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection", "keep-alive");
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String url = e.attr("href");
				if (url.endsWith(".pdf")) {
					LOG.info(url);
					LOG.info(e.text());
					process(dbconn, company, e.text(), url);
				}
			}
		}
	}

	@company("合众人寿保险股份有限公司")
	public static void save_HZRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.unionlife.com.cn/unionlife/public/basic/hlwbxxx/hlwcpxx/index.html")
				.timeout(500000);
		conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.header("Connection", "keep-alive");
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String url = e.attr("href");
				if (url.endsWith(".pdf") && e.text().contains("条款")) {
					LOG.info(host + url);
					LOG.info(e.text());
					process(dbconn, company, e.text(), host + url);
				}
			}
		}
	}

	@company("和谐健康保险股份有限公司")
	public static void save_HXJK(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.hexiehealth.com/xxpl/jbxx/zscp/index.htm")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("tr");

			for (int i = 0; i < elements.size(); i++) {

				Element e = elements.get(i);
				Elements tds = e.getElementsByTag("td");
				if (i >= 1) {
					LOG.info(tds.get(1).text());
					LOG.info(host + tds.get(4).getElementsByTag("a").attr("href").substring(8));
					process(dbconn, company, tds.get(1).text(),
							host + tds.get(4).getElementsByTag("a").attr("href").substring(8));
				}
			}
		}
	}

	@company("恒安标准人寿保险有限公司")
	public static void save_HABZ(Connection dbconn, String company) {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://www.hengansl.com/cha/2304450.html");
		queue.offer("http://www.hengansl.com/cha/2304452.html");
		queue.offer("http://www.hengansl.com/cha/2304451.html");
		queue.offer("http://www.hengansl.com/cha/2304453.html");
		queue.offer("http://www.hengansl.com/cha/49308255.html");
		queue.offer("http://www.hengansl.com/cha/2304453.html");
		String s;
		while ((s = queue.poll()) != null) {

			org.jsoup.Connection conn = Jsoup.connect(s).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
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
						LOG.info(url);
						LOG.info(e.text());
						process(dbconn, company, e.text(), url);
					}
				}
			}
		}
	}

	@company("恒大人寿保险有限公司")
	public static void save_HDRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.lifeisgreat.com.cn/portal/info/getInfo.jspa?categoryId=1000&channelType=1")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
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
					LOG.info(url);
					LOG.info(e.text());
					process(dbconn, company, e.text(), url);
				}
			}
		}

	}

	@company("弘康人寿保险股份有限公司")
	public static void save_HKRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.hongkang-life.com/product/toShowContent.do?extends1=partnerInfo").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("tr");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (i >= 1) {
					if (e.text().contains("查看条款 ")) {
						LOG.info(e.select("td").get(0).text());
						LOG.info(host + e.getElementsByTag("a").first().attr("href"));
						process(dbconn, company, e.select("td").get(0).text(),
								host + e.getElementsByTag("a").first().attr("href"));

					}
				}
			}
		}
	}

	@company("华汇人寿保险股份有限公司")
	public static void save_HHRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.sciclife.com/base_survey/_content/13_05/02/1367479651970_1.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.endsWith(".pdf")) {
					LOG.info(e.text());
					LOG.info(host + href);
					process(dbconn, company, e.text(), host + href);
				}
			}

		}
	}

	@company("华夏人寿保险股份有限公司")
	public static void save_HXRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.hxlife.com/publish/main/1080/1081/1088/20121219100610665359475/index.html")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.select("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.attr("href").endsWith(".pdf") && e.text().equals("点击下载") == false) {
					LOG.info(e.text());
					LOG.info(host + e.attr("href"));
					process(dbconn, company, e.text(), host + e.attr("href"));
				}
			}
		}
	}

	@company("吉祥人寿保险股份有限公司")
	public static void save_JXRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.jxlife.com.cn/clauseManage/query.do").timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("tr");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (i >= 1) {
					LOG.info(e.select("td").get(1).text());
					LOG.info(e.getElementsByTag("a").first().attr("href"));
					process(dbconn, company, e.select("td").get(1).text(),
							e.getElementsByTag("a").first().attr("href"));
				}
			}
		}
	}

	@company("建信人寿保险有限公司")
	public static void save_JXRSBX(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.ccb-life.com.cn/gkxxpl/jbxx/gsgk/108.shtml?timestamp=1468821212049")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.endsWith(".pdf")) {
					if (href.startsWith(".."))
						href = host + href.substring(8);
					LOG.info(e.text());
					LOG.info(href);
					process(dbconn, company, e.text(), href);
				}
			}
		}
	}

	@company("交银康联人寿保险有限公司")
	public static void save_JYKL(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.bocommlife.com/sites/main/twainindex/xxpl.htm?columnid=297&page=1")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("tr");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (i >= 1 && e.select("td").get(0).text().matches("[0-9].*")) {
					LOG.info(e.select("td").get(1).text());
					LOG.info(host + e.getElementsByTag("a").first().attr("href"));
					process(dbconn, company, e.select("td").get(1).text(),
							host + e.getElementsByTag("a").first().attr("href"));
				}
			}
		}
	}

	@company("君康人寿保险股份有限公司")
	public static void save_JKRS(Connection dbconn, String company) {
		for (int j = 1; j < 30; j++) {
			org.jsoup.Connection conn = Jsoup.connect("http://www.zdlife.com/front/productItems.do?pageNo=" + j)
					.timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("tr");

				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (i >= 1 && e.select("td").get(0).text().contains("条款")) {
						LOG.info(e.select("td").get(0).text());
						LOG.info(host + "/front/" + e.getElementsByTag("a").first().attr("href"));
						process(dbconn, company, e.select("td").get(0).text(),
								host + "/front/" + e.getElementsByTag("a").first().attr("href"));
					}
				}
			}
		}
	}

	@company("君龙人寿保险有限公司")
	public static void save_JLRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.kdlins.com.cn/info!detail.action").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (e.text().contains("君龙") && href.endsWith(".pdf")) {
					LOG.info(e.text());
					LOG.info(host + "/" + href);
					process(dbconn, company, e.text(), host + "/" + href);
				}
			}
		}
	}

	@company("昆仑健康保险股份有限公司")
	public static void save_KLJK(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.kunlunhealth.com/templet/default/xxpl.jsp?id=1375")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains("/templet/default/ShowArticle_xxpl.jsp?id=")) {
					Elements as = null;
					;
					try {
						as = Jsoup.connect("http://" + host + href).get().getElementsByTag("a");
					} catch (IOException e1) {
						LOG.error(e1.getMessage() + conn.request().url().toString());
					}
					for (Element a : as) {
						if (a.attr("href").endsWith(".pdf")) {
							LOG.info(e.text());
							LOG.info(host + a.attr("href"));
							process(dbconn, company, e.text(), host + a.attr("href"));
						}
					}
				}
			}
		}
	}

	@company("利安人寿保险股份有限公司")
	public static void save_LARS(Connection dbconn, String company) {
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
		while ((url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.endsWith(".pdf")) {
						LOG.info(e.text());
						LOG.info(host + "/" + href.substring(12));
						process(dbconn, company, e.text(), host + "/" + href.substring(12));
					}
				}
			}
		}
	}

	@company("陆家嘴国泰人寿保险有限责任公司")
	public static void save_LJZGT(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("https://www.cathaylife.cn/publish/main/113/index.html")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {
			Elements elements = doc.getElementsByTag("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.endsWith(".pdf") && e.text().equals("点击查看") == false) {
					LOG.info(e.text());
					LOG.info(host + href);
					process(dbconn, company, e.text(), host + href);
				}
			}
		}
	}

	@company("民生人寿保险股份有限公司")
	public static void save_MSRS(Connection dbconn, String company) {
		for (int i = 1; i < 15; i++) {
			org.jsoup.Connection conn = Jsoup
					.connect("http://www.minshenglife.com/templet/default/ShowClass.jsp?id=991&pid=990&pn=" + i)
					.timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");

				for (int j = 0; j < elements.size(); j++) {
					Element e = elements.get(j);
					String href = e.attr("href");
					if (href.contains("/templet/default/ShowArticle.jsp")) {
						Elements as = null;
						;
						try {
							as = Jsoup.connect("http://" + host + href).get().getElementsByTag("a");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						for (Element a : as) {
							if (a.attr("href").endsWith(".doc") || a.attr("href").endsWith(".pdf")
									|| a.attr("href").endsWith(".docx")) {
								LOG.info(e.text());
								LOG.info(host + a.attr("href"));
								process(dbconn, company, e.text(), host + a.attr("href"));
								break;
							}
						}
					}
				}
			}
		}

		for (int i = 1; i < 15; i++) {
			org.jsoup.Connection conn = Jsoup
					.connect("http://www.minshenglife.com/templet/default/ShowClass.jsp?id=992&pid=990&pn=" + i)
					.timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");
				for (int j = 0; j < elements.size(); j++) {
					Element e = elements.get(j);
					String href = e.attr("href");
					if (href.contains("/templet/default/ShowArticle.jsp")) {
						Elements as = null;
						;
						try {
							as = Jsoup.connect("http://" + host + href).get().getElementsByTag("a");
						} catch (IOException e1) {
							LOG.error(e1.getMessage() + conn.request().url().toString());
							e1.printStackTrace();
						}
						for (Element a : as) {
							if (a.attr("href").endsWith(".doc") || a.attr("href").endsWith(".pdf")
									|| a.attr("href").endsWith(".docx")) {
								LOG.info(e.text());
								LOG.info(host + a.attr("href"));
								process(dbconn, company, e.text(), host + a.attr("href"));
								break;
							}
						}
					}
				}
			}
		}
	}

	@company("农银人寿保险股份有限公司")
	public static void save_NYRS(Connection dbconn, String company) {
		for (int j = 1; j < 15; j++) {
			org.jsoup.Connection conn = Jsoup.connect(
					"http://www.abchinalife.cn/cms-web/front/abchinalife/xxpl/jbxx/cpmljtk/zscpmljtk/zscpmljtk@" + j
							+ ".html")
					.timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;

			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf")) {
						LOG.info(e.text());
						LOG.info(host + e.attr("href"));
						process(dbconn, company, e.text(), host + e.attr("href"));
					}
				}
			}

		}

		for (int j = 1; j < 10; j++) {
			org.jsoup.Connection conn = Jsoup.connect(
					"http://www.abchinalife.cn/cms-web/front/abchinalife/xxpl/jbxx/cpmljtk/tscpmljtk/tscpmljtk@" + j
							+ ".html")
					.timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e1) {
				LOG.error(e1.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf")) {
						LOG.info(e.text());
						LOG.info(host + e.attr("href"));
						process(dbconn, company, e.text(), host + e.attr("href"));
					}
				}
			}
		}

	}

	@company("平安健康保险股份有限公司")
	public static void save_PAJK(Connection dbconn, String company) {
		for (int j = 1; j < 15; j++) {
			org.jsoup.Connection conn = Jsoup
					.connect("http://health.pingan.com/gongkaixinxipilu/baoxianchanpinmulujitiaokuan_" + j + ".shtml")
					.timeout(500000);
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.getElementsByTag("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf")) {
						LOG.info(e.text());
						LOG.info(e.attr("href"));
						// process(dbconn, company, e.text(), e.attr("href"));
					}
				}
			}
		}
	}

	@company("平安养老保险股份有限公司")
	public static void save_PAYN(Connection dbconn, String company) {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://paylx.pingan.cn/informationDisclosure/insuranceProductList.shtml");
		queue.offer("http://paylx.pingan.cn/informationDisclosure/insuranceProductList_2.shtml");
		queue.offer("http://paylx.pingan.cn/informationDisclosure/grwtcp/productRecordItemsList.shtml");
		String url;
		while ((url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.getElementsByTag("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf")) {
						LOG.info(e.text());
						LOG.info(e.attr("href"));
						process(dbconn, company, e.text(), e.attr("href"));
					}
				}
			}
		}
	}

	@company("前海人寿保险股份有限公司")
	public static void save_QHRS(Connection dbconn, String company) {
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/496/20150604143510770901389/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/497/20150609110608960839474/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/496/20150608101711743337460/index.html");
		queue.offer("http://www.foresealife.com/publish/main/xxpl/60/84/497/20140220160222580952616/index.html");
		String url;
		while ((url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.select("tr");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					Elements tds = e.getElementsByTag("td");
					if (i >= 1) {
						LOG.info(tds.get(0).text());
						LOG.info(host + tds.get(1).getElementsByTag("a").attr("href"));
						process(dbconn, company, tds.get(0).text(),
								host + tds.get(1).getElementsByTag("a").attr("href"));
					}
				}
			}
		}
	}

	@company("瑞泰人寿保险有限公司")
	public static void save_RTRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/")
				.timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.attr("href").endsWith(".pdf")) {
					LOG.info(e.text());
					LOG.info("http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/"
							+ e.attr("href"));
					process(dbconn, company, e.text(),
							"http://www.oldmutual-guodian.com/common/onlineService/download/prosuctClause/"
									+ e.attr("href"));
				}
			}
		}
	}

	@company("上海人寿保险股份有限公司")
	public static void save_SHRS(Connection dbconn, String company) {
		String url;
		for (int j = 1; j < 8; j++) {
			if (j == 1) {
				url = "http://www.shanghailife.com.cn/cpzx/cptk/zscp/";
			} else {
				url = "http://www.shanghailife.com.cn/cpzx/cptk/zscp/index_" + j + ".shtml";
			}
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".shtml")
							&& e.attr("href").startsWith("http://www.shanghailife.com.cn/cpzx/cptk/zscp/")) {
						Elements as = null;
						try {
							as = Jsoup.connect(e.attr("href")).get().select("a");
						} catch (IOException e1) {
							LOG.error(e1.getMessage() + conn.request().url().toString());
						}
						for (Element a : as) {
							if (a.text().equals("保险条款")) {
								LOG.info(e.text());
								LOG.info(a.attr("href"));
								process(dbconn, company, e.text(), a.attr("href"));
							}

						}
					}
				}
			}
		}
	}

	@company("太保安联健康保险股份有限公司")
	public static void save_TBAL(Connection dbconn, String company) {
		String url;
		for (int j = 1; j < 4; j++) {
			if (j == 1) {
				url = "http://health.cpic.com.cn/jkx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/index.shtml";
			} else {
				url = "http://health.cpic.com.cn/jkx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/index_" + j + ".shtml";
			}
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");

				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf") && e.text().equals("点击下载") == false) {
						LOG.info(e.text());
						LOG.info(host + e.attr("href"));
						process(dbconn, company, e.text(), host + e.attr("href"));
					}
				}
			}
		}
	}

	@company("太平人寿保险有限公司")
	public static void save_TPRS(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://life.cntaiping.com/info-bxcp/").timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");

			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.attr("href").endsWith(".pdf") && e.text().equals("点击下载") == false) {
					LOG.info(e.text());
					LOG.info(e.attr("href"));
					process(dbconn, company, e.text(), e.attr("href"));
				}
			}
		}
	}

	@company("太平养老保险股份有限公司")
	public static void save_TPYN(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://tppension.cntaiping.com/info-bxcp/").timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.attr("href").endsWith(".pdf") && e.text().equals("点击下载") == false) {
					LOG.info(e.text());
					LOG.info(e.attr("href"));
					process(dbconn, company, e.text(), e.attr("href"));
				}
			}
		}
	}

	@company("泰康人寿保险股份有限公司")
	public static void save_TKRS(Connection dbconn, String company) {
		Queue<String> queue = new LinkedList<String>();
		for (int i = 1; i < 15; i++) {
			queue.offer("http://www.tk.cn/publicinfo/464455/tab1189/889379/cdbb2a3b-" + i + ".shtml");
		}
		for (int i = 1; i < 4; i++) {
			queue.offer("http://www.tk.cn/publicinfo/464455/tab1189/889383/a2cd5173-" + i + ".shtml");
		}
		for (int i = 1; i < 6; i++) {
			queue.offer("http://www.tk.cn/publicinfo/464455/tab1189/889387/b5b664d1-" + i + ".shtml");
		}
		for (int i = 1; i < 6; i++) {
			queue.offer("http://www.tk.cn/publicinfo/464455/tab1189/889387/b5b664d1-" + i + ".shtml");
		}
		for (int i = 1; i < 6; i++) {
			queue.offer("http://www.tk.cn/publicinfo/464455/tab1189/889391/5d6db490-" + i + ".shtml");
		}
		String url;
		while ((url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.startsWith("/")) {
						href = host + href;
					}
					if (href.endsWith(".pdf") && e.text().equals("查看条款") == false && urls.contains(href) == false) {
						LOG.info(href);
						LOG.info(e.text());
						process(dbconn, company, e.text(), href);
						urls.add(href);
					}
				}
			}
		}
	}

	@company("泰康养老保险股份有限公司")
	public static void save_TKYL(Connection dbconn, String company) {
		String url;
		for (int j = 1; j < 15; j++) {
			if (j == 1) {
				url = "http://tkyl.pension.taikang.com/cms/static/xxpl/cpxxpl/list.html";
			} else {
				url = "http://tkyl.pension.taikang.com/cms/static/xxpl/cpxxpl/list_" + j + ".html";
			}
			org.jsoup.Connection conn = Jsoup.connect(url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.attr("href").endsWith(".pdf") && e.text().equals("点击下载") == false) {
						LOG.info(e.getElementsByTag("p").text());
						LOG.info(host + e.attr("href"));
						process(dbconn, company, e.getElementsByTag("p").text(), host + e.attr("href"));
					}
				}
			}
		}
	}

	@company("同方全球人寿保险有限公司")
	public static void save_TFQQ(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.aegonthtf.com/info/xxpl.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Element element1 = doc.getElementById("tk-cont1");
			Element element2 = doc.getElementById("tk-cont2");
			Elements elements1 = element1.getElementsByTag("tr");
			Elements elements2 = element2.getElementsByTag("tr");
			for (int i = 0; i < elements1.size(); i++) {
				Element e = elements1.get(i);
				if (i >= 1) {
					Elements tds = e.getElementsByTag("td");
					try {
						LOG.info(tds.get(0).text());
						LOG.info(host + "/info/" + tds.get(2).getElementsByTag("a").attr("href"));
						process(dbconn, company, tds.get(0).text(),
								host + "/info/" + tds.get(2).getElementsByTag("a").attr("href"));
					} catch (Exception e1) {
					}
				}
			}

			for (int i = 0; i < elements2.size(); i++) {
				Element e = elements2.get(i);
				if (i >= 1) {
					Elements tds = e.getElementsByTag("td");
					try {
						LOG.info(tds.get(0).text());
						LOG.info(host + "/info/" + tds.get(4).getElementsByTag("a").attr("href"));
						process(dbconn, company, tds.get(0).text(),
								host + "/info/" + tds.get(4).getElementsByTag("a").attr("href"));
					} catch (Exception e1) {
					}
				}
			}
		}

	}

	@company("珠江人寿保险股份有限公司")
	public static void saveZhuJiang(Connection dbconn, String company) {
		/**
		 * 在售保险产品条款目录 http://www.prlife.com.cn/page/message/base/company/detail/
		 * base_company_detail_002.shtml 停售保险产品条款目录
		 * http://www.prlife.com.cn/page/message/base/company/detail/
		 * base_company_detail_003.shtml
		 */
		for (int j = 2; j < 8; j++) {
			String href_url = "http://www.prlife.com.cn/page/message/base/company/detail/base_company_detail_00" + j
					+ ".shtml";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中邮人寿保险股份有限公司
	@company("中邮人寿保险股份有限公司")
	public static void saveZhongYou(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.chinapost-life.com/export/xxpl/jbxx/article_0003.html")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf")) {
					String url = host + href;
					String text = e.text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中英人寿保险有限公司
	@company("中英人寿保险有限公司")
	public static void saveZhongYing(Connection dbconn, String company) {
		/*
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-1.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-2.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-3.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-4.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-5.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-6.shtml
		 * http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/
		 * list-7.shtml
		 */
		for (int j = 1; j < 10; j++) {
			String href_url = "http://www.aviva-cofco.com.cn/website/xxzx/gkxxpl/gsjbxx/grbxtk/rsbx/list-" + j
					+ ".shtml";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("查看 >>") == false && e.text().contains("条款")) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中银三星人寿保险有限公司
	@company("中银三星人寿保险有限公司")
	public static void saveZhongYin(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.ssac.com.cn/chanpintiaokuan.html").timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf")) {
					String url = host + href;
					String text = e.text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中意人寿保险有限公司
	@company("中意人寿保险有限公司")
	public static void saveZhongYi(Connection dbconn, String company) {
		/*
		 * http://www.generalichina.com/dbx/index.jhtml
		 * http://www.generalichina.com/ylbx/index.jhtml
		 * http://www.generalichina.com/znbx/index.jhtml
		 * http://www.generalichina.com/txylbx/index.jhtml
		 * http://www.generalichina.com/chxxlc/index.jhtml
		 * http://www.generalichina.com/cxbx/index.jhtml
		 * http://www.generalichina.com/ywbz/index.jhtml
		 * http://www.generalichina.com/ts/index.jhtml
		 * http://www.generalichina.com/djtt/index.jhtml
		 * http://www.generalichina.com/yltj/index.jhtml
		 * http://www.generalichina.com/cxbxt/index.jhtml
		 * http://www.generalichina.com/znjybx/index.jhtml
		 * http://www.generalichina.com/ttywbzbx/index.jhtml
		 * http://www.generalichina.com/ttts/index.jhtml
		 */
		String str[] = { "dbx", "ylbx", "znbx", "txylbx", "chxxlc", "cxbx", "ywbz", "ts", "djtt", "yltj", "cxbxt",
				"znjybx", "ttywbzbx", "ttts" };
		for (int j = 0; j < str.length; j++) {
			String href_url = "http://www.generalichina.com/" + str[j] + "/index.jhtml";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("中意人寿企业宣传册") == false) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中邮人寿保险股份有限公司
	@company("中邮人寿保险股份有限公司")
	public static void saveZhongMei(Connection dbconn, String company) {
		/**
		 * https://www.metlife.com.cn/about-us/disclosures/public/basicinfo/
		 * productlist1.html
		 * https://www.metlife.com.cn/about-us/disclosures/public/basicinfo/
		 * productlist2.html
		 * https://www.metlife.com.cn/about-us/disclosures/public/basicinfo/
		 * productlist3.html
		 */
		for (int j = 1; j < 8; j++) {
			String href_url = "https://www.metlife.com.cn/about-us/disclosures/public/basicinfo/productlist" + j
					+ ".html";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("理赔申请书") == false
							&& e.text().equals("保险合同变更申请书") == false && e.text().equals("都会贵宾亲属信息登记表") == false) {
						String url = host + href;
						String text = e.attr("title");
						if (text.contains("su")) {
							text = e.text();
						}
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中华联合人寿保险股份有限公司
	@company("中华联合人寿保险股份有限公司")
	public static void saveZhongHua(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://life.cic.cn/mall/html.action?action=service&viewName=views/infodisclosure/internetMsg")
				.timeout(500000);
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf") && e.text().equals("查看产品条款") == true) {
					String url = href;
					String text = e.parent().parent().child(1).text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中宏人寿保险有限公司
	@company("中宏人寿保险有限公司")
	public static void saveZhongHong(Connection dbconn, String company) {
		/**
		 * 容易超时 http://www.manulife-sinochem.com/info/gscp.html
		 * http://www.manulife-sinochem.com/info/txcp.html
		 * http://www.manulife-sinochem.com/info/dxcp.html
		 * http://www.manulife-sinochem.com/info/wxcp.html
		 */
		String str[] = { "gscp", "txcp", "dxcp", "wxcp" };
		for (int j = 0; j < str.length; j++) {
			String href_url = "http://www.manulife-sinochem.com/info/" + str[j] + ".html";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				int h = 0;
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String text = "";
						String url = "";
						if (str[j].equals("wxcp") == true) {
							if (e.text().equals("条款") == true) {
								url = host + "/info/" + href;
								text = e.parent().parent().child(4 * h).text();
								h++;
							}
						} else {
							url = host + "/info/" + href;
							text = e.text();
						}
						if (url.equals("") == false && text.equals("") == false) {
							LOG.info(url);
							LOG.info(text);
							process(dbconn, company, text, url);
						}
					}
				}
			}
		}
	}

	// 中荷人寿保险有限公司
	@company("中荷人寿保险有限公司")
	public static void saveZhongHe(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup
				.connect("http://www.bob-cardif.com/xinxipilu/jibenxinxi/gongsigaikuang/100000348849.html")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf") && e.text().contains("险")) {
					String url = host + href;
					String text = e.text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中国人寿保险股份有限公司
	@company("中国人寿保险股份有限公司")
	public static void saveGuoShou(Connection dbconn, String company) {
		/**
		 * 在售 总共31页 1 2 3 4 ... 30 31
		 */
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/zaishoubaoxianchanpin.htm&curtPage=1
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/zaishoubaoxianchanpin.htm&curtPage=2
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/zaishoubaoxianchanpin.htm&curtPage=3
		// ...
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/zaishoubaoxianchanpin.htm&curtPage=31
		/**
		 * 停售 总共7页 1 2 3 ... 6 7
		 */
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/tingbanbaoxianchanpin.html&curtPage=1
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/tingbanbaoxianchanpin.html&curtPage=2
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/tingbanbaoxianchanpin.html&curtPage=3
		// ...
		// http://www.e-chinalife.com/help-center/xiazaizhuanqu/tingbanbaoxianchanpin.html&curtPage=7
		Queue<String> queue = new LinkedList<String>();
		for (int j = 1; j < 38; j++) {
			queue.offer("http://www.e-chinalife.com/help-center/xiazaizhuanqu/zaishoubaoxianchanpin.htm&curtPage=" + j);
		}
		for (int M = 1; M < 10; M++) {
			queue.offer(
					"http://www.e-chinalife.com/help-center/xiazaizhuanqu/tingbanbaoxianchanpin.html&curtPage=" + M);
		}
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("点击下载") == false) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中国人民人寿保险股份有限公司
	@company("中国人民人寿保险股份有限公司")
	public static void saveRenBao(Connection dbconn, String company) {
		/**
		 * 在售产品条款-个险-长期险 总共9页
		 * http://www.picclife.com/IndividualLongrisk/index_1.jhtml
		 * http://www.picclife.com/IndividualLongrisk/index_2.jhtml
		 * http://www.picclife.com/IndividualLongrisk/index_3.jhtml ...
		 * http://www.picclife.com/IndividualLongrisk/index_9.jhtml
		 * 
		 * 在售产品条款-个险-短期险 总共2页
		 * http://www.picclife.com/IndividualShortrisk/index_1.jhtml
		 * http://www.picclife.com/IndividualShortrisk/index_2.jhtml
		 * 
		 * 在售产品条款-团险-长期险 总共5页
		 * http://www.picclife.com/groupLongrisk/index_1.jhtml
		 * http://www.picclife.com/groupLongrisk/index_2.jhtml ...
		 * http://www.picclife.com/groupLongrisk/index_5.jhtml
		 * 
		 * 在售产品条款-团险-短期险 总共8页
		 * http://www.picclife.com/groupShortrisk/index_1.jhtml
		 * http://www.picclife.com/groupShortrisk/index_2.jhtml ...
		 * http://www.picclife.com/groupShortrisk/index_8.jhtml
		 * 
		 * 在售产品条款-银保-长期险 总共4页 http://www.picclife.com/bankLongrisk/index_1.jhtml
		 * http://www.picclife.com/bankLongrisk/index_2.jhtml ...
		 * http://www.picclife.com/bankLongrisk/index_4.jhtml
		 * 
		 * 在售产品条款-互动-长期险 总共1页
		 * http://www.picclife.com/interactiveLongrisk/index.jhtml
		 * 
		 * 在售产品条款-新渠道-长期险 总共2页
		 * http://www.picclife.com/newApproachLongrisk/index_1.jhtml
		 * http://www.picclife.com/newApproachLongrisk/index_2.jhtml
		 * 
		 * 已停售产品条款 总共15页 http://www.picclife.com/ytscptk/index_1.jhtml
		 * http://www.picclife.com/ytscptk/index_2.jhtml ...
		 * http://www.picclife.com/ytscptk/index_15.jhtml
		 * 
		 */
		Queue<String> queue = new LinkedList<String>();
		for (int j = 1; j < 15; j++) {
			queue.offer("http://www.picclife.com/IndividualLongrisk/index_" + j + ".jhtml");
		}
		for (int M = 1; M < 6; M++) {
			queue.offer("http://www.picclife.com/IndividualShortrisk/index_" + M + ".jhtml");
		}
		for (int j = 1; j < 10; j++) {
			queue.offer("http://www.picclife.com/groupLongrisk/index_" + j + ".jhtml");
		}
		for (int M = 1; M < 15; M++) {
			queue.offer("http://www.picclife.com/groupShortrisk/index_" + M + ".jhtml");
		}
		for (int j = 1; j < 10; j++) {
			queue.offer("http://www.picclife.com/bankLongrisk/index_" + j + ".jhtml");
		}
		queue.offer("http://www.picclife.com/interactiveLongrisk/index.jhtml");
		for (int j = 1; j < 6; j++) {
			queue.offer("http://www.picclife.com/newApproachLongrisk/index_" + j + ".jhtml");
		}
		for (int M = 1; M < 20; M++) {
			queue.offer("http://www.picclife.com/ytscptk/index_" + M + ".jhtml");
		}
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中国人民健康保险股份有限公司
	@company("中国人民健康保险股份有限公司")
	public static void saveJianKang(Connection dbconn, String company) {
		/**
		 * 在售保险产品条款目录 http://www.picchealth.com/tabid/2318/Default.aspx
		 * 停售保险产品条款目录 http://www.picchealth.com/tabid/2319/Default.aspx
		 */
		String str[] = { "2318", "2319" };
		for (int j = 0; j < str.length; j++) {
			String href_url = "http://www.picchealth.com/tabid/" + str[j] + "/Default.aspx";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 长城人寿保险股份有限公司
	@company("长城人寿保险股份有限公司")
	public static void saveChangCheng(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.greatlife.cn/page/xxpl/hlwxx//cpgk.shtml")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf") && e.text().contains("险")) {
					String url = host + href;
					String text = e.text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 友邦保险有限公司上海分公司（代表友邦在华各分支公司）
	@company("友邦保险有限公司")
	public static void saveYouBang(Connection dbconn, String company) {
		/**
		 * 个人寿险
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/geren
		 * -shouxian.html 个人意外伤害
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/geren
		 * -yiwai.html 个人健康险
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/geren
		 * -jiankang.html 个人年金保险
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/geren
		 * -nianjin.html 团体人寿保险
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/
		 * tuanxian-shouxian.html 团体意外伤害
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/
		 * tuanxian-yiwai.html 团体健康险
		 * http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/
		 * tuanxian-jiankang.html
		 */
		String str[] = { "geren-shouxian", "geren-yiwai", "geren-jiankang", "geren-nianjin", "tuanxian-shouxian",
				"tuanxian-yiwai", "tuanxian-jiankang" };
		for (int j = 0; j < str.length; j++) {
			String href_url = "http://www.aia.com.cn/zh-cn/aia/media/gongkaixinxipilou/zaishou/" + str[j] + ".html";
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String url = host + href;
						String text = e.parent().parent().child(1).text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 中德安联人寿保险有限公司
	@company("中德安联人寿保险有限公司")
	public static void saveZhongDe(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.allianz.com.cn/action/loadproductcategory.php")
				.timeout(500000);
				/**
				 * 在售产品
				 */
		// conn.data("useraction","GetONSALE");
		/**
		 * 停售产品
		 */
		// conn.data("useraction","GetOFFSALE");
		String str[] = { "GetONSALE", "GetOFFSALE" };
		for (int j = 0; j < str.length; j++) {
			conn.data("useraction", str[j]);
			String host = conn.request().url().getHost();
			Document doc = null;
			;
			try {
				doc = conn.post();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf")) {
						String url = host + "/" + href;
						String text = e.parent().parent().child(0).text().split(" ")[1];
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 阳光人寿保险股份有限公司
	@company("阳光人寿保险股份有限公司")
	public static void saveYangGuang(Connection dbconn, String company) {
		/**
		 * 个人在售 总共22页
		 * http://wecare.sinosig.com/common/customerservice/html/grzscp.shtml
		 * http://wecare.sinosig.com/common/customerservice/html/grzscp_2.shtml
		 * http://wecare.sinosig.com/common/customerservice/html/grzscp_3.shtml
		 * ...
		 * http://wecare.sinosig.com/common/customerservice/html/grzscp_22.shtml
		 * 
		 * 个人停售 总共5页
		 * http://www.sinosig.com/common/customerservice/html/grtscp.shtml
		 * http://www.sinosig.com/common/customerservice/html/grtscp_2.shtml ...
		 * http://www.sinosig.com/common/customerservice/html/grtscp_5.shtml
		 * 
		 * 团体在售 总共4页
		 * http://wecare.sinosig.com/common/customerservice/html/ttzscp.shtml
		 * http://wecare.sinosig.com/common/customerservice/html/ttzscp_2.shtml
		 * ...
		 * http://wecare.sinosig.com/common/customerservice/html/ttzscp_4.shtml
		 * 
		 * 团体停售 总共2页
		 * http://www.sinosig.com/common/customerservice/html/tttscp.shtml
		 * http://www.sinosig.com/common/customerservice/html/tttscp_2.shtml
		 */
		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://wecare.sinosig.com/common/customerservice/html/grzscp.shtml");
		queue.offer("http://www.sinosig.com/common/customerservice/html/grtscp.shtml");
		queue.offer("http://wecare.sinosig.com/common/customerservice/html/ttzscp.shtml");
		queue.offer("http://www.sinosig.com/common/customerservice/html/tttscp.shtml");
		queue.offer("http://www.sinosig.com/common/customerservice/html/tttscp_2.shtml");
		for (int j = 2; j < 26; j++) {
			queue.offer("http://wecare.sinosig.com/common/customerservice/html/grzscp_" + j + ".shtml");
		}
		for (int M = 2; M < 10; M++) {
			queue.offer("http://www.sinosig.com/common/customerservice/html/grtscp_" + M + ".shtml");
		}
		for (int n = 2; n < 10; n++) {
			queue.offer("http://wecare.sinosig.com/common/customerservice/html/ttzscp_" + n + ".shtml");
		}
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("查看条款") == false) {
						String url = "";
						if (href.contains("=") && href.contains("&")) {
							href = href.split("=")[2];
						}
						if (href.charAt(0) == '/') {
							url = host + href;
						} else {
							url = href;
						}
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}
				}
			}
		}
	}

	// 招商信诺人寿保险有限公司
	@company("招商信诺人寿保险有限公司")
	public static void saveZhaoShang(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.cignacmb.com/xinxi/jibenxinxi/chanpin.html")
				.timeout(5000000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf") && e.text().contains("版本")) {
					String url = host + href;
					String text = e.parent().child(1).text().split("：")[0] + "(" + e.text() + ")";
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中国平安人寿保险股份有限公司
	@company("中国平安人寿保险股份有限公司")
	public static void savePingAn(Connection dbconn, String company) {
		/**
		 * 新增产品
		 * http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?
		 * requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.
		 * queryPlanClause&SALES_STATUS=01
		 */
		/**
		 * 在售产品
		 * http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?
		 * requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.
		 * queryPlanClause&SALES_STATUS=02
		 */
		/**
		 * 团险 http://life.pingan.com/gongkaixinxipilu/zaishou_tuanxian.xml
		 */
		/**
		 * 停办 http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?
		 * requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.
		 * queryPlanClause&SALES_STATUS=03 团险
		 * http://life.pingan.com/gongkaixinxipilu/tingshou_tuanxian.xml
		 */
		Queue<String> queue = new LinkedList<String>();
		queue.offer(
				"http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.queryPlanClause&SALES_STATUS=01");
		queue.offer(
				"http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.queryPlanClause&SALES_STATUS=02");
		queue.offer("http://life.pingan.com/gongkaixinxipilu/zaishou_tuanxian.xml");
		queue.offer(
				"http://life.pingan.com/life_insurance/elis.pa18.commonQuery.visit?requestid=com.palic.elis.pos.intf.biz.action.PosQueryAction.queryPlanClause&SALES_STATUS=03");
		queue.offer("http://life.pingan.com/gongkaixinxipilu/tingshou_tuanxian.xml");
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000);
			String host = conn.request().url().getHost();
			Document doc = null;
			;
			try {
				doc = conn.post();
			} catch (IOException e1) {
				LOG.error(e1.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.select("map");
				String pdfOpenUrl = "/life_insurance/elis.intf.queryClauseContent.visit?VERSION_NO=";
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String url = "";
					if (e.select("link_url").text() != null && e.select("link_url").text().equals("") == false) {
						url = e.select("link_url").text();
					} else {
						url = host + pdfOpenUrl + e.select("version_no").text();
					}
					String text = e.select("clause_name").text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 中国太平洋人寿保险股份有限公司
	@company("中国太平洋人寿保险股份有限公司")
	public static void saveTaiPingYang(Connection dbconn, String company) {
		/**
		 * 在办产品 寿险 总共11页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/
		 * index.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/
		 * index_2.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/
		 * index_3.shtml 。。。
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/
		 * index_11.shtml
		 * 
		 * 年金 总共4页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/
		 * index.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/
		 * index_2.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/
		 * index_3.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/
		 * index_4.shtml
		 * 
		 * 意外险 总共6页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/ywx/
		 * index.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/ywx/
		 * index_2.shtml ...
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/ywx/
		 * index_6.shtml
		 * 
		 * 健康险 总共13页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/jkx/
		 * index.shtml ...
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/jkx/
		 * index_12.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/jkx/
		 * index_13.shtml
		 * 
		 * 停办产品 寿险 总共5页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/sx/
		 * index.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/sx/
		 * index_2.shtml 。。。
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/sx/
		 * index_5.shtml
		 * 
		 * 年金 总共3页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/nj/
		 * index.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/nj/
		 * index_2.shtml
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/nj/
		 * index_2.shtml
		 * 
		 * 意外险 总共1页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/ywx/
		 * index.shtml
		 * 
		 * 健康险 总共1页
		 * http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/jkx/
		 * index.shtml
		 */

		Queue<String> queue = new LinkedList<String>();
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/index.shtml");
		for (int j = 2; j < 15; j++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/sx/index_" + j + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/index.shtml");
		for (int m = 2; m < 8; m++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/nj/index_" + m + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/ywx/index.shtml");
		for (int j = 2; j < 10; j++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/ywx/index_" + j + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/jkx/index.shtml");
		for (int m = 2; m < 17; m++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/zbcp/jkx/index_" + m + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/sx/index.shtml");
		for (int j = 2; j < 9; j++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/sx/index_" + j + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/nj/index.shtml");
		for (int m = 2; m < 7; m++) {
			queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/nj/index_" + m + ".shtml");
		}
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/ywx/index.shtml");
		queue.offer("http://life.cpic.com.cn/xrsbx/gkxxpl/jbxx/gsgk/jydbxcpmljtk/tbcp/jkx/index.shtml");
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(5000000);
			String host = conn.request().url().getHost();
			Document doc = null;
			;
			try {
				doc = conn.post();
			} catch (IOException e1) {
				LOG.error(e1.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {
				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					String href = e.attr("href");
					if (href.contains(".pdf") && e.text().equals("点击下载") == false) {
						String url = host + href;
						String text = e.text();
						LOG.info(url);
						LOG.info(text);
						process(dbconn, company, text, url);
					}

				}
			}
		}
	}

	// 长生人寿保险有限公司
	@company("长生人寿保险有限公司")
	public static void saveChangSheng(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.nissay-greatwall.com.cn/public/public-info_08.html")
				.timeout(5000000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("span");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.text().equals("在售产品") == true) {
					Element tr = e.parent().parent(); // tr
					String Sname = tr.child(1).text(); // 公司名称
					String str[] = Sname.split(" ");
					int m = 0;
					Elements el = tr.select("a");
					for (int j = 0; j < el.size(); j++) {
						Element com = el.get(j);
						String href = com.attr("href");
						if (href.contains(".pdf") && com.text().equals("条款") == true) {
							String url = host + href.substring(2);
							String text = str[m++];
							LOG.info(url);
							LOG.info(text);
							process(dbconn, company, text, url);
						}
					}

				}
			}
		}
	}

	// 中法人寿保险有限责任公司
	@company("中法人寿保险有限责任公司")
	public static void savezhongFa(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.sfli.com.cn/cms-web/front/sinofrench/cpzx/cpzx@1.html")
				.timeout(500000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("a");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				String href = e.attr("href");
				if (e.text().contains("保险")) {
					String url = host + href;
					String text = e.text();
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}

	// 英大泰和人寿保险股份有限公司
	@company("英大泰和人寿保险股份有限公司")
	public static void saveYingDa(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.ydthlife.com/ecydth/infoexp/productdetail/index.jsp")
				.timeout(50000000);
		String host = conn.request().url().getHost();
		Document doc = null;
		try {
			doc = conn.get();
		} catch (Exception e) {
			LOG.error(e.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select(".yd_t_label1");
			for (int i = 0; i < elements.size(); i++) {
				Element e = elements.get(i);
				if (e.text().contains("经营的保险产品目录及条款")) {
					Element docs = e.parent().parent().child(2);
					Elements elements2 = docs.select("a");
					for (int j = 0; j < elements2.size(); j++) {
						Element e2 = elements2.get(j);
						String href = e2.attr("href");
						Elements as = null;
						;
						try {
							as = Jsoup.connect("http://" + host + "/ecydth/infoexp/productdetail/" + href)
									.timeout(5000000).get().getElementsByTag("a");
						} catch (IOException e1) {
							LOG.error(e1.getMessage() + conn.request().url().toString());
							e1.printStackTrace();
						}
						for (Element a : as) {
							if (a.attr("href").endsWith(".pdf")) {
								// LOG.info(a.text());
								// LOG.info(host+"/ecydth/infoexp/productdetail/zsjkbx/"+a.attr("href"));
								String url = host + "/ecydth/infoexp/productdetail/zsjkbx/" + a.attr("href");
								String text = e2.text();
								LOG.info(url);
								LOG.info(text);
								process(dbconn, company, text, url);
							}
						}
					}
				}
			}
		}
	}

	// 新华人寿保险股份有限公司
	@company("新华人寿保险股份有限公司")
	public static void saveXinHua(Connection dbconn, String company) {
		/**
		 *
		 * 年金险 总共3页 http://www.newchinalife.com/Channel/1320989?_tp_xinxilb=1
		 * http://www.newchinalife.com/Channel/1320989?_tp_xinxilb=2
		 * http://www.newchinalife.com/Channel/1320989?_tp_xinxilb=3 健康险 总共17页
		 * http://www.newchinalife.com/Channel/805926?_tp_xinxilb=1
		 * http://www.newchinalife.com/Channel/805926?_tp_xinxilb=10 ...
		 * http://www.newchinalife.com/Channel/805926?_tp_xinxilb=17 人寿险 总共15页
		 * http://www.newchinalife.com/Channel/801775?_tp_xinxilb=1
		 * http://www.newchinalife.com/Channel/801775?_tp_xinxilb=2 ...
		 * http://www.newchinalife.com/Channel/801775?_tp_xinxilb=15
		 * 
		 * 意外险 总共6页 http://www.newchinalife.com/Channel/800114?_tp_xinxilb=1 ...
		 * http://www.newchinalife.com/Channel/800114?_tp_xinxilb=6 健康保障委托管理业务
		 * 1页 http://www.newchinalife.com/Channel/799947
		 * 
		 */
		Queue<String> queue = new LinkedList<String>();
		for (int j = 1; j < 7; j++) {
			queue.offer("http://www.newchinalife.com/Channel/1320989?_tp_xinxilb=" + j);
		}
		for (int m = 1; m < 21; m++) {
			queue.offer("http://www.newchinalife.com/Channel/805926?_tp_xinxilb=" + m);
		}
		for (int j = 1; j < 19; j++) {
			queue.offer("http://www.newchinalife.com/Channel/801775?_tp_xinxilb=" + j);
		}
		for (int m = 1; m < 10; m++) {
			queue.offer("http://www.newchinalife.com/Channel/800114?_tp_xinxilb=" + m);
		}
		queue.offer("http://www.newchinalife.com/Channel/799947");
		String href_url;
		while ((href_url = queue.poll()) != null) {
			org.jsoup.Connection conn = Jsoup.connect(href_url).timeout(500000000);
			String host = conn.request().url().getHost();
			Document doc = null;
			try {
				doc = conn.get();
			} catch (Exception e) {
				LOG.error(e.getMessage() + conn.request().url().toString());
			}
			if (doc != null) {

				Elements elements = doc.select("a");
				for (int i = 0; i < elements.size(); i++) {
					Element e = elements.get(i);
					if (e.text().equals("条款") == true) {
						String href = host + e.attr("href");
						Elements as = null;
						try {
							as = Jsoup.connect("http://" + href).timeout(500000000).get().getElementsByTag("a");
						} catch (IOException e1) {
							LOG.error(e1.getMessage() + conn.request().url().toString());
						}
						for (Element a : as) {
							if (a.text().endsWith(".pdf") || a.text().endsWith(".doc") || a.text().endsWith(".dot")) {
								String url = host + a.attr("href");
								String text = a.text();
								LOG.info(url);
								LOG.info(text);
								process(dbconn, company, text, url);
							}
						}
					}
				}
			}
		}
	}

	// 信泰人寿保险股份有限公司
	@company("信泰人寿保险股份有限公司")
	public static void saveXinTai(Connection dbconn, String company) {
		org.jsoup.Connection conn = Jsoup.connect("http://www.xintai.com/organization/findProducts.do").timeout(500000);
		conn.data("key", "");
		// String host=conn.request().url().getHost();
		Document doc = null;
		;
		try {
			doc = conn.post();
		} catch (IOException e1) {
			LOG.error(e1.getMessage() + conn.request().url().toString());
		}
		if (doc != null) {

			Elements elements = doc.select("body");
			Element json = elements.get(0);
			Elements j = json.children();
			Elements a = j.select("a");
			for (int i = 0; i < a.size(); i++) {
				Element e = a.get(i);
				String href = e.attr("href");
				if (href.contains(".pdf")) {
					String hrefs = href.split("\"")[1];
					String url = hrefs.split(".pdf")[0] + ".pdf";
					String text = e.text().split("<")[0];
					LOG.info(url);
					LOG.info(text);
					process(dbconn, company, text, url);
				}
			}
		}
	}
}
