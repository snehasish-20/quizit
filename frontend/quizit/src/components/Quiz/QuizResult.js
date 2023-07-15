import React from 'react'
import { Button } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import trophy from '../../assets/images/trophy.png'

function QuizResult({score, totalQuestions, setQuizDetails, quizId}) {
  const navigate = useNavigate();

  return (
    <div className="result-page overlay d-flex justify-content-center align-items-center">
        <div className="result-card col-lg-6 col-10 h-50 d-flex flex-column justify-content-center align-items-center">
            <img src={trophy} alt="Trophy" className='col-lg-2 col-4'></img>
            <span className='h5 d-block mt-2'>Wohoo! Here are the results</span>
            <span className="text-center h6">Your score:<br/>{score}/{totalQuestions}</span>
            <div className="d-flex flex-wrap col-lg-8 col-12 justify-content-around align-items-center mt-3">
                <Button className='mb-2 submit-btn' onClick={()=> setQuizDetails(null)}>Attempt another Quiz</Button>
                <Button className='mb-2 submit-btn' onClick={()=> navigate(`/leaderboard?quizId=${quizId}`)}>View Quiz Leader Board</Button>
            </div>
        </div>
    </div>
  )
}

export default QuizResult