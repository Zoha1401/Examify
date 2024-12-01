import React from 'react'
import { Link } from 'react-router-dom'

const LandingPage = () => {
  return ( 
    <><div>
      <h1>Are you the examiner or examinee?</h1>
      <button><Link to="/examiner-signin">Examiner</Link></button>
      <button><Link to="/examinee-login">Examinee</Link></button>
      </div></>
  )
}

export default LandingPage

