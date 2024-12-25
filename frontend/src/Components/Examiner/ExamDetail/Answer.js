import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axiosInstance from '../../../utils/axiosInstance';

const Answer = ({examinee}) => {
    const {examId, examineeId}=useParams();
    const [loading, setLoading]=useState(false)
    const [mcqAnswers, setMcqAnswers]=useState([]);
    const token = localStorage.getItem("token");
    let navigate=useNavigate();
    console.log(token); 
    if (!token) {
      alert("You are not authorized please login again");
      navigate("/examiner-login");
    }

    useEffect(() => {
     const fetchAnswer=async()=>{
        setLoading(true)
        try{
          const response=await axiosInstance(`/answer/getExamSpecificAnswer?examineeId=${examineeId}&examId=${examId}`,
            {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
            }
          )
          const { answerId } = response.data;
        

        const mcqAnswerResponse=await axiosInstance(`/answer/getMcqAnswerDetail?answerId=${answerId}`,
            {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
            }
        )
        setMcqAnswers(mcqAnswerResponse.data)
        setLoading(false)

        }
        catch(error){
            console.error("Error fetching answer", error.message);
        }

     }
     fetchAnswer();

    }, [token, examId]);
    
console.log(mcqAnswers)
  return (
    <div>
      
    </div>
  );
}

export default Answer;
