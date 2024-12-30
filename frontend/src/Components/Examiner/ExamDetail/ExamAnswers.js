import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";

const ExamAnswers = () => {
  const [examinees, setExaminees] = useState([]);
  const [scores, setScores] = useState({}); // Store scores for each examinee
  const [loading, setLoading] = useState(false);
  const [passed, setPassed]=useState({})
  const { examId } = useParams();
  const token = localStorage.getItem("token");
  let navigate = useNavigate();

  if (!token) {
    alert("You are not authorized, please login again");
    navigate("/examiner-login");
  }

  useEffect(() => {
    const fetchExaminees = async () => {
      setLoading(true);
      try {
        // Fetch examinees
        const response = await axiosInstance(`/exam/getExaminees?examId=${examId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setExaminees(response.data);
        
        const scoresMap = {};
        const passMap={}
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
              passMap[examinee.examineeId]= answerResponse.data.passed
            } catch (error) {
              console.error(`Error fetching answer for examinee ${examinee.examineeId}:`, error);
              scoresMap[examinee.examineeId] = "Exam not shown"; 
            }
          })
        );

        setScores(scoresMap);
        setPassed(passMap)
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
    <div>
      <Navigationbar/>
      <div className="flex-col flex mx-2 py-4">
        <b className="mx-2">Answers:</b>
      {loading ? (
        <p>Loading...</p>
      ) : examinees.length === 0 ? (
        <p>You have no exams</p>
      ) : (
        examinees.map((e) => (
          <div key={e.examineeId} onClick={() => showAnswer(e)} className="flex border-1 rounded-lg py-2 px-2 my-1 bg-gray-50 shadow-sm">
            <p className="mx-2">Email: {e.email}</p>
            <p className="mx-2">Degree: {e.degree}</p>
            <p className="mx-2">College: {e.college}</p>
            <p className="mx-2">Year: {e.year}</p>
            <p className="mx-2">MCQ Score: {scores[e.examineeId] ?? "Loading..."}</p>
            <div>Status : {passed[e.examineeId]? "Passed": "Not Passed"}</div>
          </div>
        ))
      )}
      </div>
    </div>
  );
};

export default ExamAnswers;
