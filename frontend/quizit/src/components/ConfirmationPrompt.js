import React from 'react'
import { Button, Modal } from 'react-bootstrap'
import '../stylesheets/ConfirmationPrompt.css'

function ConfirmationPrompt({title, message, actionFn, closFn}) {
  return (
    <>
    {title && 
    <div className="modal show prompt-modal" style={{ display: 'block', position: 'fixed' }}>
        <Modal.Dialog className='prompt-dialog' centered>
        <Modal.Header>
            <Modal.Title className='text-danger'>{title}</Modal.Title>
        </Modal.Header>

        <Modal.Body>
            <p>{message}</p>
        </Modal.Body>

        <Modal.Footer>
            <Button variant="secondary" className='col-2' onClick={()=> closFn()}>No</Button>
            <Button className='col-2 border-0 bg-danger' onClick={()=> actionFn()}>Yes</Button>
        </Modal.Footer>
        </Modal.Dialog>
      </div>}
    </>
  )
}

export default ConfirmationPrompt