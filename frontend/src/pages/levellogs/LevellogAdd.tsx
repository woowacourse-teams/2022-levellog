import { useParams } from 'react-router-dom';

import useLevellog from 'hooks/useLevellog';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogAdd = () => {
  const { levellogRef, onSubmitLevellogPostForm } = useLevellog();
  const { teamId } = useParams();

  const handleSubmitLevellogForm = (e: any) => {
    e.preventDefault();
    onSubmitLevellogPostForm({ teamId });
  };

  return (
    <LevellogForm
      handleSubmitLevellogForm={handleSubmitLevellogForm}
      levellogRef={levellogRef}
      title="레벨로그 작성"
      buttonValue="제출하기"
    />
  );
};

export default LevellogAdd;
