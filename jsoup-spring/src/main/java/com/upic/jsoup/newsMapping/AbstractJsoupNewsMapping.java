package com.upic.jsoup.newsMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.upic.jsoup.po.News;
/**
 * 
 * @author dtz
 *
 */
@SuppressWarnings("all")
public abstract class AbstractJsoupNewsMapping {

	private LinkedList ImgUrls = new LinkedList();// 用于存放图片URL

	protected Document getDocByUrl(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			return null;
		}
	}

	public News getNewByurl(String url) {
		Document doc = getDocByUrl(url);
		if (doc == null) {
			return null;
		}
		return getNew(doc);
	}

	private News getNew(Document doc) {
		return new News(doc.baseUri(),doc.title(), getPubTime(doc), getNewtext(doc), getSource(doc), getImgurl(doc));
	}

	/**
	 * 正文
	 * 
	 * @param doc
	 * @return
	 */
	protected abstract String getNewtext(Document doc);

	/**
	 * 时间
	 * 
	 * @param doc
	 * @return
	 */
	protected abstract String getPubTime(Document doc);

	/**
	 * 图片
	 * 
	 * @param doc
	 * @return
	 */
	protected List<String> getImgurl(Document doc) {
		// 获取图片地址，并保存在ImgUrls链表中
		int count = 0;
		Elements pngs = doc.select("img[src]");
		for (Element img : pngs) {
			count++;
			String imgUrl = img.attr("abs:src");// 获取图片的绝对路径
			// imgUrl = img.absUrl("src");
			ImgUrls.add(imgUrl);
			// System.out.println(imgUrl);
			// downloadImg(imgUrl, count);
		}
		return ImgUrls;
	}

	/**
	 * 获得来源
	 * 
	 * @param doc
	 * @return
	 */
	protected abstract String getSource(Document doc);

	/**
	 * 是否为页面获取
	 */
	protected abstract boolean isWebGet();

	/**
	 * 如果需要根据页面爬取List<News>
	 * @throws IOException 
	 */
	public  List<News> getListNews(String url) throws IOException{
		if(!isWebGet()) {
			return null;
		}
		Document document = Jsoup.connect(url).get();
		return getListNews0(document);
	}
	
	protected abstract List<News> getListNews0(Document document);
}
