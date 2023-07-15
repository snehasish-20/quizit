import React from 'react'
import CreateQuestionsCard from './CreateQuestionsCard'
import { Button, Form, InputGroup } from 'react-bootstrap'
import { useRef } from 'react'
import { useState } from 'react';
import QuizService from '../../helpers/http/QuizService';
import { useEffect } from 'react';
import ToastMessage from '../ToastMessage';
import CreatedQuizDetails from './CreatedQuizDetails';

function CreateQuiz({quiz, setLoading}) {

  let toast = {
    type: "error",
    message: undefined
  }

  let quizStructure = {
    quizName: undefined,
    quizActive: true,
    totalQuestions: 0,
    questions: []
  }

  const questionStructure = {
    question: undefined,
    option1: undefined,
    option2: undefined,
    option3: undefined,
    option4: undefined,
    answer: undefined
  }

  let quizDetails = quiz || quizStructure;

  const [Quiz, SetQuiz] =useState();
  const [toastMessage, setToastMessage] = useState();
  const [showCreatedQuizDialog, setShowCreatedQuizDialog] = useState();

  const name = useRef();
  const noOfQuestions = useRef();
  const isQuizActive = useRef();

  useEffect(()=>{
    if(quiz){
        SetQuiz(quiz);
    }
  },[quiz])

  const handleTotalQuestionsChange = () => {
    setToastMessage(null);
    if(noOfQuestions?.current?.value <= 0){
        toast.message = "Quiz should have atleast 1 question";
        setToastMessage(toast);
    }
    quizStructure.questions = [];
    for(let i=0; i<noOfQuestions?.current?.value; i++){
        quizStructure.questions.push(questionStructure);
    }
    quizDetails = quizStructure;
    SetQuiz(quizDetails);
  }

  const validateQuizData = () =>{
    let isQuizDetailsValid = false;
    let isQuestionsDetailsValid = true;

    if(quizDetails.quizName && quizDetails.totalQuestions >0){
        isQuizDetailsValid = true;
    } 
    if(quizDetails.quizName && quizDetails.totalQuestions) {
        for(let i=0;i<Quiz.questions?.length;i++){
            const question = quizDetails?.questions[i];
            if(!question){
                isQuestionsDetailsValid = false;
                break;
            } else if(!question.question || !question.option1 || !question.option2){
                isQuestionsDetailsValid = false;
                break;
            }
        }
    }

    if(isQuizDetailsValid && isQuestionsDetailsValid){
        return true;
    }
    else {
        toast.message = "Please fill all the required fields"
        setToastMessage(toast);
        return false;
    }
  }

  const handleSubmit = () => {
    setToastMessage(null);
    quizDetails.quizName = name.current.value;
    quizDetails.totalQuestions = noOfQuestions.current.value;
    quizDetails.quizActive = isQuizActive.current.checked;
    
    let isFormValid = false;
    isFormValid = validateQuizData();

    if(isFormValid){
        setLoading(true);
        if(quiz){
            QuizService.updateQuiz(quizDetails).then((res)=>{
                setTimeout(() => {
                    toast.type = "success";
                    toast.message = "Quiz updated successfully";
                    setToastMessage(toast);
                    setShowCreatedQuizDialog(res.data.quizId);
                    setLoading(false);
                }, 500);
                
            }).catch((err)=>{
                setLoading(false);
                toast.message = err.response.data;
                setToastMessage(toast);
            })
        } else {
            QuizService.createQuiz(quizDetails).then((res)=> {
                setTimeout(() => {
                    setLoading(false);
                    toast.type = "success";
                    toast.message = "Quiz created successfully";
                    setToastMessage(toast);
                    setShowCreatedQuizDialog(res.data.quizId);
                  }, 500);
            }).catch((err)=>{
                setLoading(false);
                toast.message = err.response.data;
                setToastMessage(toast);
            })
        }
    }
  }

  return (
    <div className='d-flex justify-content-center'>
        <div className="background text-center">
            <span className='h4 text-light mt-3 d-block'>{quiz? "Update" : "Create"} Quiz</span>
        </div>
        <div className='quiz-details col-lg-8 col-10 mx-auto'>
            <div className='create-quiz px-5 py-2'>
                <Form.Group className="mb-2 col-10">
                    <Form.Label className='input-label'>Quiz name <span className='text-danger'>*</span></Form.Label>
                    <Form.Control type="text" placeholder="Enter Quiz name" className="input quiz-details-input" defaultValue={quiz?.quizName} ref={name}/>
                </Form.Group>
                <InputGroup className="mb-3">
                    <Form.Group className="mb-2 me-4">
                        <Form.Label className='input-label'>No of questions <span className='text-danger'>*</span></Form.Label>
                        <Form.Control type="number" placeholder="Enter total questions" className="input quiz-details-input" defaultValue={quiz?.totalQuestions} disabled={quiz?.totalQuestions} ref={noOfQuestions} onChange={()=> handleTotalQuestionsChange()}/>
                    </Form.Group>
                    <Form.Group className="mb-2">
                        <Form.Label className='input-label'>Quiz Active status</Form.Label>
                        <Form.Check
                            type='switch'
                            defaultChecked={quiz?.quizActive}
                            ref={isQuizActive}
                        />
                    </Form.Group>
                </InputGroup>
            </div>
            <div className='bg-light'>
                
            </div>
            {Quiz?.questions.map((question,index)=> {
                return <CreateQuestionsCard quiz={quizDetails} key={index} index={index} />
            })}
            <Button onClick={()=> handleSubmit()} className='mx-auto d-block col-4 mt-3 mb-5 create-quiz-btn'>{quiz? "Update" : "Create"}</Button>
        </div>
        {toastMessage?.message && <ToastMessage type={toastMessage.type} message={toastMessage.message}/>}
        {showCreatedQuizDialog && <CreatedQuizDetails quizId={showCreatedQuizDialog}  type={quiz? "updated" : "created"}/>}
    </div>
  )
}

export default CreateQuiz