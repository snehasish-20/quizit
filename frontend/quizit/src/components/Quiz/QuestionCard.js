import React from 'react'
import { useState } from 'react';
import { Button } from 'react-bootstrap'

function QuestionCard({question, questionNo, totalQuestions, setCurrentQuestionNo, handleQuizSubmission}) {

  const [userSelection, setUserSelection] = useState();
  const [selectedBtn, setSelectedBtn] = useState(0);

  const handleAnswerSelection = (selectedAnswer, btn) => {
    setUserSelection(selectedAnswer);
    setSelectedBtn(btn)
  }

  const handleNext = () => {
    setSelectedBtn(0);
    handleQuizSubmission(userSelection);
  }
  return (
    <div className="questions">
      <div className="quizContainer mx-auto">
        <div className="questionContainer">
        <div className="questionNo text-center">
            <span>{questionNo}/{totalQuestions}</span>
        </div>
        <p className="question">{question?.question}</p>
        <span className="mark">?</span>
        </div>
        <div className="btnContainer">
        {question?.option1 && <button className={selectedBtn===1 ?"answers selected":"answers"} onClick={()=> handleAnswerSelection(question.option1, 1)}>{question?.option1}</button>}
        {question?.option2 && <button className={selectedBtn===2 ?"answers selected":"answers"} onClick={()=> handleAnswerSelection(question.option2, 2)}>{question?.option2}</button>}
        {question?.option3 && <button className={selectedBtn===3 ?"answers selected":"answers"} onClick={()=> handleAnswerSelection(question.option3, 3)}>{question?.option3}</button>}
        {question?.option4 && <button className={selectedBtn===4 ?"answers selected":"answers"} onClick={()=> handleAnswerSelection(question.option4, 4)}>{question?.option4}</button>}
        </div>
        <div className='my-2 mb-2 next-btn w-100 d-flex justify-content-center'>
            <Button onClick={()=> handleNext()} className="submit-btn">{questionNo === totalQuestions ? "Submit" : "Next"}</Button>
        </div>
      </div>
    </div>
  )
}

export default QuestionCard