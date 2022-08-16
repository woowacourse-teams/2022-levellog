import React from 'react';

import styled from 'styled-components';

import Button from 'components/@commons/Button';
import Image from 'components/@commons/Image';

const ContentHeader = ({ imageUrl, title, subTitle, children }: ContentHeaderProps) => {
  const childrenArray = children ? React.Children.toArray(children) : null;

  return (
    <>
      <ContentHeaderStyle>
        <LeftBox>
          {imageUrl && <Image src={imageUrl} sizes={'MEDIUM'} margin={'0 10px 0 0'} />}
          <TitleBox>
            <Title>{title}</Title>
            {subTitle && <SubTitle>{subTitle}</SubTitle>}
          </TitleBox>
          <FilterButtonBox>
            {childrenArray &&
              childrenArray.length === 2 &&
              (childrenArray[0] as JSX.Element).props.children}
          </FilterButtonBox>
        </LeftBox>
        <ButtonBox>
          {childrenArray && (childrenArray.length === 2 ? childrenArray[1] : childrenArray[0])}
        </ButtonBox>
      </ContentHeaderStyle>
      <Line />
    </>
  );
};

interface ContentHeaderProps {
  imageUrl?: string;
  title: string;
  subTitle?: string;
  children?: JSX.Element[] | JSX.Element;
}

const ContentHeaderStyle = styled.div`
  display: flex;
  position: relative;
  align-items: center;
  width: 100%;
  height: fit-content;
  padding: 0.625rem 0;
  @media (max-width: 560px) {
    justify-content: start;
    font-size: 0.75rem;
  }
`;

const LeftBox = styled.div`
  display: flex;

  @media (max-width: 560px) {
    flex-direction: column;
  }
`;

const TitleBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  gap: 0.3125rem;
  margin-right: 1.25rem;
`;

const Title = styled.h1`
  font-size: 1.25rem;
  font-weight: 400;
`;

const SubTitle = styled.p`
  font-size: 1rem;
  font-weight: 300;
`;

const FilterButtonBox = styled.div`
  display: flex;
  align-items: center;
  gap: 0.625rem;
`;

const ButtonBox = styled.div`
  position: absolute;
  right: 0;
`;

const Line = styled.div`
  position: absolute;
  left: 0;
  width: 100%;
  border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
`;

export default ContentHeader;
