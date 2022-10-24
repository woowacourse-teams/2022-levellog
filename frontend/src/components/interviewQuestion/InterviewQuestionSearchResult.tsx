import styled from 'styled-components';

import goodActiveIcon from 'assets/images/good-active.svg';
import goodIcon from 'assets/images/good.svg';

import { InterviewQuestionLikeRequestType } from 'apis/InterviewQuestionSearch';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';

const InterviewQuestionSearchResult = ({
  InterviewQuestion,
  onClickLikeButton,
  onClickCancelLikeButton,
}: InterviewQuestionSearchResultProps) => {
  const { id, content, like, likeCount } = InterviewQuestion;

  const handleClickLikeButton = () => {
    onClickLikeButton({ InterviewQuestionId: id });
  };

  const handleClickCancelLikeButton = () => {
    onClickCancelLikeButton({ InterviewQuestionId: id });
  };

  return (
    <S.Container role={'listitem'}>
      <S.ResultText tabIndex={0}>{content}</S.ResultText>
      <FlexBox flexFlow={'row'} alignItems={'center'}>
        <S.LikeCountText
          aria-live={'polite'}
          aria-atomic={'true'}
          aria-label={`현재 좋아요 개수 ${likeCount}개`}
          tabIndex={0}
        >
          {likeCount}
        </S.LikeCountText>
        <S.LikeButton
          type={'button'}
          onClick={like ? handleClickCancelLikeButton : handleClickLikeButton}
        >
          <Image
            src={like ? goodActiveIcon : goodIcon}
            alt={like ? '이미 누른 좋아요' : '누르지 않은 좋아요'}
            sizes={'SMALL'}
          />
        </S.LikeButton>
      </FlexBox>
    </S.Container>
  );
};

interface InterviewQuestionSearchResultProps {
  InterviewQuestion: any;
  onClickLikeButton: ({
    InterviewQuestionId,
  }: Pick<InterviewQuestionLikeRequestType, 'InterviewQuestionId'>) => void;
  onClickCancelLikeButton: ({
    InterviewQuestionId,
  }: Pick<InterviewQuestionLikeRequestType, 'InterviewQuestionId'>) => void;
}

const S = {
  Container: styled.li`
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    padding: 0.4375rem 0.4375rem 0.4375rem 0.75rem;
    margin-bottom: 1rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  ResultText: styled.p`
    font-size: 1.25rem;
  `,

  LikeCountText: styled.p`
    width: max-content;
    margin-left: 0.625rem;
    font-size: 1.25rem;
  `,

  LikeImage: styled.img`
    width: 1.25rem;
    height: 1.25rem;
  `,

  LikeButton: styled.button`
    border: none;
    background-color: ${(props) => props.theme.new_default.WHITE};
  `,
};

export default InterviewQuestionSearchResult;
