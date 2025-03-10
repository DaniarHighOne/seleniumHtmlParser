package danik.test.New.selemiun.service;


import org.springframework.retry.annotation.Retryable;

import java.net.Proxy;
import java.util.Base64;

public interface ProxyService {

    @Retryable
    ProxyHolder getAvailableProxy();

    record ProxyHolder(Proxy proxy, String username, String password) {


        public String basicAuthToken() {
            String basicAuthUnencoded = "%s:%s".formatted(this.username, this.password);
            return Base64.getEncoder().encodeToString(basicAuthUnencoded.getBytes());
        }

    }
}
