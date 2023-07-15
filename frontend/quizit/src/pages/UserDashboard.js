import React from 'react'
import StatsCard from '../components/dashboard/StatsCard'
import '../stylesheets/Dashboard.css'
import QuizList from '../components/dashboard/QuizList'
import { useEffect } from 'react'
import { useState } from 'react'
import QuizService from '../helpers/http/QuizService'
import { useNavigate } from 'react-router-dom'
import Loader from '../components/Loader';
import NoData from '../components/NoData'


function UserDashboard() {

  const [userStats, setUserStats] = useState();
  const [quizList, setQuizList] = useState();
  const [isloading, setIsLoading] = useState(false);


  const navigate = useNavigate();

  if(userStats && quizList){
    setTimeout(() => {
      setIsLoading(false);
    }, 500);
  }

  useEffect(()=>{
    setIsLoading(true);
    QuizService.getUserStats().then((res)=>{
      setUserStats(res.data);
    }).catch((err)=>{
      navigate('/quiz')
    })

    QuizService.getQuizSubmissionsByUserId().then((res)=> {
      setQuizList(res.data);
    }).catch((err)=>{
      navigate('/quiz');
    })
  },[navigate])

  return (
    <>
      <div className='col-11 mx-md-5 mx-3 mt-3'>
        <div className='stats-container col-md-9 col-12 d-flex flex-wrap'>
          <StatsCard statName={"Quizzess attempted"} statValue={userStats?.submissionsCount}/>
        </div>
        <h5 className='text-center'>Attempted Quizzess</h5>
        {quizList?.length === 0 && <NoData message={"Looks like you haven't attempted any quiz yet."}/>}
        <QuizList quizList={quizList}/>
      </div>
      {isloading && <Loader />}
    </>
    
  )
}

export default UserDashboard;