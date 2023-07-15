import React from 'react'
import { Col, Row } from 'react-bootstrap';
import '../../stylesheets/LeaderBoard.css';
import rank1 from '../../assets/icons/1st.png'
import rank2 from '../../assets/icons/2nd.png'
import rank3 from '../../assets/icons/3rd.png'


function RankingCard({rankDetails, rankNo}) {
  return (
    <Row className='col-lg-7 col-11 mx-auto h-auto p-2 rank-card'>
        <Col xs="10" className='my-auto'>
            <span className='me-2'>{rankNo}.</span>
            <div className={`bg-${rankNo%4} avatar text-center me-2` }>
                <span className='align-middle avatar-text'>{rankDetails?.user.name.substring(0,2).toUpperCase()}</span>
            </div>
            <span>{rankDetails?.user.name}</span>
            {rankNo === 1 && <img src={rank1} alt='rank 1' className='rank-badge'></img>}
            {rankNo === 2 && <img src={rank2} alt='rank 2' className='rank-badge'></img>}
            {rankNo === 3 && <img src={rank3} alt='rank 3' className='rank-badge'></img>}
        </Col>
        <Col xs="2" className='my-auto'>
            <div className='vr me-2' />
            <span>{rankDetails.totalCorrect}</span>
        </Col>
    </Row>
  )
}

export default RankingCard;