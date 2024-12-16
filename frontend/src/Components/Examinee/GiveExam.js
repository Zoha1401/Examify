import React, { useEffect, useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";

const GiveExam = () => {
  const { examId } = useParams();

  const [mcqQuestions, setMcqQuestions] = useState([]);
  const [programmingQuestions, setProgrammingQuestions] = useState([]);
  const [answers, setAnswers] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examinee-login");
  }

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const mcqResponse = await axiosInstance.get(
          `/examinee/getAllMcqQuestions?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const programmingResponse = await axiosInstance.get(
          `/examinee/getAllProgrammingQuestions?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(mcqResponse.data);
        setMcqQuestions(mcqResponse.data);
        setProgrammingQuestions(programmingResponse.data);
      } catch (error) {
        console.error("Error fetching MCQ:", error, error.message);
        alert("Failed to fetch MCQ. Please try again.");
      }
    };

    if (token && examId) {
      fetchQuestions();
    }
  }, [token, examId]);

  const handleOptionChange = (mcqId, optionId) => {
    setAnswers((prevAnswers) => {
      const updatedAnswers = prevAnswers.filter((a) => a.mcqId !== mcqId);
      return [...updatedAnswers, { mcqId, selectedOptionId: optionId }];
    });
  };

  const renderMcq = (mcq) => {
    return (
      <div>
        <h2>Question {currentQuestionIndex + 1}</h2>
        <p>{mcq.mcqQuestionText}</p>
        <div className="options-container">
          {mcq.options.map((option) => (
            <div key={option.optionId}>
              <input
                type="radio"
                name={`mcq-${mcq.mcqId}`}
                value={option.optionId}
                checked={
                  answers.find((a) => a.mcqId === mcq.mcqId)
                    ?.selectedOptionId === option.optionId
                }
                onChange={() => handleOptionChange(mcq.mcqId, option.optionId)}
              />
              {option.optionText}
            </div>
          ))}
        </div>
      </div>
    );
  };

  const renderProgrammingQuestion = (pq) => {
    return (
        <div>
        <h2>Question {currentQuestionIndex + 1}</h2>
        <p>{pq.programmingQuestionText}</p>
        <div className="testcase-container">
          {pq.testCases.map((t) => (
            <div key={t.testcaseId}>
              <p>{t.input}</p>
              <p>{t.expectedOutput}</p>
            </div>
          ))}
        </div>
      </div>
    )
  };
  const handleSubmit = async () => {};
  const currentMcq = mcqQuestions[currentQuestionIndex];
  const currentProgrammingQuestion=programmingQuestions[currentQuestionIndex-mcqQuestions.length]

  return (
    <>
      <div>
      {currentQuestionIndex < mcqQuestions.length ? (
        renderMcq(currentMcq)
      ) : currentProgrammingQuestion ? (
        renderProgrammingQuestion(currentProgrammingQuestion)
      ) : (
        <div>No questions found.</div>
      )}

        <div className="navigation-container">
          <button
            disabled={currentQuestionIndex === 0}
            onClick={() => setCurrentQuestionIndex((prev) => prev - 1)}
          >
            Previous
          </button>

          <button
            disabled={currentQuestionIndex === mcqQuestions.length + programmingQuestions.length - 1}
            onClick={() => setCurrentQuestionIndex((prev) => prev + 1)}
          >
            Next
          </button>
        </div>
        <button onClick={handleSubmit}>Submit</button>
      </div>
    </>
  );
};

export default GiveExam;
