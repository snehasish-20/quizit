import React from 'react'
import QuestionCard from './QuestionCard'
import { useState } from 'react'
import Loader from '../Loader';
import QuizService from '../../helpers/http/QuizService';
import ToastMessage from '../ToastMessage';
import QuizResult from './QuizResult';

function QuizQuestions({Quiz, setQuizDetails}) {
  let toast = {
    type: "error",
    message: undefined
  }

  const [currentQuestionNo, setCurrentQuestionNo] = useState(0);
  const [loading, setLoading] = useState(false);
  const [score, setScore] = useState();
  const [error, setError] = useState();

  const handleQuizSubmission = (selection)=> {
    Quiz.questions[currentQuestionNo].submittedAnswer = selection;
    
    if(currentQuestionNo === Quiz?.totalQuestions-1){
      setLoading(true);
      QuizService.submitQuiz(Quiz.quizId, Quiz.questions).then((res)=> {
        setTimeout(() => {
          setLoading(false);
          setScore(res.data.totalCorrect);
         }, 500);

      }).catch((err)=> {
        toast.message = err.response?.data.message;

        setTimeout(() => {
          setLoading(false);
          setError(toast);
         }, 500);
      })


      return;
    }

    setCurrentQuestionNo(()=> currentQuestionNo+1);
  }

  return (
    <div className='quiz'>
      <span className='h4 text-center d-block text-light py-2'>{Quiz?.quizName}</span>
      <QuestionCard question={Quiz?.questions[currentQuestionNo]} questionNo={currentQuestionNo+1} totalQuestions={Quiz?.totalQuestions} setCurrentQuestionNo={setCurrentQuestionNo} handleQuizSubmission={handleQuizSubmission}/>
      {loading && <Loader />}
      {score && <QuizResult score={score} totalQuestions={Quiz?.totalQuestions} setQuizDetails={setQuizDetails} quizId={Quiz.quizId}/>}
      {error?.message && <ToastMessage type={error.type} message={error.message}/>}
    </div>
    
  )
}

export default QuizQuestions