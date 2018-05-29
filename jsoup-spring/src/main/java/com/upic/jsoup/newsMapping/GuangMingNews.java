package com.upic.jsoup.newsMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.upic.jsoup.po.News;

/**
 * 光明网是需要爬取页面解析list 时政：http://politics.gmw.cn/node_9840.htm
 * 教育：http://edu.gmw.cn/node_10810.htm
 * @author dtz
 *
 */
@Component
public class GuangMingNews extends AbstractJsoupNewsMapping {

	@Override
	protected String getNewtext(Document doc) {
		// 获取新闻内容
		Element elementById = doc.getElementById("contentMain") == null ? doc.getElementById("ArticleContent")
				: doc.getElementById("contentMain");
		String text = elementById.text();
		return text;
	}

	@Override
	protected String getPubTime(Document doc) {
		// 获取新闻发布的时间
		Element elementsByClass = doc.getElementById("pubTime");
		String text = elementsByClass.text();
		return text;
	}

	@Override
	protected String getSource(Document doc) {
		Element elementsByClass = doc.getElementById("source");
		String text = elementsByClass.text();
		return text;
	}

	@Override
	protected boolean isWebGet() {
		return true;
	}

	/**
	 * 需要网上爬取
	 */
	@Override
	protected List<News> getListNews0(Document document) {
		String url=document.baseUri()+"/";
		Elements elementsByClass = document.getElementsByClass("channel-newsGroup");
		List<News> listNews = new ArrayList<News>();
		elementsByClass.forEach(x -> {
			Elements select = x.select("a");
			select.forEach(el -> {
				String select2 = el.attr("href");
				try {
					String urls =select2.startsWith("http")?select2:url+select2;
					listNews.add(selectAllNews(urls));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
		return listNews;
	}

	private News selectAllNews(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		//由于内容庞大 先不抓取
		return new News(url,doc.title(), getPubTime(doc),null, getSource(doc), getImgurl(doc));
	}

	public static void main(String[] args) throws IOException {
		GuangMingNews g = new GuangMingNews();
		List<News> listNews = g.getListNews("http://edu.gmw.cn/node_10810.htm");
		listNews.forEach(System.out::println);
	}
}
