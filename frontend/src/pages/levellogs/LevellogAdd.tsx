import { useNavigate, useParams } from 'react-router-dom';

import useLevellog from 'hooks/useLevellog';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import LevellogForm from 'components/levellogs/LevellogForm';

const LevellogAdd = () => {
  const { levellogRef, onSubmitLevellogPostForm } = useLevellog();
  const { teamId } = useParams();
  const navigate = useNavigate();

  const handleSubmitLevellogForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (typeof teamId === 'string') {
      onSubmitLevellogPostForm({ teamId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  return (
    <LevellogForm
      handleSubmitLevellogForm={handleSubmitLevellogForm}
      levellogRef={levellogRef}
      title={'레벨로그 작성'}
      buttonValue={'제출하기'}
    />
  );
};

export default LevellogAdd;
