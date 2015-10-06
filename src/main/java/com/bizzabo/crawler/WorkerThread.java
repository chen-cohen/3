//package com.bizzabo.crawler;
//
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//public class WorkerThread implements Runnable {
//
//	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36";
//	String link;
//
//	public WorkerThread(String link)
//	{
//		this.link = link;
//	}
//
//	public void run(){
//
//		System.out.println(Thread.currentThread().getName()+" (Start) message = Crawling link:" + this.link );
//		processMessage();//call processMessage method that sleeps the thread for 2 seconds
//
//		try
//		{
//			jsoup();
////			System.out.println(response.toString());
//			Crawler.decTotalPagesToScan();
//		} catch(Exception e)
//		{
//
//		}
//
//		System.out.println(Thread.currentThread().getName()+" (End)");//prints thread name
//	}
//	private void processMessage() {
//		try {  Thread.sleep(2000);  } catch (InterruptedException e) { e.printStackTrace(); }
//	}
//
//	private StringBuffer httpRequsest() throws IOException
//	{
//		CloseableHttpClient httpClient = HttpClients.createDefault();
//		HttpGet httpGet = new HttpGet(this.link);
//		httpGet.addHeader("User-Agent", USER_AGENT);
//		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
//
//		System.out.println("GET Response Status:: "
//			+ httpResponse.getStatusLine().getStatusCode());
//
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//			httpResponse.getEntity().getContent()));
//
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = reader.readLine()) != null) {
//			response.append(inputLine);
//		}
//		reader.close();
//
////		print result
////		System.out.println(response.toString());
//		httpClient.close();
//		return response;
//	}
//
//	private void jsoup() throws IOException
//	{
//		Connection connection = Jsoup.connect(this.link);
//		Document htmlDocument = connection.get();
//		Elements linksOnPage = htmlDocument.select("a[href]");
//		htmlDocument.select("title").text();
//		Crawler.addPageToPagesToVisitList("http://ifatzohar.com");
//		System.out.println(Crawler.getPagesToVisitList());
//
//
//		File input = new File("/");
//		Document doc = Jsoup.parse(input, "UTF-8", this.link);
//	}
//}
//
