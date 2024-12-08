import React from "react";
import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../utils/axiosInstance";

const Mcq = ({ mcq, onDelete }) => {
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

  
  const handleDeleteMcq=async()=>{
     try{
        const response= await axiosInstance.delete(`/mcqQuestion/deleteMcqQuestion?mcqId=${mcq.mcqId}&examId=${examId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        )

        if(response.status===200){
          alert("Mcq is deleted");
          onDelete(mcq.mcqId)
        }
     }
     catch(error){
      console.error(
        "Error deleting:",
        error.response?.data || error.message
      );
      alert("Failed to delete mcq. Please try again.");
     }
  }
  return (
    <>
      <div className="flex">
        <div>{mcq.mcqQuestionText}</div>
        <div>Correct Answer: {mcq.correctAnswer}</div>
        <div className="flex">
          {mcq.options.map((option, index) => (
            <div key={option.optionId || index}>
              {option.optionText}{" "}
              {option.isCorrect && <strong classname="font-light bg-green-200">(Correct)</strong>}
            </div>
          ))}
           <ModeEditOutlineIcon/>
           <DeleteIcon onClick={handleDeleteMcq}/>
        </div>
      </div>
    </>
      
  );
};

export default Mcq;
