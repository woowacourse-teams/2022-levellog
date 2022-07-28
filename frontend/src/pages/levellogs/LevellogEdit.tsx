import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import useLevellog from 'hooks/useLevellog';

import { ROUTES_PATH } from 'constants/constants';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogEdit = () => {
  const { levellogRef, onSubmitLevellogEditForm, getLevellogOnRef } = useLevellog();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleSubmitLevellogForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onSubmitLevellogEditForm({ teamId, levellogId });
    }

    alert('잘못된 접근입니다.');
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellogOnRef({ teamId, levellogId });
      return;
    }

    alert('잘못된 접근입니다.');
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <LevellogForm
      handleSubmitLevellogForm={handleSubmitLevellogForm}
      levellogRef={levellogRef}
      title="레벨로그 수정"
      buttonValue="수정하기"
    />
  );
};

export default LevellogEdit;
