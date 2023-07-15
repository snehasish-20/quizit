import React from 'react'
import { Card } from 'react-bootstrap'
import { CheckCircleFill, LightbulbFill, PersonFill } from 'react-bootstrap-icons'

function StatsCard({statName, statValue}) {
  return (
    <Card className='stat-card bg-dark-subtle col-md-3 col-12 me-md-3 mb-3'>
      <Card.Body>
        <Card.Title className='h6 text-white'>
          {statName === "Registered users" && <PersonFill className='mb-1 me-2'/>}
          {(statName === "Quizzess created" || statName === "Quizzess attempted") && <LightbulbFill className='mb-1 me-2'/>}
          {statName === "Quiz submissions" && <CheckCircleFill className='mb-1 me-2'/>}
          {statName}</Card.Title>
        <Card.Text className='h5 text-white'>
          {statValue}
        </Card.Text>
      </Card.Body>
    </Card>
  )
}

export default StatsCard