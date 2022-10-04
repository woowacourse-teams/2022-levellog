package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.authentication.support.JwtTokenProvider.ADMIN_TOKEN_PAYLOAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.admin.dto.request.AdminPasswordRequest;
import com.woowacourse.levellog.admin.dto.response.AdminAccessTokenResponse;
import com.woowacourse.levellog.admin.dto.response.AdminTeamResponse;
import com.woowacourse.levellog.admin.exception.WrongPasswordException;
import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AdminService의")
public class AdminServiceTest extends ServiceTest {

    @Nested
    @DisplayName("login 메서드는")
    class Login {

        @Test
        @DisplayName("암호화된 비밀번호와 일치하는 비밀번호를 입력하면 token을 발급한다.")
        void success() {
            // given
            final AdminPasswordRequest request = new AdminPasswordRequest("levellog1!");

            // when
            final AdminAccessTokenResponse actual = adminService.login(request);

            // then
            final String token = actual.getAccessToken();
            final String payload = jwtTokenProvider.getPayload(token);

            assertThat(payload).isEqualTo(ADMIN_TOKEN_PAYLOAD);
        }

        @Test
        @DisplayName("잘못된 비밀번호를 입력하면 예외를 던진다.")
        void login_wrongPassword_exception() {
            // given
            final AdminPasswordRequest request = new AdminPasswordRequest("wrong-password");

            // when & then
            assertThatThrownBy(() -> adminService.login(request))
                    .isInstanceOf(WrongPasswordException.class);
        }
    }

    @Nested
    @DisplayName("findAllTeam 메서드는")
    class FindAllTeam {

        @Test
        @DisplayName("팀 목록을 조회한다.")
        void success() {
            // given
            final Member rick = saveMember("릭");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");

            saveTeam(rick, eve);
            saveTeam(eve, alien);

            // when
            final List<AdminTeamResponse> actual = adminService.findAllTeam();

            // then
            assertThat(actual).hasSize(2);
        }
    }

    @Nested
    @DisplayName("deleteTeamById 메서드는")
    class DeleteTeamById {

        @Test
        @DisplayName("팀 id가 주어지면 해당하는 팀을 삭제한다. - status: ready")
        void deleteTeamById_ready_success() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, alien);
            final Long teamId = team.getId();

            // when
            adminService.deleteTeamById(teamId);

            // then
            final Optional<Team> actual = teamRepository.findById(teamId);
            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("팀 id가 주어지면 해당하는 팀을 삭제한다. - status: inProgress")
        void deleteTeamById_inProgress_success() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, alien);
            final Long teamId = team.getId();

            timeStandard.setInProgress();

            // when
            adminService.deleteTeamById(teamId);

            // then
            final Optional<Team> actual = teamRepository.findById(teamId);
            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("팀 id가 주어지면 해당하는 팀을 삭제한다. - status: closed")
        void deleteTeamById_closed_success() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, alien);
            final Long teamId = team.getId();

            team.close(TimeFixture.AFTER_START_TIME);

            // when
            adminService.deleteTeamById(teamId);

            // then
            final Optional<Team> actual = teamRepository.findById(teamId);
            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void deleteTeamById_notFound_exception() {
            // given
            final Long teamId = 999L;

            // when & then
            assertThatThrownBy(() -> adminService.deleteTeamById(teamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("closeTeam 메서드는")
    class CloseTeam {

        @Test
        @DisplayName("팀 id가 주어지면 해당하는 팀을 종료한다. - status: ready")
        void closeTeam_ready_success() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, alien);
            final Long teamId = team.getId();

            // when
            adminService.closeTeam(teamId);

            // then
            final Team actual = teamRepository.findById(teamId).get();
            assertThat(actual.isClosed()).isTrue();
        }

        @Test
        @DisplayName("팀 id가 주어지면 해당하는 팀을 종료한다. - status: inProgress")
        void closeTeam_inProgress_success() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, alien);
            final Long teamId = team.getId();

            timeStandard.setInProgress();

            // when
            adminService.closeTeam(teamId);

            // then
            final Team actual = teamRepository.findById(teamId).get();
            assertThat(actual.isClosed()).isTrue();
        }

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void closeTeam_notFound_exception() {
            // given
            final Long teamId = 999L;

            // when & then
            assertThatThrownBy(() -> adminService.closeTeam(teamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }
}
