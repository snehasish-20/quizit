import React from 'react'
import { Button } from 'react-bootstrap'
import QuizListCard from './QuizListCard'
import { PlusCircle } from 'react-bootstrap-icons'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../helpers/contexts/AuthContext'

function QuizList({quizList}) {

  const navigate = useNavigate();
  const auth = useAuth();

  const handleNewQuizBtnClick = () => {
    navigate('/quiz/create')
  }

  return (
    <div className='col-12'>
      {auth?.user?.roleType=== "ADMIN" && <Button className='create-btn' onClick={()=> handleNewQuizBtnClick()}><PlusCircle className='mb-1'/> Create new quiz</Button>}

      <div className='d-flex flex-wrap mt-3'>
        {quizList?.map((quiz, index)=>{
          return <QuizListCard imageNo={index%4} quizId={quiz.quizId} quizName={quiz.quizName} score={quiz.score} key={quiz.quizId}/>
        })}
      </div>
    </div>
  )
}

export default QuizList