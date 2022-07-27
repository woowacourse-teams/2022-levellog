package com.woowacourse.levellog.authentication.domain;

import com.woowacourse.levellog.authentication.dto.GithubAccessTokenDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.authentication.dto.TokenDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GithubOAuthClient implements OAuthClient {

    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_ACCESS_URL = "https://api.github.com/user";

    private final String clientId;
    private final String clientSecret;

    public GithubOAuthClient(final String clientId, final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getAccessToken(final String authorizationCode) {
        final GithubAccessTokenDto request = new GithubAccessTokenDto(clientId, clientSecret, authorizationCode);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GithubAccessTokenDto> httpEntity = new HttpEntity<>(request, headers);
        final TokenDto response = new RestTemplate()
                .exchange(TOKEN_URL, HttpMethod.POST, httpEntity, TokenDto.class)
                .getBody();

        if (response == null) {
            throw new IllegalStateException("Github 로그인 요청 실패 - authorizationCode:" + authorizationCode);
        }

        return response.getAccessToken();
    }

    @Override
    public GithubProfileDto getProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        final HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        return new RestTemplate()
                .exchange(USER_ACCESS_URL, HttpMethod.GET, httpEntity, GithubProfileDto.class)
                .getBody();
    }
}
