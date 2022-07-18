package com.woowacourse.levellog.authentication.domain;

import com.woowacourse.levellog.authentication.dto.GithubAccessTokenRequest;
import com.woowacourse.levellog.authentication.dto.GithubAccessTokenResponse;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import java.util.Objects;
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
    public String getAccessToken(final String code) {
        final GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(clientId, clientSecret,
                code);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GithubAccessTokenRequest> httpEntity =
                new HttpEntity<>(githubAccessTokenRequest, headers);

        final GithubAccessTokenResponse response = new RestTemplate()
                .exchange(TOKEN_URL, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
                .getBody();

        if (Objects.isNull(response)) {
            throw new IllegalStateException("Github 요청에 실패했습니다.");
        }

        return response.getAccessToken();
    }

    @Override
    public GithubProfileResponse getProfile(final String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "token " + accessToken);

        final HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        return new RestTemplate()
                .exchange(USER_ACCESS_URL, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
    }
}
