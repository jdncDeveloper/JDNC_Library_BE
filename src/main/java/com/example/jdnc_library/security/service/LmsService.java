package com.example.jdnc_library.security.service;

import com.example.jdnc_library.security.model.LmsLoginInfo;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LmsService {

    private final String loginUrl = "https://lms.jdnc.or.kr/auth/login_check";

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, KeyManagementException {
        //ssl 우회인증
        setSSL();
    }

    public String getNameWithLogin (LmsLoginInfo lmsLoginInfo)
        throws IOException {
        //암호화 해제
        decryptedInfo(lmsLoginInfo);
        //조회
        Document document = Jsoup.connect(loginUrl)
            .timeout(50000)
            .data("mb_id", lmsLoginInfo.getUserNumber(), "mb_password", lmsLoginInfo.getPassword()).post();

        Elements userWelcomeSpans = document.getElementsByClass("user_welcome");

        if (!userWelcomeSpans.isEmpty()) {
            Element userWelcomeSpan = userWelcomeSpans.first();

            if (userWelcomeSpan != null) {
                return getNameBySpan(userWelcomeSpan);
            } else {
                return null;
            }
        } else {
           return null;
        }
    }

    private String getNameBySpan(Element element) {
        String spanString = element.text();
        return spanString.substring(0, spanString.indexOf("님"));
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

    private void decryptedInfo (LmsLoginInfo lmsLoginInfo) {

    }

}
