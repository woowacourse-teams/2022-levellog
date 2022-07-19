import React, { useRef } from 'react';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';
import { SubTitleLabel } from 'components/@commons/Label';

const LevellogAdd = () => {
  const levellogRef = useRef(null);
  const { levellogAdd } = useLevellog();

  const handleSubmitLevellogForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    levellogAdd(levellogRef.current.value);
  };

  return (
    <form onSubmit={handleSubmitLevellogForm}>
      <ContentHeader title="레벨로그 작성">
        <Button>제출하기</Button>
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

export default LevellogAdd;
