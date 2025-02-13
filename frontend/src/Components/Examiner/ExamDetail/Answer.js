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
  const [passed, setPassed] = useState(false);

  //Get token else navigate to login
  const token = localStorage.getItem("token");
  let navigate = useNavigate();
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  //Fetch answer
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
        const { passed } = response.data;
        console.log(answerId);
        setMcqScore(mcqScore);
        setPassed(passed);

        //Fetch detail answerss
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
      <Navigationbar />
      <div className="bg-indigo-200 text-center font-bold text-lg rounded-md shadow-md p-4">
        Examination Results
      </div>
      <div className="p-4 flex-col flex">
        <p className="font-semibold text-xl">Mcq Answers</p>
        <span className="text-gray-700">Mcq Score: {mcqScore}</span>
        {passed && (
          <div className="bg-green-200 w-full rounded-md shadow-sm p-2 mb-2 mt-2">
            The examinee has passed
          </div>
        )}
        {loading ? (
          <p>Loading...</p>
        ) : mcqAnswers.length === 0 ? (
          <p>No MCQ answers found</p>
        ) : (
          mcqAnswers.map((m) => (
            <div className="flex-col bg-gray-100 rounded-lg my-2">
              <div className="mx-2 my-2 px-2 font-semibold">
                {m.questionText}
              </div>
              <div className="flex mt-3 mx-2 my-2">
                {m.options.map((o) => (
                  //Have two divisions here when mcq is correct and when wrong.
                  <div className="mx-2">
                    <input
                      type="radio"
                      checked={o.optionId === m.selectedOptionId}
                      className="mb-2"
                    />
                    {o.optionText}
                  </div>
                ))}
                <div className="flex justify-end mx-">
                  {m.correct ? (
                    <div className="bg-green-100 justify-content-end">
                      correct
                    </div>
                  ) : (
                    <div className="bg-red-100">Incorrect</div>
                  )}
                </div>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Display answers, and if examinee has passed or not */}

      <div className="flex flex-col p-4">
        <div className="text-xl font-semibold">Programming Answers:</div>
        {loading ? (
          <p>Loading...</p>
        ) : programmingAnswers && programmingAnswers.length > 0 ? (
          programmingAnswers.map((answer, index) => (
            <div key={index}>
              <p className="mt-3">
                <span className="font-bold text-lg mt-3">Question:</span>{" "}
                {answer.programmingQuestionText}
              </p>
              <p className="font-semibold">Code Submission:</p>
              <pre className="mt-2 p-3 rounded-md bg-gray-200 overflow-x-auto">
                {answer.codeSubmission}
              </pre>
              <div>
                <h4>Test Cases:</h4>
                {answer.testCases?.length > 0 ? (
                  answer.testCases.map((testCase, idx) => (
                    <div key={idx} className="flex flex-row p-2">
                      <p className="text-gray-700">
                        <strong>Input:</strong> {testCase.input}
                      </p>
                      <p className="text-gray-700">
                        <strong className="p-1">Expected Output:</strong>{" "}
                        {testCase.expectedOutput}
                      </p>
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
