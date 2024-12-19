import React, { useEffect, useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { Button } from "react-bootstrap";
import { Link, useNavigate, useParams } from "react-router-dom";

const ExamineeDashBoard = () => {
  const [exams, setExams] = useState([]);
  let navigate = useNavigate();
  const parameter = useParams();
  const examineeEmail = parameter.email;

  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examinee-login");
  }

  useEffect(() => {
    const fetchExams = async () => {
      try {
        const responseId = await axiosInstance.get(
          `/examinee/getExamineeIdFromEmail?examineeEmail=${examineeEmail}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        console.log(responseId.data);

        const examineeId = responseId.data;

        const response = await axiosInstance.get(
          `/examinee/getAllExams?examineeId=${examineeId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        console.log(response.data);
        if (response.status === 200) {
          setExams(response.data);
        }
      } catch (error) {
        console.error("Error:", error.response?.data || error.message);
        alert("Failed. Please try again.");
      }
    };
    fetchExams();
  }, [token, examineeEmail]);
  return (
    <>
      <div>Exams:</div>
      <div className="flex">
        {exams.length > 0 ? (
          exams.map((exam, index) => (
            <div key={exam.examId || index} className="mx-2">
              {exam.startTime}
              {exam.duration} min
              <Link to={`/startExam/${exam.examId}`}>
                <Button>Start Exam</Button>
              </Link>
            </div>
          ))
        ) : (
          <p>No exams available</p>
        )}
      </div>
    </>
  );
};

export default ExamineeDashBoard;

//Tomorrow
//Working on Update functionality of MCQ.
//Examinee can see the exams assign to them-> onClick start, ek page navbar timer, get mcq and pro, page wise mcq will display.
//Submit answer, automatic marking of answers in backend. Display marked results in frontend.
//OTP vala if possible

//Next week.
//Another endpoint where examiner will be able to assign exam to the examinee after examinee creation.
//Also an (endpoint) option to assign a particular exam to all examinees, after exam creation
