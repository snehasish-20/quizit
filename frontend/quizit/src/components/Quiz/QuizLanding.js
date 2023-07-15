import React from 'react'
import logo from '../../assets/images/logo.png'
import { Button, Form } from 'react-bootstrap';
import { useRef } from 'react';
import { useState } from 'react';
import ToastMessage from '../ToastMessage';
import QuizService from '../../helpers/http/QuizService'
import Loader from '../Loader';

function QuizLanding({setQuizDetails}) {

  let toast = {
    type: "error",
    message: undefined
  }

  const quiz = useRef();
  const [error, setError] = useState();
  const [loading, setLoading] = useState(false);

  const loadQuizData = (quizId) => {
    setLoading(true);
    setError(null)
    QuizService.getQuizById(quizId).then((res)=> {
      setTimeout(() => {
        setLoading(false);
        setQuizDetails(res?.data);
       }, 500);
    }).catch((err)=> {
      toast.message = err.response?.data.message;
      
      setTimeout(() => {
        setLoading(false);
        setError(toast);
      }, 500);
    })
  }

  const validateQuizId = (quiz) => {
    if(!quiz.current.value){
      toast.message = "Quiz id is required";
      setError(toast);
      return false;
    }

    return true;
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    const isQuizIdValid = validateQuizId(quiz);    

    if(isQuizIdValid) {
      loadQuizData(quiz.current.value);
    }
  }

  return (
    <div className="quiz-landing d-flex flex-column justify-content-center align-items-center">
      <img src={logo} alt="logo" className="logo"/>
      <p className="col-md-4 col-10 text-center">Are you ready to proove yourself a genius? Get ready to challenge yourself</p>
      <Form className="col-md-4 col-10">
        <Form.Control type="text" placeholder="Enter Quiz Id" className="input text-center fw-medium" ref={quiz}/>
        <Button variant="primary" type="submit" className='w-100 mt-3 submit-btn' onClick={(e)=> handleSubmit(e)}>
            Attempt Quiz
        </Button>
      </Form>
      {error?.message && <ToastMessage type={error.type} message={error.message}/>}
      {loading && <Loader />}
    </div>
  )
}

export default QuizLanding