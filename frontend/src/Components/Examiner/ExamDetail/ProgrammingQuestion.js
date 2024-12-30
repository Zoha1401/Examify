import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../../../utils/axiosInstance';
import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditOutlineIcon from '@mui/icons-material/ModeEditOutline';

const ProgrammingQuestion = ({pq, onDelete, onUpdate}) => {
  
  const [editableProQ, setEditableProQ]=useState(null);
  const [testCases, setTestCases]=useState([]) 

  const {examId}=useParams();

  let navigate=useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  const handleUpdateProQ=async()=>{
    try{
        const payload={
          ...editableProQ,
          testCases,
        };

        const response=await axiosInstance.post(`/programmingQuestion/updateProgrammingQuestion?examId=${examId}&proId=${editableProQ.programmingQuestionId}`,
          {
            ...payload,
          },
          {
            headers:{
                Authorization:`Bearer ${token}`
            }
          }

        )
        if(response.status===200){
        alert("Programming Question Updated")
        onUpdate(response.data)
        setEditableProQ(null)
        }
    }
    catch(error){
      console.error("Error updating pro q:", error.response?.data || error.message);
      alert("Error updating pro q. Please try again.");
    }
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

  const onChange=(e)=>{
    setEditableProQ({...editableProQ, [e.target.name]:e.target.value})
  }

  const handleEdit=()=>{
    setEditableProQ(pq);
    setTestCases(pq.testCases || [])
  }

  const handleTestCaseChange = (index, field, value) => {
    const updatedTestCases = testCases.map((testCase, i) =>
      i === index ? { ...testCase, [field]: value } : testCase
    );
    setTestCases(updatedTestCases);
  }
  return (
    <>
    <div className='flex-col'>
        <div className='px-2'>{pq.programmingQuestionText}</div>
        <div className="flex-col">
          {pq.testCases && pq.testCases.map((t)=>(
            <div className='flex px-2'>
              Input : {t.input}{" "}
              Output: {t.expectedOutput}
            </div>
           
          ))}
        </div>
        <ModeEditOutlineIcon onClick={handleEdit}/>
        {editableProQ && (
          <form onSubmit={handleUpdateProQ}>
              <input
                type="text"
                name="programmingQuestionText"
                value={editableProQ.programmingQuestionText || ""}
                onChange={onChange}
              ></input>
              <div className="flex">
                {testCases.map((t, index) => (
                  <div key={t.programmingQuestionId || index}>
                    {t.input}{" "}
                    {t.expectedOutput}{" "}
                    <input
                      type="text"
                      name="input"
                      value={t.input || ""}
                      onChange={(e) =>
                        handleTestCaseChange(index, "input", e.target.value)
                      }
                    ></input>
                    <input
                      type="text"
                      name="expectedOutput"
                      value={t.expectedOutput || ""}
                      onChange={(e) =>
                        handleTestCaseChange(index, "expectedOutput", e.target.value)
                      }
                    ></input>
                   
                  </div>
                ))}
              </div>
              <button type="submit">Update Programming Question</button>
          </form>
        )}
         
        
        <DeleteIcon onClick={handleDeleteProgrammingQuestion}/>
    </div>
    </>
  );
}

export default ProgrammingQuestion;


//summarise results
//passed examinees
//assign exam functionality debugging and proper frontend (Assigned exmainee dekhava joiye ane assign kari sake)


//Results are getting fetched now have to enhance UI and show

//Enhance UI


//Sequence of Importance (To finish it beautifully) 



////Google oauth
//Enhance UI
//Try to load questions from CSV.
//Try to load examinees from CSV.


//Done
//State management
//Have examinee enter phone number as password.
//Organize backendd code based on return type
//Once exam deleted delete all asociated answers****
//assign to all examinee frontend***

