package danik.test.New.selemiun.service.impl;


import danik.test.New.selemiun.service.ParsingUnit;
import danik.test.New.selemiun.service.ProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.htmlunit.BrowserVersion;
import org.htmlunit.ProxyConfig;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class HtmlUnitImpl implements ParsingUnit {

    private static final CredentialsProvider CREDENTIALS_PROVIDER = new BasicCredentialsProvider();

    public String parseHtml(String url, int statusCode) {
        log.info("Parse HTML page without proxy request");
        return parseHtmlWithConfig(url, null);
    }


    @Override
    public String parseHtmlWithProxy(String url, int statusCode, ProxyService proxyService) {
        log.info("Parsing HTML with proxy request");
        ProxyService.ProxyHolder proxyHolder = proxyService.getAvailableProxy();
        return parseHtmlWithConfig(url, proxyHolder);
    }

    @Override
    public Integer getHttpStatusCode(String url) {

        return 0;
    }


    private String parseHtmlWithConfig(String url, ProxyService.ProxyHolder proxyHolder) {

        List<String> userAgentsList = Arrays.asList(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.2420.81",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
                "Mozilla/5.0 (X11; Linux i686; rv:124.0) Gecko/20100101 Firefox/124.0"
        );

        // Randomly select a User-Agent
        String selectedUserAgent = userAgentsList.get(new Random().nextInt(userAgentsList.size()));
        log.info("Selected User-Agent: {}", selectedUserAgent);

        var address = (InetSocketAddress) proxyHolder.proxy().address();
        HttpHost host = new HttpHost(address.getHostName(), address.getPort());
        CREDENTIALS_PROVIDER.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("%s:%s".formatted(proxyHolder.username(), proxyHolder.password())));

        try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            ProxyConfig proxyConfig = new ProxyConfig(host.getHostName(), host.getPort(), null);
            webClient.getOptions().setProxyConfig(proxyConfig);

            webClient.addRequestHeader("User-Agent", selectedUserAgent);

            //configs for best parsing
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(50000);
            webClient.getOptions().setCssEnabled(true);
            webClient.setJavaScriptTimeout(40000);

            HtmlPage page = webClient.getPage(url);
            log.info("Page title: {}", page.getTextContent());

            String htmlContent = page.asXml();
            log.info("HTML content: {}", htmlContent);


            Path outputPath = Paths.get("htmlPageFormHtmlUnit.html");
            Files.writeString(outputPath, htmlContent, StandardCharsets.UTF_8);
            log.info("HTML file path: {}", outputPath.toAbsolutePath());

            return htmlContent;
        } catch (IOException ex) {
            log.error("Failed to parse HTML page", ex);
        }
        return "";
        }

    }



