import styled from 'styled-components';

import InterviewerImage from 'assets/images/group.png';
import MicImage from 'assets/images/mic.png';

import Image from 'components/@commons/Image';

const Role = ({ role }: RoleProp) => {
  return (
    <RoleStyle role={role}>
      {role === '인터뷰어' ? (
        <Image src={InterviewerImage} sizes={'SMALL'} />
      ) : (
        <Image src={MicImage} sizes={'SMALL'} />
      )}
      <RoleText>{`나의 ${role}`}</RoleText>
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
  top: -0.625rem;
  left: -3.125rem;
  width: 7.125rem;
  height: 2.1875rem;
  padding: 0.1875rem;
  box-shadow: 0.25rem 0.25rem 0.25rem ${(props) => props.theme.default.GRAY};
  border-style: none;
  border-radius: 1.5625rem;
  background-color: ${(props) => {
    if (props.role === '인터뷰어') {
      return props.theme.default.WINE;
    }
    return props.theme.default.BLUE;
  }};
`;

const RoleText = styled.p`
  color: ${(props) => props.theme.default.WHITE};
  margin-left: 1px;
  font-size: 0.8rem;
  font-weight: 800;
`;

export default Role;
