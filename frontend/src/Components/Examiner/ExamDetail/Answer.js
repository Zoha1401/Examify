import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";

const Answer = () => {
  const { examId, examineeId } = useParams();
  const [loading, setLoading] = useState(false);
  const [mcqAnswers, setMcqAnswers] = useState([]);
  const [programmingAnswers, setProgrammingAnswers] = useState([]);
  const [mcqScore, setMcqScore] = useState(0);

  const token = localStorage.getItem("token");
  let navigate = useNavigate();
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  useEffect(() => {
    const fetchAnswer = async () => {
      setLoading(true);
      try {
        const response = await axiosInstance(
          `/answer/getExamSpecificAnswer?examineeId=${examineeId}&examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        const { answerId } = response.data;
        const { mcqScore } = response.data;
        console.log(answerId);
        setMcqScore(mcqScore);

        const mcqAnswerResponse = await axiosInstance(
          `/answer/getMcqAnswerDetail?answerId=${answerId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setMcqAnswers(mcqAnswerResponse.data || []);

        const programmingAnswerResponse = await axiosInstance(
          `/answer/getProgrammingAnswerDetail?answerId=${answerId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(programmingAnswerResponse.data);
        setProgrammingAnswers(programmingAnswerResponse.data || []);
        setLoading(false);
      } catch (error) {
        console.error("Error fetching answer", error.message);
      }
    };
    fetchAnswer();
  }, [token, examId]);

  console.log(mcqAnswers);
  console.log(programmingAnswers);
  const count = 1;

  return (
    <div>
      {mcqScore}
      {loading ? (
        <p>Loading...</p>
      ) : mcqAnswers.length === 0 ? (
        <p>No MCQ answers found</p>
      ) : (
        mcqAnswers.map((m) => (
          <div>
            {m.questionText}
            {m.options.map((o) => (
              //Have two divisions here when mcq is correct and when wrong.
              <div>
                <input
                  type="radio"
                  checked={o.optionId === m.selectedOptionId}
                  className="mb-2"
                />
                {o.optionText}
                {o.isCorrect ? (
                  <strong classname="font-light bg-green-200">(Correct)</strong>
                ):(<strong classname="font-light bg-red-200">(Incorrect)</strong>)}
              </div>
            ))}
          </div>
        ))
      )}
      <div>
        {loading ? (
          <p>Loading...</p>
        ) : programmingAnswers && programmingAnswers.length > 0 ? (
          programmingAnswers.map((answer, index) => (
            <div key={index}>
              <h3>Question: {answer.programmingQuestionText}</h3>
              <p>Code Submission: {answer.codeSubmission}</p>
              <div>
                <h4>Test Cases:</h4>
                {answer.testCases?.length > 0 ? (
                  answer.testCases.map((testCase, idx) => (
                    <div key={idx}>
                      <p>Input: {testCase.input}</p>
                      <p>Expected Output: {testCase.expectedOutput}</p>
                    </div>
                  ))
                ) : (
                  <p>No test cases available.</p>
                )}
              </div>
            </div>
          ))
        ) : (
          <p>No Programming answers found</p>
        )}
      </div>
    </div>
  );
};

export default Answer;
