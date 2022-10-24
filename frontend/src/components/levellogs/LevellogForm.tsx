import React from 'react';

import styled from 'styled-components';

import SubTitleLabel from 'components/@commons/Label';
import Button from 'components/@commons/button/Button';
import ContentHeader from 'components/@commons/contentHeader/ContentHeader';
import Input from 'components/@commons/input/Input';

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
      <S.Content>
        <SubTitleLabel>Level Log</SubTitleLabel>
        <Input width="100%" height="50rem" inputRef={levellogRef} />
      </S.Content>
    </form>
  );
};

interface LevellogFormProps {
  handleSubmitLevellogForm: (e: React.FormEvent<HTMLFormElement>) => void;
  levellogRef: React.RefObject<HTMLInputElement>;
  title: string;
  buttonValue: string;
}

const S = {
  Content: styled.main`
    display: flex;
    flex-direction: column;
  `,
};

export default LevellogForm;
