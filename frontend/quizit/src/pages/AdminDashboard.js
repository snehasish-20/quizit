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


function AdminDashboard() {

  const [adminStats, setAdminStats] = useState();
  const [quizList, setQuizList] = useState();
  const [isloading, setIsLoading] = useState(false);


  const navigate = useNavigate();

  if(adminStats && quizList){
    setTimeout(() => {
      setIsLoading(false);
    }, 500);
  }

  useEffect(()=>{
    setIsLoading(true);
    QuizService.getAdminStats().then((res)=>{
      setAdminStats(res.data);
    }).catch((err)=>{
      navigate('/quiz/create')
    })

    QuizService.getAllQuiz().then((res)=> {
      setQuizList(res.data);
    }).catch((err)=>{
      navigate('/quiz/create');
    })

  },[navigate])
  return (
    <>
      <div className='col-11 mx-md-5 mx-3 mt-3'>
        <div className='stats-container col-md-9 col-12 d-flex flex-wrap'>
          <StatsCard statName={"Quizzess created"} statValue={adminStats?.quizCount}/>
          <StatsCard statName={"Registered users"} statValue={adminStats?.usersCount}/>
          <StatsCard statName={"Quiz submissions"} statValue={adminStats?.submissionsCount}/>
        </div>
        <h5 className='text-center'>All Quizzess</h5>
        <QuizList quizList={quizList}/>
        {quizList?.length === 0 && <NoData message={"Looks like you haven't created any quiz yet."}/>}
      </div>
      {isloading && <Loader />}
    </>
  )
}

export default AdminDashboard