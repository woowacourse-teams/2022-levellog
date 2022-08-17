package com.woowacourse.levellog.admin.application;

import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.PasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    public AdminAccessTokenDto login(final PasswordDto request) {
        return null;
    }
}
