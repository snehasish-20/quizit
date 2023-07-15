import React from 'react'
import { Card } from 'react-bootstrap'
import quiz1 from '../../assets/images/quiz-1.jpg'
import quiz2 from '../../assets/images/quiz-2.jpg'
import quiz3 from '../../assets/images/quiz-3.jpg'
import quiz4 from '../../assets/images/quiz-4.jpg'
import { Button } from 'react-bootstrap'
import { useAuth } from '../../helpers/contexts/AuthContext'
import { PencilSquare, Trash } from 'react-bootstrap-icons'
import '../../stylesheets/QuizListCard.css'
import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import ConfirmationPrompt from '../ConfirmationPrompt'
import QuizService from '../../helpers/http/QuizService'
import ToastMessage from '../ToastMessage'
import { useEffect } from 'react'


function QuizListCard({imageNo, quizName, quizId, score}) {

  let toast = {
    type: "error",
    message: undefined
  }

  const images =[quiz1,quiz2,quiz3,quiz4];
  const auth = useAuth();
  const navigate = useNavigate();

  const [showDeleteDialog, setShowDeleteDialog] = useState(false);
  const [error, setError] = useState();
  const [totalSubmissions, setTotalSubmissions] = useState();
  // const [score, setScore] = useState();


  const handleViewLeaderboard = () => {
    navigate(`/leaderboard?quizId=${quizId}`)
  }

  const handleEditQuiz = ()=> {
    navigate(`/quiz/update?quizId=${quizId}`)
  }

  const handleDeleteQuiz = ()=> {
    setShowDeleteDialog(true);
  }

  const closeDialog = () => {
    setShowDeleteDialog(false);
  }

  const deleteQuiz = () => {
    QuizService.deleteQuiz(quizId).then((res)=>{
      toast.message = "Quiz deleted successfully";
      toast.type= "success";
      setError(toast)
      window.location.reload();
    }).catch((err)=>{
      toast.message = err.response?.data.message;
      setError(toast)
    })
    setShowDeleteDialog(false);
  }

  useEffect(()=> {
    if(auth?.user?.roleType === "ADMIN") {
      QuizService.getQuizSubmissions(quizId).then((res)=> {
        setTotalSubmissions(res.data.length);
      }).catch(()=>{
        navigate('/quiz/create');
      })
    }
    // else if(auth?.user?.roleType === "USER") {
    //   QuizService.getQuizSubmissionByQuizAndUserId(quizId).then((res)=> {
    //     setScore(res.data.totalCorrect);
    //   }).catch(()=>{
    //     navigate('/quiz/create');
    //   })
    // }
  })

  return (
    <>
      <Card className='col-md-3 col-12 mb-4 me-5 quiz-list-card'>
          <Card.Img variant="top" src={images[imageNo]} />
          <Card.Body>
              <div className='d-flex align-items-baseline justify-content-between'>
                <Card.Title className='m-0'>{quizName}</Card.Title>
                {auth?.user?.roleType === "ADMIN" && <div className='ms-3'>
                  <PencilSquare title='Edit quiz' className='me-2 card-icon edit-icon' onClick={()=> handleEditQuiz()}/>
                  <Trash title='delete quiz' className='card-icon delete-icon' onClick={()=> handleDeleteQuiz()}/>
                </div>}
              </div>
              <div className='d-flex flex-column mt-2'>
                {auth?.user?.roleType === "ADMIN" && <span className='fw-semibold'>Submissions: {totalSubmissions}</span>}
                <span className='fw-semibold'>Quiz Id: {quizId}</span>
                {auth?.user?.roleType === "USER" && <span className='fw-semibold'>Your score: {score}</span>}
              </div>
              <div className='mt-2'>
                <Button className='d-block col-12 card-btn' onClick={()=> handleViewLeaderboard()}>View Leaderboard</Button>
              </div>
          </Card.Body>
      </Card>
      {error?.message && <ToastMessage type={error.type} message={error.message}/>}
      {showDeleteDialog && <ConfirmationPrompt title="Delete Quiz" message="Are you sure, you want to delete the quiz?" actionFn={deleteQuiz} closFn={closeDialog}/>}
    </>
  )
}

export default QuizListCard