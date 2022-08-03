import styled from 'styled-components';

import TeamAddInput from './TeamAddInput';

const TeamEditForm = ({ teamInfoRef }: TeamEditFormProps) => {
  return (
    <S.FormContainer>
      <TeamAddInput
        label={'제목'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[0] = el)}
        minLength={'5'}
        maxLength={'10'}
        required
      />
      <TeamAddInput
        label={'장소'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[1] = el)}
        minLength={'5'}
        maxLength={'10'}
        required
      />
      <TeamAddInput
        label={'날짜'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[2] = el)}
        type={'date'}
        required
      />
      <TeamAddInput
        label={'시간'}
        inputRef={(el: HTMLInputElement) => (teamInfoRef.current[3] = el)}
        type={'time'}
        required
      />
    </S.FormContainer>
  );
};

interface TeamEditFormProps {
  teamInfoRef: React.MutableRefObject<HTMLInputElement[]>;
}

const S = {
  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1.5rem;
  `,
};

export default TeamEditForm;
