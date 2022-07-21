import React, { useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import useTeams from 'hooks/useTeams';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';
import { SubTitleLabel } from 'components/@commons/Label';

const LevellogModify = () => {
  const levellogRef = useRef(null);
  const { levellogModify, levellogLookup } = useLevellog();
  const { teamRequestAndDispatch } = useTeams();
  const { teamId, levellogId } = useParams();

  const handleSubmitLevellogForm = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await levellogModify(teamId, levellogId, levellogRef.current.value);
    teamRequestAndDispatch();
  };

  const requestLevellogLookup = async () => {
    const res = await levellogLookup(teamId, levellogId);
    levellogRef.current.value = res.content;
  };

  useEffect(() => {
    requestLevellogLookup();
  }, []);

  return (
    <form onSubmit={handleSubmitLevellogForm}>
      <ContentHeader title="레벨로그 수정">
        <Button>수정하기</Button>
      </ContentHeader>
      <MainContainer>
        <SubTitleLabel>Level Log</SubTitleLabel>
        <Input width="100%" height="500px" inputRef={levellogRef} />
      </MainContainer>
    </form>
  );
};

const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
`;

export default LevellogModify;
