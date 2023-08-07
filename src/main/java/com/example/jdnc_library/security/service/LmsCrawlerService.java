package com.example.jdnc_library.security.service;

import com.example.jdnc_library.security.model.LmsLoginInfo;
import com.example.jdnc_library.security.model.LmsUserInfo;
import jakarta.annotation.PostConstruct;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class LmsCrawlerService {
    private final RestTemplate restTemplate;

    private final String loginUrl;

    private final String personalInfoUrl;

    @Autowired
    public LmsCrawlerService(
        @Value("${lms.login-url}") String loginUrl,
        @Value("${lms.my-info-url}") String personalInfoUrl) {
        this.restTemplate = new RestTemplate();
        this.loginUrl = loginUrl;
        this.personalInfoUrl = personalInfoUrl;
    }

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, KeyManagementException {
        //ssl 우회인증
        setSSL();
    }

    public LmsUserInfo getLmsLoginInfo (LmsLoginInfo lmsLoginInfo) {
        HttpHeaders headers = loginAndGetCookie(lmsLoginInfo.getUsername(), lmsLoginInfo.getPassword());
        return getPersonalInfo(headers);
    }

    // Method to log in and get the main page cookie
    private HttpHeaders loginAndGetCookie(String mbNumber, String mbPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("mb_id", mbNumber);
        requestBody.add("mb_password", mbPassword);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, String.class);

        return responseEntity.getHeaders();
    }

    private LmsUserInfo getPersonalInfo(HttpHeaders beforeHeaders) {
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = beforeHeaders.get("Set-Cookie");

        if (cookies == null) {
            return null;
        }

        //뒤에 cookie 2개를 때야함
        headers.put("Cookie", cookies.subList(0,2));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(personalInfoUrl, HttpMethod.GET, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        String email = parsePersonalEmail(responseBody);
        String name = parsePersonalName(responseBody);

        return new LmsUserInfo(name, email);
    }

    private String parsePersonalEmail (String html) {
        return parsePersonalInfo(html, "mb_email");
    }

    private String parsePersonalName (String html) {
        return parsePersonalInfo(html, "mb_name");
    }

    // Method to parse the personal information from the HTML response
    private String parsePersonalInfo(String html, String inputName) {
        Document doc = Jsoup.parse(html);

//        Element myInfoUl = doc.selectFirst("div.myInfo_area > ul");

//        Element nameLi = myInfoUl.selectFirst("li:has(span:contains("+value+"))");

        Element nameInput = doc.selectFirst("input[name="+inputName+"]");

        return nameInput.attr("value");
    }

    private void setSSL() throws KeyManagementException, NoSuchAlgorithmException {
        //ssl 우회인증
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return new X509Certificate[0];}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}