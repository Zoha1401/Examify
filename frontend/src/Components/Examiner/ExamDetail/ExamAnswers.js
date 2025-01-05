import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";

const ExamAnswers = () => {
  const [examinees, setExaminees] = useState([]);
  const [scores, setScores] = useState({}); // Store scores for each examinee
  const [loading, setLoading] = useState(false);
  const [passed, setPassed] = useState({});
  const { examId } = useParams();
  const token = localStorage.getItem("token");
  let navigate = useNavigate();

  if (!token) {
    alert("You are not authorized, please login again");
    navigate("/examiner-login");
  }

  const sortedExaminees = [...examinees].sort((a, b) => {
    const aPassed = passed[a.examineeId] ? 1 : 0;
    const bPassed = passed[b.examineeId] ? 1 : 0;
    return bPassed - aPassed; // Passed examinees come first
  });

  //Fetch examinees of an exam
  useEffect(() => {
    const fetchExaminees = async () => {
      setLoading(true);
      try {
        // Fetch examinees
        const response = await axiosInstance(
          `/exam/getExaminees?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setExaminees(response.data);

        const scoresMap = {};
        const passMap = {};
        //Fetch those examinee's respective answers based on examId
        await Promise.all(
          response.data.map(async (examinee) => {
            try {
              const answerResponse = await axiosInstance(
                `/answer/getExamSpecificAnswer?examineeId=${examinee.examineeId}&examId=${examId}`,
                {
                  headers: {
                    Authorization: `Bearer ${token}`,
                  },
                }
              );

              scoresMap[examinee.examineeId] = answerResponse.data.mcqScore;
              passMap[examinee.examineeId] = answerResponse.data.passed;
            } catch (error) {
              console.error(
                `Error fetching answer for examinee ${examinee.examineeId}:`,
                error
              );
              scoresMap[examinee.examineeId] = "Exam not shown";
            }
          })
        );

        setScores(scoresMap);
        setPassed(passMap);
      } catch (error) {
        console.error("Error fetching examinees:", error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchExaminees();
  }, [token, examId]);

  const showAnswer = (e) => {
    console.log(e.examineeId);
    navigate(`/detail-answer/${e.examineeId}/${examId}`);
  };

  console.log(examinees);

  return (
    <div className="">
      <Navigationbar />
      <div className="flex-col flex mx-2 py-4 p-4">
        <b className="mx-2 text-xl font-semibold mb-4">Exam Answers:</b>
        {loading ? (
          <p>Loading...</p>
        ) : examinees.length === 0 ? (
          <p>You have no exams</p>
        ) : (
          sortedExaminees.map((e) => (
            <div
              key={e.examineeId}
              onClick={() => showAnswer(e)}
              className={`hover:cursor-pointer hover:shadow-lg transition-transform transform hover:scale-95 flex border-1 rounded-lg p-3 my-1 shadow-sm ${
                passed[e.examineeId] ? "bg-green-100" : "bg-red-100"
              }`}
            >
              <p className="mx-2 font-bold">Email:</p> <span> {e.email}</span>
              <p className="mx-2 font-bold">Degree:</p>
              <span>{e.degree}</span>
              <p className="mx-2 font-bold">College:</p>
              <span> {e.college}</span>
              <p className="mx-2 font-bold">Year:</p>
              <span> {e.year}</span>
              <p className="mx-2 font-bold">
                MCQ Score: {scores[e.examineeId] ?? "Loading..."}
              </p>
              <div>
                Status : {passed[e.examineeId] ? "Passed" : "Not Passed"}
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ExamAnswers;
