import React from 'react'
import { useSearchParams } from 'react-router-dom';
import QuizRanking from '../components/Leaderboard/QuizRanking';
import NotFound from './NotFound';


function LeaderBoard() {

  const [searchParam] = useSearchParams();
  const quizId = searchParam.get("quizId");
  return (
    <div className='pt-4 leaderboard'>
        {!quizId && <NotFound/>}
        {quizId && <QuizRanking quizId={quizId}/>}
    </div>
  )
}

export default LeaderBoard