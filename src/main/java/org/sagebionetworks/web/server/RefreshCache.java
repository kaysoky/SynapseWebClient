package org.sagebionetworks.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RefreshCache {
	public static Set<String> seenUrls = new HashSet<String>();
	public static Queue<String> urlQueue = new LinkedList<String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException("Please provide the website url");
		}
		urlQueue.add(fixUrl(args[0]));
		//go!
		while(!urlQueue.isEmpty()) {
			processNext();
		}
	}
	
	protected static void processNext() throws Exception{
		URL url = new URL(urlQueue.remove());
		URLConnection c = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String inputLine;
		StringBuilder fullHtml = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			fullHtml.append(inputLine);
		in.close();
		Document doc = Jsoup.parse(fullHtml.toString());
		Elements elements = doc.getElementsByTag("a");
		for (int i = 0; i < elements.size(); i++) {
			Element anchor = elements.get(i);
			String newUrl = anchor.attr("href");
			if (newUrl.contains("#!")) {
				newUrl = fixUrl(newUrl);
				if (!seenUrls.contains(newUrl)) { 
					seenUrls.add(newUrl);
					urlQueue.add(newUrl);	
				}
			}
		}
	}
	
	protected static String fixUrl(String url){
		return url.replace("#!", "?_escaped_fragment_=");
	}
	

}
