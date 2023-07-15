import React, { useRef } from 'react'
import signup from "../../assets/images/signup.svg"
import "../../stylesheets/Signin.css"
import { Button, Form } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import ToastMessage from '../ToastMessage'
import { useState } from 'react'
import UserService from '../../helpers/http/UserService'
import Loader from '../Loader';


function Signup() {
  const email = useRef();
  const password = useRef();
  const name = useRef();
  
  let toast = {
    type: "error",
    message: undefined
  }

  const [toastMessage, setToastMessage] = useState();
  const [isloading, setIsLoading] = useState(false);


  const handleSubmit = (e) => {
    setToastMessage(null);
    e.preventDefault();

    const userEmail = email.current.value;
    const userPassword = password.current.value;
    const userName = name.current.value;

    var isEmailValid = validateEmail(userEmail);
    var isPasswordValid = validatePassword(userPassword);
    var isNameValid = validateName(userName);

    if(isEmailValid && isPasswordValid && isNameValid) {
      setIsLoading(true);
      const userData = {
        "name": userName,
        "email": userEmail,
        "password": userPassword
      }

      UserService.registerUser(userData).then((res)=>{
        setTimeout(() => {
          setIsLoading(false);
          toast.type = "success";
          toast.message = "User registered, Signin to continue";
          setToastMessage(toast);
        }, 500);

      }).catch((err) => {
        setIsLoading(false);
        toast.type = err.response.data.status;
        toast.message = err.response.data.message;
        setToastMessage(toast);
      })
    }
  }

  const validateEmail = (email) => {
    const isEmailValid = String(email)
      .toLowerCase()
      .match(
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      );

    if(!isEmailValid){
      toast.type = "error"
      toast.message = "Please enter a valid email";
      setToastMessage(toast)
    }

    return isEmailValid;
  };

  const validatePassword = (password) => {
    const isPasswordValid = String(password)
      .toLowerCase()
      .match(
        /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,}$/
      );

    if(!isPasswordValid){
      toast.type = "error"
      toast.message = "Password must containe combination of letters, digits and symbol";
      if(password.length < 6){
        toast.message = "Password should have atleast 6 characters";
      }
      setToastMessage(toast)
    }

    return isPasswordValid;
  };

  const validateName = (name) => {
    if(!name){
      toast.type = "error"
      toast.message = "Please enter your name";
      setToastMessage(toast)
    }
    
    return name;
  };

  return (
    <div className='row w-100 signin'>
        <div className='col-lg-6 d-lg-flex d-none align-items-center justify-content-center banner'>
            <img src={signup} alt="signin" className='w-75'/>
        </div>
        <div className="col-lg-6 signin-container d-flex flex-column justify-content-center align-items-center pt-lg-0 pt-1">
          <div className='col-lg-8 col-10 mb-4'>
            <h1 className='app-name'>QuizIT</h1>
            <h3 className='form-heading'>Sign Up</h3>
          </div>
          <Form className="col-lg-8 col-10">
            <Form.Group className="mb-2" controlId="formBasicEmail">
              <Form.Label>Name <span className='text-danger'>*</span></Form.Label>
              <Form.Control type="text" placeholder="Enter your name" className="input" ref={name}/>
            </Form.Group>

            <Form.Group className="mb-2" controlId="formBasicText">
              <Form.Label>Email address <span className='text-danger'>*</span></Form.Label>
              <Form.Control type="email" placeholder="Enter email" className="input" ref={email}/>
              <Form.Text className="text-muted">
                We'll never share your email with anyone else.
              </Form.Text>
            </Form.Group>

            <Form.Group className="mb-1" controlId="formBasicPassword">
              <Form.Label>Password <span className='text-danger'>*</span></Form.Label>
              <Form.Control type="password" placeholder="Password" className="input" ref={password}/>
            </Form.Group>
            <Button variant="primary" type="submit" className='w-100 mt-3 submit-btn' onClick={(e)=> handleSubmit(e)}>
              Sign in
            </Button>
            <span className='mt-2 mx-auto col-lg-8 col-11 d-block'>Aldready registered? <Link className="primary-link" to="/signin">Sign-in here</Link></span>
          </Form>
        </div>
        {toastMessage?.message && <ToastMessage message={toastMessage.message} type={toastMessage.type}/>}
        {isloading && <Loader />}
    </div>
  )
}

export default Signup