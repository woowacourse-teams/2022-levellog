const baseURL = 'http://localhost:3000';
const serverURL = 'https://local.levellog.app/api';

describe('Page Route Test', () => {
  it('Home(InterviewTeams) Page Test', () => {
    cy.intercept('GET', `${serverURL}/teams`, (req) => {
      req.continue((res) => {
        res.send(200, {
          teams: [
            {
              id: 111,
              title: 'TEST 제목',
              place: 'TEST 장소',
              startAt: 'TEST 시간',
              teamImage: '페퍼.com',
              hostId: 1,
              participants: [
                {
                  memberId: 1,
                  levellogId: null,
                  nickname: '페퍼',
                  profileUrl: '페퍼.com',
                },
                {
                  memberId: 2,
                  levellogId: null,
                  nickname: '해리',
                  profileUrl: '해리.com',
                },
              ],
            },
            {
              id: 222,
              title: 'TEST2 제목',
              place: 'TEST2 장소',
              startAt: 'TEST3 시간',
              teamImage: '이브.com',
              hostId: 2,
              participants: [
                {
                  memberId: 3,
                  levellogId: null,
                  nickname: '이브',
                  profileUrl: '이브.com',
                },
                {
                  memberId: 4,
                  levellogId: null,
                  nickname: '로마',
                  profileUrl: '로마.com',
                },
              ],
            },
          ],
        });
      });
    }).as('getTeams');
    cy.visit(baseURL);
    cy.wait('@getTeams');
    cy.get('h1').should((pageTitle) => {
      expect(pageTitle).to.contain('인터뷰 팀');
    });
    cy.get('#111').should((firstTeamTitle) => {
      expect(firstTeamTitle).to.contain('TEST 제목');
    });
    cy.get('#222').should((secondTeamTitle) => {
      expect(secondTeamTitle).to.contain('TEST2 제목');
    });
  });

  // it('InterviewDetail Page Test', () => {
  //   cy.intercept('GET', `${serverURL}/teams/111`, (req) => {
  //     req.continue((res) => {
  //       res.send(200, {
  //         id: 111,
  //         title: 'TEST 제목',
  //         place: 'TEST 장소',
  //         startAt: 'TEST 시간',
  //         teamImage: '페퍼.com',
  //         hostId: 1,
  //         participants: [
  //           {
  //             memberId: 1,
  //             levellogId: 12,
  //             nickname: '페퍼',
  //             profileUrl: '페퍼.com',
  //           },
  //           {
  //             memberId: 2,
  //             levellogId: null,
  //             nickname: '해리',
  //             profileUrl: '해리.com',
  //           },
  //         ],
  //       });
  //     });
  //   }).as('getTeam');
  //   cy.visit(`${baseURL}/interview/teams/111`);
  //   cy.wait('@getTeam');
  //   cy.get('h3:first').should((pageTitle) => {
  //     expect(pageTitle).to.contain('TEST 제목');
  //   });
  //   cy.get('div p').should((participantName) => {
  //     expect(participantName[2]).to.contain('페퍼');
  //   });
  // });

  // it('LevellogModal Test', () => {
  //   cy.intercept('GET', `${serverURL}/teams/111/levellogs/12`, (req) => {
  //     req.continue((res) => {
  //       res.send(200, {
  //         content: 'Spring과 React를 학습했습니다.',
  //       });
  //     });
  //   }).as('getLevellog');
  //   cy.get('div > button')
  //     .first()
  //     .should((participantLevellog) => {
  //       expect(participantLevellog).to.contain('레벨로그 보기');
  //     })
  //     .click();
  //   cy.wait('@getLevellog');
  //   cy.get('h2').should((levellogTitle) => {
  //     expect(levellogTitle).to.contain('페퍼의 레벨로그');
  //   });
  // });
});
