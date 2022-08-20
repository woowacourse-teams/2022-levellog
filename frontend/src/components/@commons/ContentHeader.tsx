import React from 'react';

import styled from 'styled-components';

import Image from 'components/@commons/Image';

const ContentHeader = ({ imageUrl, title, subTitle, children }: ContentHeaderProps) => {
  const childrenArray = children ? React.Children.toArray(children) : null;

  return (
    <>
      <ContentHeaderStyle>
        <LeftBox>
          <FlexBox>
            {imageUrl && (
              <ImageBox>
                <Image src={imageUrl} sizes={'MEDIUM'} />
              </ImageBox>
            )}
            <TitleBox>
              <Title>{title}</Title>
              {subTitle && <SubTitle>{subTitle}</SubTitle>}
            </TitleBox>
          </FlexBox>
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
  justify-content: space-between;
  position: relative;
  align-items: center;
  width: 100%;
  height: 4.375rem;
  margin-bottom: 3.125rem;
  border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  @media (min-width: 1620px) {
    padding: 0.625rem calc((100vw - 100rem) / 2);
  }
  @media (max-width: 1620px) {
    padding: 0.625rem 1.25rem;
  }
  @media (max-width: 800px) {
    margin-top: 0.625rem;
    margin-bottom: 1.25rem;
    border: none;
    font-size: 0.75rem;
  }
`;

const LeftBox = styled.div`
  display: flex;
  @media (max-width: 800px) {
    flex-direction: column;
    margin-bottom: 0.3125rem;
  }
`;

const ImageBox = styled.div`
  margin-right: 0.625rem;
`;

const FlexBox = styled.div`
  display: flex;
  align-items: center;
  @media (max-width: 800px) {
    margin-bottom: 0.3125rem;
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
  font-weight: 600;
`;

const SubTitle = styled.p`
  font-size: 0.875rem;
  font-weight: 500;
`;

const FilterButtonBox = styled.div`
  display: flex;
  align-items: center;
  gap: 0.625rem;
`;

const ButtonBox = styled.div`
  @media (max-width: 800px) {
    top: 0.625rem;
  }
`;

export default ContentHeader;
