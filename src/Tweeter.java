
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Bharath
 *
 */
public class Tweeter { 
	public static void main(String[] args) throws TwitterException, IOException {
		Properties prop = getProperity();
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(prop.getProperty("auth.consumer.key"))
		.setOAuthConsumerSecret(prop.getProperty("auth.consumer.secret"))
		.setOAuthAccessToken(prop.getProperty("auth.access.token"))
		.setOAuthAccessTokenSecret(prop.getProperty("auth.access.token.secret"));
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		System.out.println("Please enter topics name in comma seprate, e.g. baseball, basketball, movies, etc");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		String[] topics = reader.readLine().split(",");
		searchtweets(twitter, topics);
		System.out.println("Tweet message write sucessfully in files.");
	}

	public static void searchtweets(Twitter twitter, String[] topics) throws TwitterException {

		Query query = null;
		for (String topic : topics) {
			System.out.println("Processing "+topic.trim()+" messages.");
			query = new Query(topic);
			QueryResult result = twitter.search(query);

			List<String> list = result.getTweets().stream().map(item -> item.getText()).collect(Collectors.toList());
			writeTweetMessage(list, topic);
		}
	}

	private static void writeTweetMessage(List<String> list, String fileName) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName + ".txt");
			for (String str : list) {
				writer.write(str + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Properties getProperity() {
		Properties prop = null;
		try (InputStream input = new FileInputStream("tweets.properties")) {

			prop = new Properties();
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop;
	}

}
