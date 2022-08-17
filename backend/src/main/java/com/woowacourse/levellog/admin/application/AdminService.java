package com.woowacourse.levellog.admin.application;

import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.PasswordDto;
import com.woowacourse.levellog.admin.exception.WrongPasswordException;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final String hash;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminService(@Value("${security.admin.hash}") final String hash, final JwtTokenProvider jwtTokenProvider) {
        this.hash = hash;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AdminAccessTokenDto login(final PasswordDto request) {
        final boolean isMatch = BCrypt.checkpw(request.getValue(), hash);
        if (!isMatch) {
            throw new WrongPasswordException(DebugMessage.init()
                    .append("plainPassword", request.getValue())
                    .append("hash", hash)
            );
        }

        final String token = jwtTokenProvider.createToken("This is admin token.");
        return new AdminAccessTokenDto(token);
    }
}
