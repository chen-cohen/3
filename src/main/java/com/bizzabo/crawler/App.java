package com.bizzabo.crawler;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 *
 */
public class App
{
    private static int totalPagesToScan;

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
        String basePageUrl = in.nextLine();

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
                String link = q.take();
                if (link == null || App.getTotalPagesToScan() <=0) {
                    throw new InterruptedException();
                }
                System.out.println("Crawling Link: " + link);
                System.out.println("Worker: " + currentThread().getName());
                Connection connection = Jsoup.connect(link);
                Document htmlDocument = connection.get();
                Elements linksOnPage = htmlDocument.select("a[href]");
                System.out.printf(linksOnPage.toString());
                System.out.println("Page title: " + htmlDocument.select("title").text());
                System.out.println('\n');
                q.add("http://msn.co.il");
                App.decTotalPagesToScan();
            }
        } catch (InterruptedException e) {
            System.exit(0);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
