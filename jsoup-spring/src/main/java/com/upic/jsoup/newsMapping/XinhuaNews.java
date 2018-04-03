package com.upic.jsoup.newsMapping;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.upic.jsoup.po.News;

/**
 * 新华网爬取
 * 注：时政，通过json爬取http://qc.wa.news.cn/nodeart/list?nid=113352&pgnum=1&cnt=10&tp=1&orderby=1
 * 教育：http://qc.wa.news.cn/nodeart/list?nid=11109063&pgnum=1&cnt=10&tp=1&orderby=1
 * 
 * @author dtz
 *
 */
@Component
@SuppressWarnings("all")
public class XinhuaNews extends AbstractJsoupNewsMapping {
	
	@Override
	protected String getNewtext(Document doc) {
		Element elementById = doc.getElementById("p-detail") == null ? doc.getElementById("content")
				: doc.getElementById("p-detail");
		String text = elementById.text();
		return text;
	}

	@Override
	protected String getPubTime(Document doc) {
		Elements elementsByClass = doc.getElementsByClass("h-time");
		String text = elementsByClass.text();
		return text;
	}


	@Override
	protected String getSource(Document doc) {
		Element elementById = doc.getElementById("source");
		return elementById.text();
	}

	@Override
	protected boolean isWebGet() {
		return false;
	}

	/**
	 * 无需网上爬取
	 */
	@Override
	protected List<News> getListNews0(Document document) {
		return null;
	}

	

}
