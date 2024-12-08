import React from 'react';
import TestCase from './TestCase';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../../utils/axiosInstance';
import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';

const ProgrammingQuestion = ({pq, onDelete}) => {

  const json = useParams();
  console.log(json.examId);
  const examId=json.examId;
  console.log(examId)

  let navigate=useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  
  const handleDeleteProgrammingQuestion=async()=>{
     try{
        const response= await axiosInstance.delete(`/programmingQuestion/deleteProgrammingQuestion?examId=${examId}&proId=${pq.programmingQuestionId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )

        if(response.status===200){
          alert("Programming Question is deleted");
          onDelete(pq.programmingQuestionId)
        }
     }
     catch(error){
      console.error(
        "Error deleting:",
        error.response?.data || error.message
      );
      alert("Failed to delete question. Please try again.");
     }
  }
  return (
    <>
    <div className='flex'>
        <h1>{pq.programmingQuestionText}</h1>
        <div className="flex">
          {pq.testCases && pq.testCases.map((t)=>(
            <TestCase key={t.testcaseId} testcase={t} programmingQuestionId={pq.programmingQuestionId}/> 
           
          ))}
        </div>
        <ModeEditOutlineIcon/>
        <DeleteIcon onClick={handleDeleteProgrammingQuestion}/>
    </div>
    </>
  );
}

export default ProgrammingQuestion;
