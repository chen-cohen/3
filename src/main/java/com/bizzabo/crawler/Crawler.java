package com.bizzabo.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Crawler {

	private static Crawler instance = new Crawler();
	private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36";
	private static Set<String> pagesVisited = new HashSet<String>();
//	public static final List<String> pagesToVisitList = new LinkedList<String>();
	public final static BlockingQueue<String> pagesToVisitList = new LinkedBlockingQueue<String>();
	private String basePageUrl;
	private int numberOfWorkers;
	private static int totalPagesToScan;


	private Crawler()
	{

	}


	public static Crawler getInstance()
	{
		if (instance == null)
		{
			synchronized(Crawler.class) {
				Crawler inst = instance;
				if (inst == null){
					synchronized(Crawler.class) {
						instance = new Crawler();
					}
				}
			}
		}
		return instance;
	}

//	public static synchronized String nextUrl()
//	{
//		String nextUrl;
//		do
//		{
//			nextUrl = pagesToVisitList.remove(0);
//		} while(pagesVisited.contains(nextUrl));
//		pagesVisited.add(nextUrl);
//		return nextUrl;
//	}

	public static synchronized String nextUrl() throws InterruptedException
	{
		String link = pagesToVisitList.take();
		if (pagesVisited.contains(link))
		{
			return null;
		}
		else{
			pagesVisited.add(link);
			return 	link;
		}
	}

	public void getInput() throws Exception
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter A Base Page Url");
		this.basePageUrl = in.nextLine();

		if (!this.basePageUrl.startsWith("http://")){
			throw new Exception("An Url Must Start With:  http:// format");
		}

		System.out.println("Enter Number Of Workers");
		this.numberOfWorkers = in.nextInt();

		System.out.println("Enter A Number Of Total Pages To Scan");
		totalPagesToScan = in.nextInt();
	}

	protected void crawl() throws InterruptedException
	{
		pagesToVisitList.put(basePageUrl);
		final ExecutorService executor = Executors.newFixedThreadPool(this.numberOfWorkers);
//		while (!pagesToVisitList.isEmpty() && totalPagesToScan > 0) {
//			Runnable worker = new WorkerThread(nextUrl());
//			executor.execute(worker);
//		}


		while (!pagesToVisitList.isEmpty() && totalPagesToScan >0) {
			executor.submit(new Runnable() {


				public void run() {
					while (!Thread.currentThread().isInterrupted() ) {
						try {
							System.out.println();
							String item = pagesToVisitList.take();
							try
							{
								jsoup(item);
								Crawler.decTotalPagesToScan();

							} catch(IOException e)
							{
								e.printStackTrace();
							}

							// Process item
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
							break;
						}
					}
				}
			});
		}

		executor.shutdown();
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");




//		executor.shutdown();
//		while (!executor.isTerminated()) {   }
//		System.out.println("Finished all threads");
	}

	public static synchronized void decTotalPagesToScan()
	{
		totalPagesToScan--;
	}

	public static synchronized void addPageToPagesToVisitList(String link) throws InterruptedException
	{
		pagesToVisitList.put(link);
//		Crawler.pagesToVisitList.notifyAll();
	}

	public static BlockingQueue<String> getPagesToVisitList()
	{
		return pagesToVisitList;
	}

	public void jsoup (String link) throws IOException, InterruptedException
	{

		try
		{
			Connection connection = Jsoup.connect(link);
			Document htmlDocument = connection.get();
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println(htmlDocument.select("title").text());
			pagesToVisitList.put("http://ifatzohar.com");
		} catch(IOException e)
		{
			e.printStackTrace();
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}

//		System.out.println(Crawler.getPagesToVisitList());


//		File input = new File("/");
//		Document doc = Jsoup.parse(input, "UTF-8", link);
	}
}

