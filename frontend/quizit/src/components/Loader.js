import React from 'react'
import { Spinner } from 'react-bootstrap'
import '../stylesheets/loader.css'

function Loader() {
  return (
    <div className="loader overlay d-flex justify-content-center align-items-center">
      <div>
        <span className='me-2 h5 text-light'>Please wait...</span>
        <Spinner animation="border" variant="light" />
      </div>
    </div>
  )
}

export default Loader