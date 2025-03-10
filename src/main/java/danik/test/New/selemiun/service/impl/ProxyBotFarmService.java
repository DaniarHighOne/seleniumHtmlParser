package danik.test.New.selemiun.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import danik.test.New.selemiun.service.ProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;

import java.util.concurrent.Executor;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("bot-farm-proxy")
public class ProxyBotFarmService implements ProxyService {

    private final Executor virtualThreadExecutor;
    private final ObjectMapper objectMapper;

    private static final String PROXY_PATH = "/proxy/";


    private static final RequestConfig CONFIG = RequestConfig.custom()
            .setConnectTimeout(15_000)
            .setConnectionRequestTimeout(15_000)
            .setSocketTimeout(15_000)
            .build();


    @Value("${bot-farm.url}")
    private String botFarmUrl;


    @Override
    public ProxyHolder getAvailableProxy() {
        try (CloseableHttpClient client = this.getHttpClient()){
            String uri = this.botFarmUrl + PROXY_PATH;
            //get uri from request
            HttpUriRequest request = new HttpGet(URI.create(uri));
            request.setHeader("Accept", "application/json");
            try (CloseableHttpResponse response = client.execute(request)){
                if(response.getStatusLine().getStatusCode() == 200){
                    return extractResponse(response.getEntity().getContent().readAllBytes());
                }
                else {
                    throw new RuntimeException("Failed to get available proxy");
                }
            }
        } catch (Exception ex) {
            log.error("Error to get available proxy", ex);
            throw new RuntimeException(ex);
        }
    }


    private ProxyHolder extractResponse(byte[] bytes) throws IOException {
        JsonNode dataNode = objectMapper.readTree(bytes).path("data");
        String ip = dataNode.path("ip_address").textValue();
        int port = dataNode.path("port").intValue();
        String username = dataNode.path("username").textValue();
        String password = dataNode.path("password").textValue();
        InetSocketAddress address = new InetSocketAddress(ip, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        return new ProxyHolder(proxy, username, password);
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClientBuilder
                .create()
                .setDefaultRequestConfig(CONFIG)
                .build();
    }

}
