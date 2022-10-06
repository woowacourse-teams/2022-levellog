package com.woowacourse.levellog.admin.application;

import static com.woowacourse.levellog.authentication.support.JwtTokenProvider.ADMIN_TOKEN_PAYLOAD;

import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.AdminPasswordDto;
import com.woowacourse.levellog.admin.dto.AdminTeamDto;
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

    public AdminAccessTokenDto login(final AdminPasswordDto request) {
        final boolean isMatch = BCrypt.checkpw(request.getValue(), hash);
        if (!isMatch) {
            throw new WrongPasswordException(DebugMessage.init()
                    .append("plainPassword", request.getValue())
                    .append("hash", hash)
            );
        }

        final String token = jwtTokenProvider.createToken(ADMIN_TOKEN_PAYLOAD);
        return new AdminAccessTokenDto(token);
    }

    public List<AdminTeamDto> findAllTeam() {
        final LocalDateTime presentTime = timeStandard.now();

        return teamRepository.findAll()
                .stream()
                .map(it -> AdminTeamDto.of(it, it.status(presentTime)))
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
        team.close(presentTime.plusDays(1));
    }
}
