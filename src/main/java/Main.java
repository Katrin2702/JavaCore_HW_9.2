import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(
                "https://api.nasa.gov/planetary/apod?api_key=UuqZpqUcBGbaHNKqNVJCT0a4CD8C8eGhWgpaOLLz");
        CloseableHttpResponse response1 = httpClient.execute(request);

        Nasa post = mapper.readValue(response1.getEntity().getContent(), Nasa.class);
        System.out.println(post);

        String[] parts = post.getUrl().split("/");
        String fileName= parts[parts .length-1];

        CloseableHttpResponse response2 = httpClient.execute(new HttpGet(post.getUrl()));

        File file = new File(fileName);
        FileOutputStream os = new FileOutputStream(file);
             InputStream is = response2.getEntity().getContent();
             byte[] buf = new byte[4096];
             int read;
             while ((read = is.read(buf)) != -1) {
                 os.write(buf, 0, read);
             }
        os.close();

    }
}
