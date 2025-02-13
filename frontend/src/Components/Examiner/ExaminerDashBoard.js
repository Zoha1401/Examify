import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";
import axiosInstance from "../../utils/axiosInstance";
import Exam from "./ExamDetail/Exam";
import Navigationbar from "./Navigationbar";

const ExaminerDashBoard = () => {
  const [exams, setExams] = useState([]);
  const [loading, setLoading] = useState(false);
  let navigate = useNavigate();

  useEffect(() => {
    const fetchAllExams = async () => {
      try {
        const token = localStorage.getItem("token");
        console.log("Token:", token);
        if (!token || !token.includes(".")) {
          alert("You are not authorized please login again");
          navigate("/examiner-login");
        }
        const response = await axiosInstance.get("/examiner/getAllExams", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          console.log("Response data:", response.data);
          const exams = response.data;
          setExams(exams);
        }
      } catch (error) {
        console.error(
          "Error fetching exams:",
          error.response?.data || error.message
        );
        alert("Failed to fetch exams. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchAllExams();
  }, []);

  const handleDeleteExam = (examId) => {
    setExams((prev) => prev.filter((exam) => exam.examId !== examId));
  };

  const handleUpdateExam = (updatedExam) => {
    setExams((prevExams) =>
      prevExams.map((exam) =>
        exam.examId === updatedExam.examId ? updatedExam : exam
      )
    );
  };

  return (
    <>
      <Navigationbar />
      <div className="flex flex-col bg-white justify-center items-center px-4 py-6">
        <h1 className="text-2xl font-bold text-center mb-6">
          Examiner Dashboard
        </h1>
        <div className="flex flex-row my-2 mb-4 px-2">
          <Link to="/manage-examinee">
            <Button variant="dark" className="px-3 my-3 mx-2">
              Manage Examinees
            </Button>
          </Link>
          {/* This will show a list of all examinees added by examiner, where he/she can add, update or delete examinee. Can also assign an exam from here to examinee, also bulk upload examinee*/}
          <Link to="/create-exam">
            <Button variant="dark" className="px-3 my-3 mx-2">
              Create Exam
            </Button>
          </Link>
        </div>
      </div>
      <div className="px-2 py-2 text-xl font-bold">Exams:</div>
      <div className="mx-2 font-semibold">
        <span className="p-2">Exam ID</span>
        <span className="p-8">Date</span>
        <span className="p-10">Start</span>
        <span className="p-6">End</span>
        <span className="p-6">Mcq Passing score</span>
        <span className="p-6">Duration</span>
      </div>

      <div className="">
        {loading ? (
          <p>Loading...</p>
        ) : exams.length === 0 ? (
          <p>You have no exams</p>
        ) : (
          exams.map((e) => (
            <Exam
              key={e.examId}
              temp_exam={e}
              onDelete={handleDeleteExam}
              onUpdate={handleUpdateExam}
            />
          ))
        )}
      </div>
    </>
  );
};

export default ExaminerDashBoard;
