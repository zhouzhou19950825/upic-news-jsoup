package com.upic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownLoadImg {
	private static final String SAVE_PATH="/Users/dongtengzhou/git/";
	public static void main(String[] args) throws Exception {
		DownLoadImg d=new DownLoadImg();
		String url = "https://product.suning.com/0000000000/729770974.html";
		Document doc = Jsoup.connect(url).get();
		LinkedList<String> imgurl = d.getImgurl(doc);
		imgurl.forEach(x -> {
			try {
				d.downloadImg(x, 1);
			} catch (IOException e) {
				System.out.println("error");
			}});
	}
	public LinkedList<String> ImgUrls = new LinkedList<String>();// 用于存放图片URL
	public LinkedList<String> getImgurl(Document doc) throws IOException {
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
//		String str = "F:\\MyEclipse\\Img\\";// 保存下载图片文件夹
		String ss = SAVE_PATH + System.currentTimeMillis() + ".png";// 保存图片路径
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
}
