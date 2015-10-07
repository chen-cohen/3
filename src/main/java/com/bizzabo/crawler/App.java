package com.bizzabo.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class App
{
    private static Set<String> pagesVisited = new HashSet<String>();
    private static int totalPagesToScan;

    public static Set<String> getPagesVisited()
    {
        return pagesVisited;
    }

    public static void addPageToPagesVisited(String page)
    {
        App.pagesVisited.add(page);
    }

    public static synchronized int getTotalPagesToScan()
    {
        return totalPagesToScan;
    }

    public static synchronized void decTotalPagesToScan()
    {
        totalPagesToScan--;
    }

    public static void main( String[] args ) throws InterruptedException
    {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter A Base Page Url");
        String basePageUrl = in.nextLine().toLowerCase();

        if (!basePageUrl.startsWith("http://")){
            throw new RuntimeException("An Url Must Start With:  http:// format");
        }

        System.out.println("Enter Number Of Workers");
        int numberOfWorkers = in.nextInt();

        System.out.println("Enter A Number Of Total Pages To Scan");
        App.totalPagesToScan = in.nextInt();

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

        Worker[] workers = new Worker[numberOfWorkers];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker(queue);
            workers[i].start();
        }
        queue.put(basePageUrl);

    }
}

class Worker extends Thread {
    private BlockingQueue<String> q;


    Worker(BlockingQueue<String> q) {
        this.q = q;

    }

    public void run() {
        try {
            while (true) {
                String link = nextUrl();
                if (link == null || App.getTotalPagesToScan() <=0) {
                    throw new InterruptedException();
                }

                Connection connection = Jsoup.connect(link);
                Document htmlDocument = null;
                try
                {
                    htmlDocument = connection.get();
                } catch(Exception e)
                {
                    e.getStackTrace();
                    break;
                }
                String regex = "http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(htmlDocument.toString());
                while (m.find()) {
                    q.add(m.group());
                }

                System.out.println("Crawling Link: " + link);
                System.out.println("Worker: " + currentThread().getName());
                System.out.println("Page title: " + htmlDocument.select("title").text());
                System.out.println('\n');
                App.decTotalPagesToScan();
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
            System.exit(0);
        }
    }

    public String nextUrl() throws InterruptedException
    {
        String link = q.take();
        if (App.getPagesVisited().contains(link))
        {
            return null;
        }
        else{
            App.addPageToPagesVisited(link);
            return link;
        }
    }
}
