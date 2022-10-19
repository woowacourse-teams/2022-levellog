import { Exception } from 'pages/status';

import notFound from 'assets/images/notFound.webp';

import FlexBox from 'components/@commons/FlexBox';

const NotFound = () => {
  return (
    <Exception>
      <Exception.Image sizes={'EXCEPTION'}>{notFound}</Exception.Image>
      <FlexBox flexFlow={'column'} alignItems={'center'} gap={0.5}>
        <Exception.Title>원하시는 페이지를 찾을 수 없습니다.</Exception.Title>
        <Exception.Text>요청하신 페이지가 사라졌거나, 잘못된 주소로 접근하였습니다.</Exception.Text>
      </FlexBox>
    </Exception>
  );
};

export default NotFound;
