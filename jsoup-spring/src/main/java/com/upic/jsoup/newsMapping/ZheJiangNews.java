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
 * 浙江新闻网
 * 时政：http://china.zjol.com.cn/gnxw/
 * 经济：http://fin.zjol.com.cn/
 * 人事：最新任免 http://zjnews.zjol.com.cn/rsdt/zxrm/
 * @author dtz
 *
 */
@Component
public class ZheJiangNews extends AbstractJsoupNewsMapping {

	@Override
	protected String getNewtext(Document doc) {
		// 获取新闻内容
		Elements elementById = doc.getElementsByClass("contTxt");
		String text = elementById.text();
		return text;
	}

	@Override
	protected String getPubTime(Document doc) {
		// 获取新闻发布的时间
		Element elementsByClass = doc.getElementById("pubtime_baidu")==null?doc.getElementsByClass("time").first():doc.getElementById("pubtime_baidu");
		String text = elementsByClass.text();
		return text;
	}

	@Override
	protected String getSource(Document doc) {
		Element elementsByClass = doc.getElementById("source_baidu")==null?doc.getElementsByClass("time").first():doc.getElementById("source_baidu");
		String text = elementsByClass.text();
		return text;
	}

	@Override
	protected boolean isWebGet() {
		return true;
	}

	@Override
	protected List<News> getListNews0(Document document) {
		Elements elementsByClass = document.getElementsByClass("listLi");
		List<News> newsList=new ArrayList<News>();
		String baseUri=document.baseUri();
		elementsByClass.forEach(x -> {
			Elements select = x.select("a");
			String select2 = select.attr("href");
			select2=dealwithUrl(select2,baseUri);
			try {
				newsList.add(selectAllNews(select2));
			} catch (IOException e) {
			}
		});
		return newsList;
	}
	private String dealwithUrl(String select2,String baseUrl) {
		if(!select2.startsWith("http")) {
			select2=baseUrl+select2.substring(2, select2.length());
		}
		return select2;
	}

	private News selectAllNews(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		//由于内容庞大 先不抓取
		return new News(url,doc.title(), getPubTime(doc),null, getSource(doc), getImgurl(doc));
	}

	public static void main(String[] args) throws IOException {
		ZheJiangNews g = new ZheJiangNews();
		List<News> listNews = g.getListNews("http://zjnews.zjol.com.cn/rsdt/zxrm/");
		listNews.forEach(System.out::println);
	}
}
