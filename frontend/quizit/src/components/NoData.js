import React from 'react'
import empty from '../assets/images/no-data.svg';

function NoData({message}) {
  return (
    <div className='w-100 d-flex flex-column align-items-center'>
        <img src={empty} className='col-md-3 col-8' alt="no data found"></img>
        <span className='fw-semibold'>{message}</span>
    </div>
  )
}

export default NoData;