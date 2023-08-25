package com.example.jdnc_library.security.service;

import com.example.jdnc_library.domain.member.model.Member;
import com.example.jdnc_library.domain.member.model.Role;
import com.example.jdnc_library.domain.member.repository.MemberRepository;
import com.example.jdnc_library.exception.clienterror._401.NotLoginException;
import com.example.jdnc_library.security.model.LoginInfo;
import com.example.jdnc_library.security.model.LmsTotalInfo;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * lms 로그인 서비스
 */
@Service
public class LmsCrawlerService {

    private final RestTemplate restTemplate;

    private final String loginUrl;

    private final String personalInfoUrl;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LmsCrawlerService(
        @Value("${lms.login-url}") String loginUrl,
        @Value("${lms.my-info-url}") String personalInfoUrl,
        MemberRepository memberRepository,
        PasswordEncoder passwordEncoder) {
        this.restTemplate = new RestTemplate();
        this.loginUrl = loginUrl;
        this.personalInfoUrl = personalInfoUrl;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() throws NoSuchAlgorithmException, KeyManagementException {
        //ssl 우회인증
        setSSL();
    }

    /**
     * lms 로그인 인증 및 정보 취득
     *
     * @param lmsLoginInfo Lms 로그인 정보
     * @return 총체적인 Lms 정보
     */
    public LmsTotalInfo getLmsLoginInfo(LoginInfo lmsLoginInfo) {
        HttpHeaders headers = loginAndGetCookie(lmsLoginInfo.getUsername(),
            lmsLoginInfo.getPassword());
        LmsUserInfo lmsUserInfo = getLmsUserInfo(headers);

        Member member = memberRepository.findByUsername(lmsLoginInfo.getUsername());
        LmsTotalInfo lmsTotalInfo;
        if (member == null) {
            lmsTotalInfo = LmsTotalInfo.of(createNewMember(lmsLoginInfo, lmsUserInfo));
        } else {
            lmsTotalInfo = LmsTotalInfo.of(validateMember(member, lmsLoginInfo, lmsUserInfo));
        }

        return lmsTotalInfo;
    }

    /**
     * db에 유저정보 없을시 유저정보 삽입
     *
     * @param lmsLoginInfo Lms 로그인 정보
     * @param lmsUserInfo  Lms 유저정보
     * @return 새로 생성된 멤버
     */
    private Member createNewMember(LoginInfo lmsLoginInfo, LmsUserInfo lmsUserInfo) {
        return memberRepository.save(new Member(
            lmsLoginInfo.getUsername(),
            passwordEncoder.encode(lmsLoginInfo.getPassword()),
            lmsUserInfo.getName(),
            lmsUserInfo.getEmail(),
            null,
            Role.ROLE_USER
        ));
    }

    /**
     * db에 유저정보 있을시 유저정보 조회 및 최신화
     *
     * @param member       db entity
     * @param lmsLoginInfo Lms 로그인 정보
     * @param lmsUserInfo  Lms 유저정보
     * @return 업데이트 된 멤버
     */

    private Member validateMember(Member member, LoginInfo lmsLoginInfo,
        LmsUserInfo lmsUserInfo) {
        boolean flag = false;
        if (member.getPassword() == null || !passwordEncoder.matches(lmsLoginInfo.getPassword(),
            member.getPassword())) {
            flag = true;
        }

        if (member.getEmail() == null || !member.getEmail().equals(lmsUserInfo.getEmail())) {
            flag = true;
        }

        if (member.getName() == null || !member.getName().equals(lmsUserInfo.getName())) {
            flag = true;
        }

        if (flag) {
            member.update(
                lmsUserInfo.getName(),
                passwordEncoder.encode(lmsLoginInfo.getPassword()),
                lmsUserInfo.getEmail());
            memberRepository.save(member);
        }
        return member;
    }

    /**
     * Lms 로그인 및 쿠키 탈취
     *
     * @param mbNumber   로그인 id
     * @param mbPassword 로그인 pw
     * @return
     */
    // Method to log in and get the main page cookie
    private HttpHeaders loginAndGetCookie(String mbNumber, String mbPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("mb_id", mbNumber);
        requestBody.add("mb_password", mbPassword);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody,
            headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(loginUrl, HttpMethod.POST,
            requestEntity, String.class);

        return responseEntity.getHeaders();
    }

    /**
     * 헤더를 이용하여 lms 내 정보 가져오기
     *
     * @param beforeHeaders 로그인 완료 헤더
     * @return lms 유저정보
     */
    private LmsUserInfo getLmsUserInfo(HttpHeaders beforeHeaders) {
        String responseBody = getMyInfoPageBody(beforeHeaders);

        if (responseBody == null) {
            throw new NotLoginException("로그인에 실패했습니다.");
        }

        String email = parsePersonalEmail(responseBody);
        String name = parsePersonalName(responseBody);

        return new LmsUserInfo(name, email);
    }

    /**
     * 헤더를 이용하여 페이지 response 조회
     *
     * @param beforeHeaders 로그인 정보 헤더
     * @return response
     */
    private String getMyInfoPageBody(HttpHeaders beforeHeaders) {
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = beforeHeaders.get("Set-Cookie");

        if (cookies == null) {
            return null;
        }

        //뒤에 cookie 2개를 때야함
        headers.put("Cookie", cookies.subList(0, 2));

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(personalInfoUrl,
            HttpMethod.GET, requestEntity, String.class);

        return responseEntity.getBody();
    }

    /**
     * html 에서 이메일 파싱
     *
     * @param html
     * @return email
     */
    private String parsePersonalEmail(String html) {
        return parsePersonalInfo(html, "mb_email");
    }

    /**
     * html 에서 이름 파싱
     *
     * @param html
     * @return email
     */
    private String parsePersonalName(String html) {
        return parsePersonalInfo(html, "mb_name");
    }

    /**
     * html 에서 정보 파싱
     *
     * @param html
     * @param inputName
     * @return
     */
    // Method to parse the personal information from the HTML response
    private String parsePersonalInfo(String html, String inputName) {
        try {
            Document doc = Jsoup.parse(html);

            Element nameInput = doc.selectFirst("input[name=" + inputName + "]");

            if (nameInput != null) {
                return nameInput.attr("value");
            } else {
                throw new NotLoginException("로그인에 실패했습니다.");
            }
        } catch (Exception e) {
            throw new NotLoginException("로그인에 실패했습니다.");
        }
    }

    private void setSSL() throws KeyManagementException, NoSuchAlgorithmException {
        //ssl 우회인증
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}