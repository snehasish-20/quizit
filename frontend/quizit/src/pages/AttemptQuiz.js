import React from 'react'
import QuizLanding from '../components/Quiz/QuizLanding'
import "../stylesheets/Quiz.css"
import { useState } from 'react'
import QuizQuestions from '../components/Quiz/QuizQuestions';
import '../stylesheets/CreateUpdateQuiz.css'

function AttemptQuiz() {
  const [quizDetails, setQuizDetails] = useState();

  return (
    <div>
    {!quizDetails && <QuizLanding setQuizDetails={setQuizDetails}/>}
    {quizDetails && <QuizQuestions Quiz={quizDetails} setQuizDetails={setQuizDetails}/>}
    {/* {loading && <Loader />} */}
    </div>
  )
}

export default AttemptQuiz