import InterviewTeams from 'pages/teams/InterviewTeams';

import QuestionSearchForm from 'components/interviewQuestion/InterviewQuestionSearchForm';

const Home = () => {
  return (
    <>
      <QuestionSearchForm />
      <InterviewTeams />
    </>
  );
};

export default Home;
