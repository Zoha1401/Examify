import React, {useState, useEffect} from 'react';
import { useParams } from 'react-router-dom';

const ExamQuestions = () => {
  const json=useParams();
  console.log(json.examId);
  
  const [technicalMcqs, setTechnicalMcqs]=useState([]);
  const [aptitudeMcqs, setAptitudeMcqs]=useState([]);

  const [programmingQuestion, setProgrammingQuestions]=useState([]);

  useEffect(() => {
    const fetchTechnicalMcqs=async()=>{

    }

    const fetchAptitudeMcqs= async ()=>{

    }

    const fetchProgrammingQuestions= async ()=>{

    }
  }, []);
  return (
    <div>
      {/*This will contain exam details, all mcqs and programming, they can update mcq and programming question optiion will be provided in a single page only */}
      {/*They will also be able to add mcq and programming */}
    </div>
  );
}

export default ExamQuestions;
