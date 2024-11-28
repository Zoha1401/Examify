import React from 'react'
import { Link } from 'react-router-dom'
import { Button } from 'react-bootstrap'

const ExaminerDashBoard = () => {
  return (
    <div>
      <h1>This is Examiner Dashboard</h1>
       {/* Dashboard will show a button to create exam and all the existing exams of examiner. Add a filter to show the most recent exam in first and finished exams at end */}
       {/*have a dropdown that will show all examinees assigned to that exam and their respective result if exam done or nothing*/}
       {/*An exam on click will see all the questions in different sections and be able to edit */}
       <Button variant="info">Info<Link to="/manageexaminee">Manage examinees</Link> </Button>
      {/* This will show a list of all examinees added by examiner, where he/she can add, update or delete examinee. Can also assign an exam from here to examinee, also bulk upload examinee*/}
      
      {/* A button create a exam, navigate where fetch all the deta then go to a page where add Mcq question or add programming question.
      Add technical question, add logical question and add programming question. save-> navigate to dashboard  */}
    </div>
  )
}

export default ExaminerDashBoard

//for tomorrow

//Login signup integration with examiner
//Add fetch, delete examinees functionalities integrated with frontend
//Fetch, create exam functionality added

//Next week
//Add Mcq and Programming question