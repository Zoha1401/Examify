import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";

const Answer = () => {
  const { examId, examineeId } = useParams();
  const [loading, setLoading] = useState(false);
  const [mcqAnswers, setMcqAnswers] = useState([]);
  const [programmingAnswers, setProgrammingAnswers] = useState([]);
  const [mcqScore, setMcqScore] = useState(0);
  const [passed, setPassed]=useState(false)

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
        const {passed} =response.data
        console.log(answerId);
        setMcqScore(mcqScore);
        setPassed(passed)

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
      <Navigationbar/>

      <div className="flex-col font-bold"><b>Mcq Answers</b></div>
      Mcq Score: {mcqScore}
      {passed && <div>The examinee has passed</div>}
      {loading ? (
        <p>Loading...</p>
      ) : mcqAnswers.length === 0 ? (
        <p>No MCQ answers found</p>
      ) : (
        mcqAnswers.map((m) => (
          <div className="flex-col bg-gray-100 rounded-lg px-2 my-2 mx-2 py-2">
            <div className="mx-2 px-2">{m.questionText}</div>
            <div className="flex mt-3 mx-2">{m.options.map((o) => (
              //Have two divisions here when mcq is correct and when wrong.
              <div>
                <input
                  type="radio"
                  checked={o.optionId === m.selectedOptionId}
                  className="mb-2"
                />
                {o.optionText}
              </div>
              
            ))}
            <div className="flex justify-end mx-">
             {m.correct ?( <div className="bg-green-100 justify-content-end">correct</div>):(
              <div className="bg-red-100">Incorrect</div>
             )}
             </div>
            </div>
           
          </div>
        ))
      )}
      <div>
        <b>Programming Answers:</b>
        {loading ? (
          <p>Loading...</p>
        ) : programmingAnswers && programmingAnswers.length > 0 ? (
          programmingAnswers.map((answer, index) => (
            <div key={index}>
              <p>Question: {answer.programmingQuestionText}</p>
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
