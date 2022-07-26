import useLevellog from 'hooks/useLevellog';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogAdd = () => {
  const { levellogRef, handleSubmitLevellogPostForm } = useLevellog();

  return (
    <LevellogForm
      handelSubmitLevellogPostForm={handleSubmitLevellogPostForm}
      levellogRef={levellogRef}
      title="레벨로그 작성"
      buttonValue="제출하기"
    />
  );
};

export default LevellogAdd;
