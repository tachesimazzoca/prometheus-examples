package com.github.tachesimazzoca.prometheus.examples.alertmanager;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class EmailNotificationTest {

    private static final String CHARSET = StandardCharsets.UTF_8.name();

    @Test
    public void testOneshotAlert() {
        GreenMail greenMail = new GreenMail(ServerSetupTest.ALL);
        greenMail.setUser("user", "pass");
        greenMail.start();

        String json = "[" +
                "  {" +
                "    \"annotations\": {" +
                "      \"message\": \"This is an alert message.\"" +
                "    }," +
                "    \"labels\": {" +
                "      \"alertname\": \"app-1\"" +
                "    }," +
                "    \"generatorURL\": \"http://amtest.example.net\"" +
                "  }" +
                "]";

        System.out.println("adding alerts");
        try (CloseableHttpClient hc = HttpClients.createDefault();
             CloseableHttpResponse rs = hc.execute(RequestBuilder
                     .post()
                     .setUri("http://localhost:9093/api/v2/alerts")
                     .addHeader("Content-Type", "application/json")
                     .setEntity(new ByteArrayEntity(json.getBytes(CHARSET)))
                     .build())
        ) {
            System.out.println(rs.getStatusLine());

            greenMail.waitForIncomingEmail(30000L, 1);

            System.out.println("retrieving messages");
            System.out.println();

            MimeMessage[] messages = greenMail.getReceivedMessages();
            Arrays.stream(messages).forEach((msg) -> {
                try {
                    System.out.println("Recipients:");
                    Arrays.stream(msg.getAllRecipients()).forEach(System.out::println);
                    System.out.println();

                    Collections.list(msg.getAllHeaderLines()).forEach(System.out::println);
                    System.out.println();

                    System.out.println(inputStreamToString(msg.getRawInputStream(), CHARSET));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            greenMail.stop();
        }
    }

    public static String inputStreamToString(InputStream input, String charset) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        return output.toString(charset);
    }
}
