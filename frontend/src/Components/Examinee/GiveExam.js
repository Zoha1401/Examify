import React, { useEffect, useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import { Dropdown } from "react-bootstrap";

const GiveExam = () => {
  const { examId } = useParams();

  const [mcqQuestions, setMcqQuestions] = useState([]);
  const [programmingQuestions, setProgrammingQuestions] = useState([]);
  const [codeAnswers, setCodeAnswers] = useState([]);
  const [mcqAnswers, setMcqAnswers] = useState([]);
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

  const handleOptionChange = (mcqQuestionId, optionId) => {
    setMcqAnswers((prevAnswers) => {
      const updatedAnswers = prevAnswers.filter((a) => a.mcqQuestionId !== mcqQuestionId);
      return [...updatedAnswers, { mcqQuestionId, selectedOptionId: optionId }];
    });
    console.log(mcqAnswers);
  };

  const onChangeCodeAnswer = (pqId, language, code) => {
    setCodeAnswers((prev) => {
      const updatedAnswers = prev.filter((a) => a.pqId !== pqId);
      return [...updatedAnswers, { pqId, language, codeSubmission: code }];
    });
  };

  const handleLanguageChange = (pqId, newLanguage) => {
    setCodeAnswers((prev) => {
      const existingAnswer = prev.find((a) => a.pqId === pqId);
      if (existingAnswer) {
        return prev.map((a) =>
          a.pqId === pqId ? { ...a, language: newLanguage } : a
        );
      } else {
        return [...prev, { pqId, language: newLanguage, codeSubmission: "" }];
      }
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
                  mcqAnswers.find((a) => a.mcqQuestionId === mcq.mcqId)
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
    const currentAnswer =
      codeAnswers.find((a) => a.pqId === pq.programmingQuestionId) || {};
    const { language = "C++", codeSubmission = "" } = currentAnswer;
    console.log(codeAnswers);
    return (
      <div>
        <Dropdown>
          <Dropdown.Toggle variant="secondary">{language}</Dropdown.Toggle>
          <Dropdown.Menu>
            <Dropdown.Item
              onClick={() =>
                handleLanguageChange(pq.programmingQuestionId, "C++")
              }
            >
              C++
            </Dropdown.Item>
            <Dropdown.Item
              onClick={() =>
                handleLanguageChange(pq.programmingQuestionId, "Java")
              }
            >
              Java
            </Dropdown.Item>
            <Dropdown.Item
              onClick={() =>
                handleLanguageChange(pq.programmingQuestionId, "Python")
              }
            >
              Python
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>

        <div className="programming-question">
          <h2>Question {currentQuestionIndex + 1}</h2>
          <p>{pq.programmingQuestionText}</p>
          <div className="testcase-container">
            {pq.testCases.map((t) => (
              <div key={t.testcaseId} className="testcase">
                <p>
                  <strong>Input:</strong> {t.input}
                </p>
                <p>
                  <strong>Expected Output:</strong> {t.expectedOutput}
                </p>
              </div>
            ))}
          </div>

          <div>
            <label htmlFor={`codeSubmission-${pq.programmingQuestionId}`}>
              Your Code:
            </label>
            <textarea
              id={`codeSubmission-${pq.programmingQuestionId}`}
              rows="10"
              cols="50"
              value={codeSubmission}
              onChange={(e) =>
                onChangeCodeAnswer(
                  pq.programmingQuestionId,
                  language,
                  e.target.value
                )
              }
            ></textarea>
          </div>
        </div>
      </div>
    );
  };

  const handleSubmit = async () => {
    try {
      const payload = {
        mcqAnswers,
        programmingQuestionAnswers: codeAnswers,
      };
  
      console.log(payload)
      const response = await axiosInstance.post(
        `/examinee/submitExam?examId=${examId}`,
       payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response);
      alert("Exam submitted");
      //Submit screen
      navigate("/");
    } catch (error) {
      console.error("Error submiting exam:", error, error.message);
      alert("Failed to submit exam. Please try again.");
    }
  };
  const currentMcq = mcqQuestions[currentQuestionIndex];
  const currentProgrammingQuestion =
    programmingQuestions[currentQuestionIndex - mcqQuestions.length];

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
            disabled={
              currentQuestionIndex ===
              mcqQuestions.length + programmingQuestions.length - 1
            }
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

//For this week

//Integrate Frontend with submit exam
//Integrate assign to all examinees
