import React from 'react'
import RankingCard from './RankingCard';
import {Row, Col, Stack} from 'react-bootstrap';
import { useEffect } from 'react';
import { useState } from 'react';
import QuizService from '../../helpers/http/QuizService';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../helpers/contexts/AuthContext';
import Loader from '../../components/Loader';


function QuizRanking({quizId}) {
  const[quizResults, setQuizResults] = useState();
  const [isloading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const auth = useAuth();

  useEffect(()=> {
    setIsLoading(true);
    QuizService.getQuizSubmissions(quizId).then((res)=>{
      setTimeout(() => {
        setIsLoading(false);
        setQuizResults(res.data);
      }, 500);

    }).catch((error) => {
      setIsLoading(false);
      const path= auth?.user.roleType === "ADMIN" ? '/admin-dashboard' : '/user-dashboard';
      navigate(path);
    })
  },[quizId, navigate, auth])

  return (<>
    <h3 className='text-center page-title'>Quiz Leader Board (Quiz id: {quizId})</h3>
      <Stack gap={2} className='w-100'>
        <Row className='col-11 col-lg-7 mx-auto p-1'>
          <Col xs="10">
              <span className='col-title'>Name</span>
          </Col>
          <Col xs="2">
              <span className='col-title'>Score</span>
          </Col>
        </Row>
        {quizResults?.length === 0  && <span className='h6 text-center'>No submissions yet.</span>}
        {quizResults?.map((rankDetails, index)=>{
        return <RankingCard rankDetails={rankDetails} rankNo={index+1} key={index}/>
        })}
      </Stack>
      {isloading && <Loader />}
    </>
  )
}

export default QuizRanking;