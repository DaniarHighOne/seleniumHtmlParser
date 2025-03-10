package danik.test.New.selemiun.service.impl;

import danik.test.New.selemiun.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
@Service
@Profile({"tor-proxy", "test"})
public class ProxyRandomTorService implements ProxyService {

    private static final int[] PORTS = IntStream.rangeClosed(9152, 10152).toArray();
    private static final String IP = "0.0.0.0";

    @Override
    public ProxyHolder getAvailableProxy() {
        InetSocketAddress address = new InetSocketAddress(IP, getRandomPort());
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        log.debug(proxy.toString());

        return new ProxyHolder(proxy,null,null);
    }
    private int getRandomPort() { return PORTS[new Random().nextInt(PORTS.length)];}
}
