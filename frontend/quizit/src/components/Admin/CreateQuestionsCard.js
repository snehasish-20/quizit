import React from 'react'
import { useRef } from 'react'
import { Form, InputGroup } from 'react-bootstrap'

function CreateQuestionsCard({quiz, index}) {

  let questionDetails = quiz?.questions[index];

  const question = useRef();
  const option1 = useRef();
  const option2 = useRef();
  const option3 = useRef();
  const option4 = useRef();
  const correctAnswer = useRef();

  const handleFormValuesChange = () => {
    const questionStructure = {
      question: question.current.value,
      option1: option1.current.value,
      option2: option2.current.value,
      option3: option3.current.value,
      option4: option4.current.value,
      answer: correctAnswer.current.value
    }
    quiz.questions[index] = questionStructure;
  }

  return (
    <div className={`question-container bg-light-subtle px-lg-5 px-2 py-2 mt-3 pt-4 border-style-${(index+1)%4}`}>
      <span className='h6 mb-3 d-block'>Question {(index+1)}.</span>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Question <span className='text-danger'>*</span></InputGroup.Text>
        <Form.Control type="text" placeholder="Enter question" className="input" defaultValue={questionDetails?.question} ref={question} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Option 1 <span className='text-danger'>*</span></InputGroup.Text>
        <Form.Control type="text" placeholder="Enter option 1" className="input" defaultValue={questionDetails?.option1} ref={option1} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Option 2 <span className='text-danger'>*</span></InputGroup.Text>
        <Form.Control type="text" placeholder="Enter option 2" className="input" defaultValue={questionDetails?.option2} ref={option2} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Option 3</InputGroup.Text>
        <Form.Control type="text" placeholder="Enter option 3" className="input" defaultValue={questionDetails?.option3} ref={option3} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Option 4</InputGroup.Text>
        <Form.Control type="text" placeholder="Enter option 4" className="input" defaultValue={questionDetails?.option4} ref={option4} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
      <InputGroup className="mb-3">
        <InputGroup.Text id="basic-addon1">Answer <span className='text-danger'>*</span></InputGroup.Text>
        <Form.Control type="text" placeholder="Enter answer" className="input" defaultValue={questionDetails?.answer} ref={correctAnswer} onChange={()=> handleFormValuesChange()}/>
      </InputGroup>
    </div>
  )
}

export default CreateQuestionsCard