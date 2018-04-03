package com.upic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test {
	public LinkedList ImgUrls = new LinkedList();// 用于存放图片URL
	public LinkedList linkurls = new LinkedList();// 用于存放url链接

	public static void main(String[] args) throws IOException {
		Test newCl = new Test();
		// String url = "http://www.xinhuanet.com/politics/2018-03/27/c_1122594312.htm";
		// String url = "http://china.zjol.com.cn/gnxw/201804/t20180402_6934037.shtml";
		// String url="http://china.zjol.com.cn/gnxw/index.shtml";//浙江网list
		String url = "http://www.xinhuanet.com/politics/2018-04/03/c_1122629749.htm";
		Document doc = Jsoup.connect(url).get();
		// newCl.downloadPage(url);// 下载网页
		String title = newCl.getnewTitle(doc);// 获取新闻标题
		String time = newCl.getTimeNew(doc);// 获取新闻发布时间
		String text = newCl.getNewtextBynew(doc);// 获取新闻内容
		String orgin = newCl.getSource(doc);
		// String title = newCl.getnewTitle(doc);// 获取新闻标题
		// String time = newCl.getTimeGMW(doc);// 获取新闻发布时间
		// String text = newCl.getNewtextByGMW(doc);// 获取新闻内容
		// String orgin=newCl.getOrignGMW(doc);
		// newCl.getDataZJW(doc);
		// String title = newCl.getnewTitle(doc);// 获取新闻标题
		// String time = newCl.getTimeZJW(doc);// 获取新闻发布时间
		// String text = newCl.getNewtextByZJW(doc);// 获取新闻内容
		// String orgin=newCl.getOrignZJW(doc);
		// newCl.getData(doc);
		 System.out.println("新闻Url:" + url);
		 System.out.println("新闻标题:" + title);
		 System.out.println("newsTime:" + time);
		 System.out.println("新闻内容:" + text);
		 System.out.println("新闻来源:" + orgin);
		System.out.println("******************************************************************");

		// newCl.getImgurl(doc);// 获取图片链接
		// newCl.getlinkurl(doc);// 获取网页链接
		// System.out.println("图片url链接");
		// for (Object IU : newCl.ImgUrls) {
		// System.out.println(IU);
		// }
		//
		// System.out.println("*****************************************************************");
		// System.out.println("url链接");
		// for (Object LU : newCl.linkurls) {
		// System.out.println(LU);
		// }
	}

	public String getnewTitle(Document doc) {
		// 获取网页的标题
		String title = doc.title();
		return title;
	}

	public LinkedList getlinkurl(Document doc) throws IOException {
		// 获取网页中的所有url地址，并保存在linkurls链表中
		Elements href = doc.select("a[href]");
		for (Element hre : href) {
			String linkUrl = hre.attr("abs:href");// 获取网页的绝对地址
			if (!linkurls.contains(linkUrl)) {
				linkurls.add(linkUrl);
				// System.out.println(linkUrl);
			}
		}
		return linkurls;
	}

	public LinkedList getImgurl(Document doc) throws IOException {
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

	public void downloadImg(String ImgUrl, int count) throws IOException {
		// 下载图片
		String str = "F:\\MyEclipse\\Img\\";// 保存下载图片文件夹
		String ss = str + count + ".png";// 保存图片路径
		URL url = new URL(ImgUrl); // 构造URL
		URLConnection uc = url.openConnection(); // 打开连接
		InputStream is = uc.getInputStream(); // 输入流
		File file = new File(ss); // 创建文件
		FileOutputStream out = new FileOutputStream(file); // 输出的文件流
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			out.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		out.close();
		is.close();
	}

	public String getNewtext(Document doc) {
		// 获取新闻内容
		String text = doc.select("p-right").text();
		text = text.replace(Jsoup.parse("   ").text(), "");
		return text;
	}

	public String getTime(Document doc) {
		// 获取新闻发布的时间
		String time = null;
		Elements element = doc.select("strong.timeSummary");// 此处是根据百度新闻的网页形式解析的新闻时间
		String rex = "^(((20\\d{2})-(\\d{2})-(\\d{2}))) ((\\d{2}):(\\d{2}):(\\d{2}))$";// 正则表达式用于匹配时间
		Pattern pattern = Pattern.compile(rex);
		for (Element el : element) {
			String content = el.text();
			Matcher matcher = pattern.matcher(content);
			if (matcher.matches()) {
				time = content;
				System.out.println("新闻发布时间为：" + content);
			}
		}
		return time;
	}

	public static void downloadPage(String str) {
		// 下载网页
		String filestr = "F:/test";// 设置网页的保存地址
		try {
			URL pageUrl = new URL(str);
			URLConnection uc = pageUrl.openConnection(); // 打开连接
			String path = pageUrl.getPath();// 获取网页的相对路径
			// System.out.println(path);
			if (path.length() == 0) {// 对网页的相对路径进行处理
				path = "index.html";
			} else {
				if (path.endsWith("/")) {
					path = path + "index.html";
				} else {
					int lastSlash = path.lastIndexOf("/");
					int lastPoint = path.lastIndexOf(".");
					if (lastPoint < lastSlash) {
						path = path + ".html";
					}
				}
			}
			filestr = filestr + path;// 网页保存路径
			System.out.println("网页保存路径：" + filestr);
			// 设置网页保存目录，当保存网页的文件夹不存在时，需要先创建文件夹，然后在保存网页
			int lastSlash2 = filestr.lastIndexOf("/");
			String filestrr = filestr.substring(0, lastSlash2);
			// System.out.println(filestrr);
			File file2 = new File(filestrr); // 创建文件目录
			file2.mkdirs();
			File file = new File(filestr); // 创建文件
			InputStream is = uc.getInputStream(); // 输入流
			FileOutputStream out = new FileOutputStream(file); // 输出的文件流
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 开始读取
			while ((len = is.read(bs)) != -1) {

				out.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			out.close();
			is.close();
		} catch (Exception ex) {
		}
	}

	/**
	 * 新华网获取正文
	 * 
	 * @param doc
	 * @return
	 */
	public String getNewtextBynew(Document doc) {
		// 获取新闻内容
		Element elementById = doc.getElementById("p-detail") == null ? doc.getElementById("content")
				: doc.getElementById("p-detail");
		String text = elementById.text();
		return text;
	}

	/**
	 * 新华网获取发布时间
	 */
	public String getTimeNew(Document doc) {
		// 获取新闻发布的时间
		Elements elementsByClass = doc.getElementsByClass("h-time");
		String text = elementsByClass.text();
		return text;
	}

	public String getSource(Document doc) {
		Element elementById = doc.getElementById("source");
		return elementById.text();
	}

	/**
	 * 光明网获取正文
	 * 
	 * @param doc
	 * @return
	 */
	public String getNewtextByGMW(Document doc) {
		// 获取新闻内容
		Element elementById = doc.getElementById("contentMain") == null ? doc.getElementById("ArticleContent")
				: doc.getElementById("contentMain");
		String text = elementById.text();
		return text;
	}

	/**
	 * 光明网获取发布时间
	 */
	public String getTimeGMW(Document doc) {
		// 获取新闻发布的时间
		Element elementsByClass = doc.getElementById("pubTime");
		String text = elementsByClass.text();
		return text;
	}

	/**
	 * 光明网来源
	 * 
	 * @param doc
	 * @return
	 */
	public String getOrignGMW(Document doc) {
		Element elementsByClass = doc.getElementById("source");
		String text = elementsByClass.text();
		return text;
	}

	public String getData(Document doc) {
		Elements elementsByClass = doc.getElementsByClass("channel-newsGroup");
		elementsByClass.forEach(x -> {
			Elements select = x.select("a");
			String select2 = select.attr("href");
			String title = x.text();
			System.out.println(select2);
			try {
				printGMW("http://politics.gmw.cn/" + select2);
			} catch (IOException e) {
			}
		});
		return null;
	}

	public void printGMW(String url) throws IOException {
		Test newCl = new Test();
		// String url = "http://politics.gmw.cn/node_9840.htm ";
		Document doc = Jsoup.connect(url).get();
		String title = newCl.getnewTitle(doc);// 获取新闻标题
		String time = newCl.getTimeGMW(doc);// 获取新闻发布时间
		String text = newCl.getNewtextByGMW(doc);// 获取新闻内容
		String orgin = newCl.getOrignGMW(doc);
		System.out.println("新闻Url:" + url);
		System.out.println("新闻标题:" + title);
		System.out.println("newsTime:" + time);
		System.out.println("新闻内容:" + text);
		System.out.println("新闻来源:" + orgin);
	}

	/******************************************************************************/
	/**
	 * 浙江在线获取正文
	 * 
	 * @param doc
	 * @return
	 */
	public String getNewtextByZJW(Document doc) {
		// 获取新闻内容
		Elements elementById = doc.getElementsByClass("contTxt");
		String text = elementById.text();
		return text;
	}

	/**
	 * 浙江在线获取发布时间
	 */
	public String getTimeZJW(Document doc) {
		// 获取新闻发布的时间
		Element elementsByClass = doc.getElementById("pubtime_baidu");
		String text = elementsByClass.text();
		return text;
	}

	/**
	 * 浙江在线来源
	 * 
	 * @param doc
	 * @return
	 */
	public String getOrignZJW(Document doc) {
		Element elementsByClass = doc.getElementById("source_baidu");
		String text = elementsByClass.text();
		return text;
	}

	public String getDataZJW(Document doc) {
		Elements elementsByClass = doc.getElementsByClass("listLi");
		elementsByClass.forEach(x -> {
			Elements time = x.getElementsByClass("listSpan");
			String times = time.text();
			Elements select = x.select("a");
			String select2 = select.attr("href");
			String title = x.text();
			System.out.println("time:" + times + "   title:" + title);
			System.out.println(select2);
			try {
				printZJW(select2);
			} catch (IOException e) {
			}
		});
		return null;
	}

	private void printZJW(String url) throws IOException {
		Test newCl = new Test();
		// String url = "http://politics.gmw.cn/node_9840.htm ";
		Document doc = Jsoup.connect(url).get();
		String title = newCl.getnewTitle(doc);// 获取新闻标题
		String time = newCl.getTimeZJW(doc);// 获取新闻发布时间
		String text = newCl.getNewtextByZJW(doc);// 获取新闻内容
		String orgin = newCl.getOrignZJW(doc);
		System.out.println("新闻Url:" + url);
		System.out.println("新闻标题:" + title);
		System.out.println("newsTime:" + time);
		System.out.println("新闻内容:" + text);
		System.out.println("新闻来源:" + orgin);

	}
}
