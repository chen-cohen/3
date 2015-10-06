package com.bizzabo.crawler;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {

        Crawler crawler = Crawler.getInstance();
        try
        {
            crawler.getInput();
        } catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
        try
        {
            crawler.crawl();
        } catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
