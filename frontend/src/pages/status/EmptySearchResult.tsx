import { Exception } from 'pages/status';

import emptyImage from 'assets/images/empty.webp';

import FlexBox from 'components/@commons/FlexBox';

const EmptySearchResult = () => {
  return (
    <Exception>
      <Exception.Image sizes={'EXCEPTION'}>{emptyImage}</Exception.Image>
      <Exception.Title>일치하는 검색결과가 없습니다.</Exception.Title>
      <FlexBox flexFlow={'column'} alignContent={'center'} gap={0.5}>
        <Exception.Text>모든 단어의 철자가 정확한지 확인하세요.</Exception.Text>
        <Exception.Text>다른 검색어를 사용해 보세요.</Exception.Text>
        <Exception.Text>더 일반적인 검색어를 사용해 보세요.</Exception.Text>
        <Exception.Text>키워드 수를 줄여보세요.</Exception.Text>
      </FlexBox>
    </Exception>
  );
};

export default EmptySearchResult;
