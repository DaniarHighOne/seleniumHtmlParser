package danik.test.New.selemiun.service.impl;


import danik.test.New.selemiun.service.ProxyService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Service
@Profile({"static", "defaut"})
public class ProxyStaticService implements ProxyService {

    private static final String IP = "0.0.0.0";
    private static final int PORT = 3785;
    private static final String USERNAME = "freeqzaq";
    private static final String PASSWORD = "freeqzaq";


    @Override
    public ProxyHolder getAvailableProxy() {
        InetSocketAddress address = new InetSocketAddress(IP, PORT);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        return new ProxyHolder(proxy,USERNAME,PASSWORD);
    }
}
