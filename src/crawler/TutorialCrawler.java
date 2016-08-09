package crawler;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.FileUtils;

public class TutorialCrawler extends BreadthCrawler {

	
    //用于保存图片的文件夹
    File downloadDir;

    //原子性int，用于生成图片文件名
    AtomicInteger imageId;
    
    public TutorialCrawler(String crawlPath, String downloadPath) {
        super(crawlPath, true);
        downloadDir = new File(downloadPath);
        if(!downloadDir.exists()){
            downloadDir.mkdirs();
        }
        computeImageId();
    }

    /*
        可以往next中添加希望后续爬取的任务，任务可以是URL或者CrawlDatum
        爬虫不会重复爬取任务，从2.20版之后，爬虫根据CrawlDatum的key去重，而不是URL
        因此如果希望重复爬取某个URL，只要将CrawlDatum的key设置为一个历史中不存在的值即可
        例如增量爬取，可以使用 爬取时间+URL作为key。

        新版本中，可以直接通过 page.select(css选择器)方法来抽取网页中的信息，等价于
        page.getDoc().select(css选择器)方法，page.getDoc()获取到的是Jsoup中的
        Document对象，细节请参考Jsoup教程
    */
    @Override
    public void visit(Page page, CrawlDatums next) {
    	 String contentType = page.getResponse().getContentType();
         if(contentType==null){
             return;
         }else if (contentType.contains("html")) {
             //如果是网页，则抽取其中包含图片的URL，放入后续任务
             Elements imgs = page.select("a[src]");
             for (Element img : imgs) {
                 String imgSrc = img.attr("abs:src");
                 next.add(imgSrc);
             }

         } else if (contentType.startsWith("application/pdf")) {
             //如果是图片，直接下载
             String extensionName=contentType.split("/")[1];
             String imageFileName=imageId.incrementAndGet()+"."+extensionName;
             File imageFile=new File(downloadDir,imageFileName);
             try {
//             	if(page.getContent().length>50*1024){	}
                     FileUtils.writeFile(imageFile, page.getContent());
                     System.out.println("保存文件 "+page.getUrl()+" 到 "+imageFile.getAbsolutePath());
             } catch (IOException ex) {
                 throw new RuntimeException(ex);
             }
         }
    }

    public static void main(String[] args) throws Exception {
        TutorialCrawler crawler = new TutorialCrawler("crawler", "download");
        crawler.addSeed("http://www.anbang-life.com/gkxxpl/jbxx/zscp/index.htm");
        crawler.addRegex("http://www.anbang-life.com/docs/xxpl_zscp_20160425/.*.pdf");
        /*可以设置每个线程visit的间隔，这里是毫秒*/
        //crawler.setVisitInterval(1000);
        /*可以设置http请求重试的间隔，这里是毫秒*/
        //crawler.setRetryInterval(1000);

        crawler.setThreads(30);
        crawler.start(2);
    }
    
    public void computeImageId(){
        int maxId=-1;
        for(File imageFile:downloadDir.listFiles()){
            String fileName=imageFile.getName();
            String idStr=fileName.split("\\.")[0];
            int id=Integer.valueOf(idStr);
            if(id>maxId){
                maxId=id;
            }
        }
        imageId=new AtomicInteger(maxId);
    }

}