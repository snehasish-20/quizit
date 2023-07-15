import React from 'react'
import { Button } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import created from '../../assets/images/created.png'

function CreatedQuizDetails({quizId, type}) {
  const navigate = useNavigate();

  const handleCreateAnotherQuiz = () => {
    window.location.reload();
  }

  return (
    <div className="result-page overlay d-flex justify-content-center align-items-center">
        <div className="result-card col-lg-6 col-10 h-50 d-flex flex-column justify-content-center align-items-center">
            <img src={created} alt="Trophy" className='col-lg-2 col-4'></img>
            <span className='h5 d-block mt-4'>Yaay! Your quiz has been {type}</span>
            <span className="text-center h5">Quiz Id: {quizId}</span>
            <span className="text-center h6">In order for users to attempt this quiz, You need to share the quiz id.</span>
            <div className="d-flex flex-wrap flex-md-row flex-column col-lg-8 col-12 justify-content-around align-items-center mt-3">
                <Button className='mb-2 submit-btn' onClick={()=> handleCreateAnotherQuiz()}>Create another Quiz</Button>
                <Button className='mb-2 submit-btn' onClick={()=> navigate('/admin-dashboard')}>View Dashboard</Button>
            </div>
        </div>
    </div>
  )
}

export default CreatedQuizDetails;