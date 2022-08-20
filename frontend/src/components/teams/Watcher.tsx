import styled from 'styled-components';

import Image from 'components/@commons/Image';

const Watcher = ({ watcher }: WatcherProps) => {
  return (
    <S.Container>
      <Image src={'https://avatars.githubusercontent.com/u/28749734?v=4'} sizes={'SMALL'} />
      <S.Text>kk</S.Text>
    </S.Container>
  );
};

interface WatcherProps {
  watcher: any;
}

const S = {
  Container: styled.div`
    display: flex;
    align-items: center;
    gap: 1rem;
    padding: 0.625rem 0 0.375rem 0.625rem;
    width: 17.5rem;
    height: 60px;
    border-radius: 0.625rem;
    background-color: ${(props) => props.theme.new_default.WHITE};
    box-shadow: 0.0625rem 0.25rem 0.625rem ${(props) => props.theme.new_default.GRAY};
  `,

  Text: styled.p`
    font-size: 1.5rem;
  `,
};

export default Watcher;
