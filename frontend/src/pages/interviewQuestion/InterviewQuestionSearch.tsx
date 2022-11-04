import { Suspense } from 'react';

import Loading from 'pages/status/Loading';

import InterviewQuestionSearchForm from 'components/interviewQuestion/InterviewQuestionSearchForm';
import InterviewQuestionSearchResults from 'components/interviewQuestion/InterviewQuestionSearchResults';

const InterviewQuestionSearch = () => {
  return (
    <>
      <InterviewQuestionSearchForm />
      <Suspense fallback={<Loading />}>
        <InterviewQuestionSearchResults />
      </Suspense>
    </>
  );
};

export default InterviewQuestionSearch;
