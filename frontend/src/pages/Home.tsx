import { Suspense } from 'react';

import Loading from 'pages/status/Loading';
import InterviewTeams from 'pages/teams/InterviewTeams';

import QuestionSearchForm from 'components/interviewQuestion/InterviewQuestionSearchForm';

const Home = () => {
  return (
    <>
      <QuestionSearchForm />
      <Suspense fallback={<Loading />}>
        <InterviewTeams />
      </Suspense>
    </>
  );
};

export default Home;
