import React, { useState, useEffect } from "react";
import { useParams , Link} from "react-router-dom";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import ProgrammingQuestion from "./ProgrammingQuestion";
import Mcq from "./Mcq";
import { Button } from "react-bootstrap";

const ExamQuestions = () => {
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

  const [technicalMcqs, setTechnicalMcqs] = useState([]);
  const [aptitudeMcqs, setAptitudeMcqs] = useState([]);
  const [loading, setLoading] = useState(false);

  const [programmingQuestions, setProgrammingQuestions] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
  
        const [technicalRes, aptitudeRes, programmingRes] = await Promise.all([
          axiosInstance.get(`/exam/getMcqTechnical?examId=${examId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axiosInstance.get(`/exam/getMcqAptitude?examId=${examId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axiosInstance.get(`/exam/getAllProgrammingQuestions?examId=${examId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);
        console.log(technicalRes.data)
        console.log(aptitudeRes.data)
        console.log(programmingRes.data)
        if (technicalRes.status === 200) setTechnicalMcqs(technicalRes.data);
        if (aptitudeRes.status === 200) setAptitudeMcqs(aptitudeRes.data);
        if (programmingRes.status === 200) setProgrammingQuestions(programmingRes.data);
      } catch (error) {
        console.error(
          "Error fetching data:",
          error.response?.data || error.message
        );
        alert("Failed to fetch data. Please try again.");
      } finally {
        setLoading(false);
      }
    };
  
    fetchData();
  }, [examId, token]);

  const handleDeleteTechnicalMcq=(mcqId)=>{
    setTechnicalMcqs((prev)=>prev.filter((mcq)=>mcq.mcqId!==mcqId));
  };

  const handleDeleteAptitudeMcq=(mcqId)=>{
    setAptitudeMcqs((prev)=>prev.filter((mcq)=> mcq.mcqId!==mcqId));
  };

  const handleDeleteProgQuestion=(programmingQuestionId)=>{
    setProgrammingQuestions((prev)=>prev.filter((pq)=>pq.programmingQuestionId!==programmingQuestionId))
  }
  
    
  return (
    <div>
      {/*This will contain exam details, all mcqs and programming, they can update mcq and programming question optiion will be provided in a single page only */}
      {/*They will also be able to add mcq and programming */}
      <div className="flex">
      <Link to={`/addMcqQuestion/${examId}`}><Button>Add new MCQ Question</Button></Link>
      <Link to={`/addProgQuestion/${examId}`}><Button>Add new Programming Question</Button></Link>
      </div>
      
      <div>Technical Questions</div>
      <div>
        {loading ? (
          <p>Loading...</p>
        ): technicalMcqs.length===0 ? (
          <p>You have no technical mcqs</p>
        ): (
          technicalMcqs.map((m)=>(
             <Mcq key={m.mcqId} mcq={m} onDelete={handleDeleteTechnicalMcq}/>
          ))
        )}
      </div>
      <div>Aptitude Questions</div>
      <div>
      {loading ? (
          <p>Loading...</p>
        ): aptitudeMcqs.length===0 ? (
          <p>You have no aptitude mcqs</p>
        ): (
          aptitudeMcqs.map((m)=>(
             <Mcq key={m.mcqId} mcq={m} onDelete={handleDeleteAptitudeMcq}/>
          ))
        )}
      </div>
      <div>
        Programming Questions
      </div>
      <div>
      {loading ? (
          <p>Loading...</p>
        ): programmingQuestions.length===0 ? (
          <p>You have no programming questions</p>
        ): (
          programmingQuestions.map((q)=>(
             <ProgrammingQuestion key={q.programmingQuestionId} pq={q} onDelete={handleDeleteProgQuestion}/>
          ))
        )}
      </div>
      <Link to={`/mcqQuestionPool/${examId}`}><Button>Add MCQ Question from pool</Button></Link>
      <Link to={`/programmingQuestionPool/${examId}`}><Button>Add Programming Question from pool</Button></Link>
    </div>

  
  );
};

export default ExamQuestions;

//Give option to delete all.