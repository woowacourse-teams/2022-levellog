import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

import useLevellog from 'hooks/useLevellog';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogEdit = () => {
  const { levellogRef, onSubmitLevellogEditForm, getLevellogOnRef } = useLevellog();
  const { teamId, levellogId } = useParams();

  const handleSubmitLevellogForm = (e: any) => {
    e.preventDefault();
    onSubmitLevellogEditForm({ teamId, levellogId });
  };

  useEffect(() => {
    getLevellogOnRef({ teamId, levellogId });
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
