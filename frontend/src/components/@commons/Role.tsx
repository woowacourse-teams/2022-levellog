import styled from 'styled-components';

import InterviewerImage from 'assets/images/group.png';
import HandShake from 'assets/images/handshake.png';
import MicImage from 'assets/images/mic.png';

import Image from 'components/@commons/Image';

const Role = ({ role }: RoleProp) => {
  return (
    <RoleStyle role={role}>
      {role === '인터뷰어' && <Image src={InterviewerImage} sizes={'SMALL'} />}
      {role === '인터뷰이' && <Image src={MicImage} sizes={'SMALL'} />}
      {role === '상호 인터뷰' && <Image src={HandShake} sizes={'SMALL'} />}
      {role === '상호 인터뷰' ? <RoleText>{role}</RoleText> : <RoleText>{`나의 ${role}`}</RoleText>}
    </RoleStyle>
  );
};

interface RoleProp {
  role: string;
}

const RoleStyle = styled.div`
  display: flex;
  justify-content: start;
  align-items: center;
  position: absolute;
  top: 14px;
  left: 10px;
  z-index: 10;
  width: fit-content;
  height: 2.1875rem;
  padding: 0.1875rem;
  box-shadow: 2px 2px 0.25rem ${(props) => props.theme.new_default.DARK_GRAY};
  border-style: none;
  border-radius: 1.5625rem;
  background-color: ${(props) => props.theme.default.DARK_GRAY};
`;

const RoleText = styled.p`
  color: ${(props) => props.theme.default.WHITE};
  margin: 0 0.375rem 0 0.25rem;
  font-size: 0.8rem;
  font-weight: 800;
`;

export default Role;
