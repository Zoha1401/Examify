import React, { useEffect, useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";

const GiveExam = () => {
  const { examId } = useParams();

  const [mcqQuestions, setMcqQuestions] = useState([]);
  const [programmingQuestions, setProgrammingQuestions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  let navigate=useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examinee-login");
  }

  useEffect(() => {
    const fetchAllMcqs = async () => {
      try {
        const response = await axiosInstance.get(
          `/examinee/getAllMcqQuestions?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response.data)
        setMcqQuestions(response.data);
      } catch (error) {
        console.error("Error fetching MCQ:", error, error.message);
        alert("Failed to fetch MCQ. Please try again.");
      }
    };

    if (token && examId) {
        fetchAllMcqs();
      }
  }, [token, examId]);

  const handleOptionChange = (mcqId, optionId) => {
    setAnswers((prevAnswers) => {
      const updatedAnswers = prevAnswers.filter((a) => a.mcqId !== mcqId);
      return [...updatedAnswers, { mcqId, selectedOptionId: optionId }];
    });
  };

  const handleSubmit=async()=>{

  }
  const currentMcq = mcqQuestions[currentQuestionIndex];

  return (
    <>
      <div>
        {mcqQuestions.length===0 ? (
            <div>No MCQ Questions found</div>
        ):(
            <div>
        <h2>Question {currentQuestionIndex + 1}</h2>
        <p>{currentMcq.mcqQuestionText}</p>
        <div className="options-container">
          {currentMcq.options.map((option) => (
            <div key={option.optionId}>
              <input
                type="radio"
                name={`mcq-${currentMcq.mcqId}`}
                value={option.optionId}
                checked={
                  answers.find((a) => a.mcqId === currentMcq.mcqId)
                    ?.selectedOptionId === option.optionId
                }
                onChange={() =>
                  handleOptionChange(currentMcq.mcqId, option.optionId)
                }
              />
              {option.optionText}
            </div>
          ))}
        </div>
        <div className="navigation-container">
          <button
            disabled={currentQuestionIndex === 0}
            onClick={() => setCurrentQuestionIndex((prev) => prev - 1)}
          >
            Previous
          </button>

          <button
            disabled={currentQuestionIndex === mcqQuestions.length-1}
            onClick={() => setCurrentQuestionIndex((prev) => prev + 1)}
          >
            Next
          </button>
        </div>
        <button onClick={handleSubmit}>Submit</button>
        </div>
        )}
      </div>
    </>
  );
};

export default GiveExam;
