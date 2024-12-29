import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";

const ExamAnswers = () => {
  const [examinees, setExaminees] = useState([]);
  const [scores, setScores] = useState({}); // Store scores for each examinee
  const [loading, setLoading] = useState(false);
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
            } catch (error) {
              console.error(`Error fetching answer for examinee ${examinee.examineeId}:`, error);
              scoresMap[examinee.examineeId] = "Error"; 
            }
          })
        );

        setScores(scoresMap);
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
      {loading ? (
        <p>Loading...</p>
      ) : examinees.length === 0 ? (
        <p>You have no exams</p>
      ) : (
        examinees.map((e) => (
          <div key={e.examineeId} onClick={() => showAnswer(e)}>
            <p>Email: {e.email}</p>
            <p>Degree: {e.degree}</p>
            <p>College: {e.college}</p>
            <p>Year: {e.year}</p>
            <p>MCQ Score: {scores[e.examineeId] ?? "Loading..."}</p>
          </div>
        ))
      )}
    </div>
  );
};

export default ExamAnswers;
