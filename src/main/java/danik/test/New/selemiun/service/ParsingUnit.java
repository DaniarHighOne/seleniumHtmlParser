package danik.test.New.selemiun.service;



public interface ParsingUnit {

    String parseHtml(String url, int statusCode);

    String parseHtmlWithProxy(String url, int statusCode, ProxyService proxyservice);

}
