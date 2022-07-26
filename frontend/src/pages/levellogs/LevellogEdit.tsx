import { useEffect } from 'react';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogEdit = () => {
  const { levellogRef, handleSubmitLevellogEditForm, getLevellogOnRef } = useLevellog();

  useEffect(() => {
    getLevellogOnRef();
  }, []);

  return (
    <LevellogForm
      handelSubmitLevellogPostForm={handleSubmitLevellogEditForm}
      levellogRef={levellogRef}
      title="레벨로그 작성"
      buttonValue="제출하기"
    />
  );
};

export default LevellogEdit;
