import styled from 'styled-components';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import Input from 'components/@commons/Input';
import { SubTitleLabel } from 'components/@commons/Label';

const LevellogForm = ({ handleSubmitLevellogForm, levellogRef, title, buttonValue }: any) => {
  return (
    <form onSubmit={handleSubmitLevellogForm}>
      <ContentHeader title={title}>
        <Button onClick={handleSubmitLevellogForm}>{buttonValue}</Button>
      </ContentHeader>
      <MainContainer>
        <SubTitleLabel>Level Log</SubTitleLabel>
        <Input width="100%" height="50rem" inputRef={levellogRef} />
      </MainContainer>
    </form>
  );
};

const MainContainer = styled.main`
  display: flex;
  flex-direction: column;
`;

export default LevellogForm;
