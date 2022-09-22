import InterviewTeams from 'pages/teams/InterviewTeams';

import InterviewQuestionSearchForm from 'components/interviewQuestion/InterviewQuestionSearchForm';

const Home = () => {
  return (
    <>
      <InterviewQuestionSearchForm />
      <InterviewTeams />
    </>
  );
};

export default Home;
