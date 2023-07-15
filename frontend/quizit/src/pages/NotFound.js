import React from 'react'
import ErrorPage from '../assets/images/not-found.jpg'

function NotFound() {
  return (
    <div className='bg-white error-page d-flex flex-column align-items-center w-100'>
        <img src={ErrorPage} alt='Page not found' className='d-block col-6'></img>
        <span className='h5'>Oops! Page not found</span>
    </div>
  )
}

export default NotFound