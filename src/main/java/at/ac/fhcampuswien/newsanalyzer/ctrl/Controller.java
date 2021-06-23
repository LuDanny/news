package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Article;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "3574f8e6f732410b9869c85db852d28f";  //TODO add your api key


	public void process(NewsApi news) {
		System.out.println("Start process");


		NewsResponse newsResponse = null;
		List<Article> articles = null;

		//TODO implement Error handling
		try{
			newsResponse = news.getNews();
			articles = newsResponse.getArticles();
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}

		//TODO load the news based on the parameters
		if(newsResponse != null && !articles.isEmpty()) {
			articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()));


			//TODO implement methods for analysis - task 5

			System.out.println("Number of articles: " + articles.size());


			articles.stream().forEach(article -> System.out.println(article.getSource().getName()));

			//get the name of provider and the number of articles
			String provider = articles.stream()
					.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
					.entrySet()
					.stream()
					.max(Comparator.comparingInt(t -> t.getValue().intValue())).get().getKey();
			//max compares values and then gets the name aka the key

			System.out.println("The provider with the most articles is " + provider);

			//b
			List shortestName = articles.stream()
					.map(Article::getAuthor)
					.filter(Objects::nonNull)
					.sorted(Comparator.comparing(String::length))
					.collect(Collectors.toList());

			System.out.println(shortestName.get(0));

			//c
			System.out.println("SORTED LIST\n");
			List<Article> result = articles.stream().sorted((object1, object2) -> object1.getTitle().compareTo(object2.getTitle())).collect(Collectors.toList());
			result.stream().forEach(article -> System.out.println("\t" + article.toString() + "\n"));


			// if article is found, save and dl otherwise tell user not found
			if (result != null) {
				System.out.println(result.get(0));
			}

			for (Article downloadedArticle : articles) {
				try {
					URL url = new URL(downloadedArticle.getUrl());
					InputStream input = url.openStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(input));
					//writer creates a new html file - substring is the number of characters that are gonna be the name of the file
					BufferedWriter writer = new BufferedWriter(new FileWriter(downloadedArticle.getTitle().substring(0, 5) + ".html"));
					String line;
					while ((line = reader.readLine()) != null) {
						writer.write(line);
						writer.newLine();
					}
					reader.close();
					writer.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		} else {
			System.err.println("No news");
		}


		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
