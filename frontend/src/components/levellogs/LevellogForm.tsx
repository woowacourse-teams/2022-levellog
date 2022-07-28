import React from 'react';

import styled from 'styled-components';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';
import { SubTitleLabel } from 'components/@commons/Label';

const LevellogForm = ({
  handleSubmitLevellogForm,
  levellogRef,
  title,
  buttonValue,
}: LevellogFormProps) => {
  return (
    <form onSubmit={handleSubmitLevellogForm}>
      <ContentHeader title={title}>
        <Button type={'submit'}>{buttonValue}</Button>
      </ContentHeader>
      <MainContainer>
        <SubTitleLabel>Level Log</SubTitleLabel>
        <Input width="100%" height="50rem" inputRef={levellogRef} />
      </MainContainer>
    </form>
  );
};

interface LevellogFormProps {
  handleSubmitLevellogForm: (e: React.FormEvent<HTMLFormElement>) => void;
  levellogRef: React.RefObject<HTMLInputElement>;
  title: string;
  buttonValue: string;
}

const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
`;

export default LevellogForm;
