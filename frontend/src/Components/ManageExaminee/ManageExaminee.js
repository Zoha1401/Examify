import React from 'react'
import { Link } from 'react-router-dom'
import Button from 'react-bootstrap/Button';

const ManageExaminee = () => {
  return (
    <div>
      Your examinees
      <Link to="/addexaminee"> <Button variant="info">Add Examinee</Button></Link>
    </div>
  )
}

export default ManageExaminee
