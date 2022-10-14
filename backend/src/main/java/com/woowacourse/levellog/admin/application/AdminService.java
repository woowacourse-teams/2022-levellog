package com.woowacourse.levellog.admin.application;

import static com.woowacourse.levellog.authentication.support.JwtTokenProvider.ADMIN_TOKEN_PAYLOAD;

import com.woowacourse.levellog.admin.dto.request.AdminPasswordRequest;
import com.woowacourse.levellog.admin.dto.response.AdminAccessTokenResponse;
import com.woowacourse.levellog.admin.dto.response.AdminTeamResponse;
import com.woowacourse.levellog.admin.exception.WrongPasswordException;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final String hash;
    private final JwtTokenProvider jwtTokenProvider;
    private final TimeStandard timeStandard;
    private final TeamRepository teamRepository;

    public AdminService(@Value("${security.admin.hash}") final String hash, final JwtTokenProvider jwtTokenProvider,
                        final TimeStandard timeStandard, final TeamRepository teamRepository) {
        this.hash = hash;
        this.jwtTokenProvider = jwtTokenProvider;
        this.timeStandard = timeStandard;
        this.teamRepository = teamRepository;
    }

    public AdminAccessTokenResponse login(final AdminPasswordRequest request) {
        final boolean isMatch = BCrypt.checkpw(request.getValue(), hash);
        if (!isMatch) {
            throw new WrongPasswordException(DebugMessage.init()
                    .append("plainPassword", request.getValue())
                    .append("hash", hash)
            );
        }

        final String token = jwtTokenProvider.createToken(ADMIN_TOKEN_PAYLOAD);
        return new AdminAccessTokenResponse(token);
    }

    public List<AdminTeamResponse> findAllTeam() {
        final LocalDateTime presentTime = timeStandard.now();

        return teamRepository.findAll()
                .stream()
                .map(it -> AdminTeamResponse.of(it, it.status(presentTime)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteTeamById(final Long teamId) {
        teamRepository.getTeam(teamId);
        teamRepository.deleteById(teamId);
    }

    @Transactional
    public void closeTeam(final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        final LocalDateTime presentTime = team.getDetail()
                .getStartAt();
        team.close(presentTime.plusDays(1)); // Team 도메인 정책상 인터뷰 시작 시간 이후에만 종료할 수 있으므로 plusDays(1) 추가
    }
}
