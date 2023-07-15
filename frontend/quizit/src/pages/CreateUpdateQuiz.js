import React from 'react'
import CreateQuiz from '../components/Admin/CreateQuiz'
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom'
import { useState } from 'react';
import QuizService from '../helpers/http/QuizService';
import { useEffect } from 'react';
import { useAuth } from '../helpers/contexts/AuthContext';
import Loader from '../components/Loader';


function CreateUpdateQuiz() {

  const[quiz,setQuiz] = useState();
  const [isloading, setIsLoading] = useState(false);


  const auth = useAuth();

  const navigate = useNavigate();

  const location = useLocation();
  const [searchParam] = useSearchParams();
  const path = location.pathname.startsWith('/quiz/update');

  useEffect(()=>{
    if(path){
        setIsLoading(true)
        const quizId = searchParam.get("quizId");
        QuizService.getQuizById(quizId).then((res)=> {
            setQuiz(res.data);
            setTimeout(() => {
              setIsLoading(false);
            }, 500);
            
        }).catch((err)=>{
            const path = auth?.user?.roleType === "ADMIN" ? '/admin-dashboard' : '/user-dashboard';
            navigate(path)
        })
    }
  },[path, searchParam, auth, navigate])

  return (
    <>
      <CreateQuiz quiz={quiz} setLoading={setIsLoading}/>
      {isloading && <Loader />}
    </>
  )
}

export default CreateUpdateQuiz