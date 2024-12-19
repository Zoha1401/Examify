import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";
import axiosInstance from "../../utils/axiosInstance";
import Exam from "../ExamDetail/Exam";

const ExaminerDashBoard = () => {
  const [exams, setExams] = useState([]);
  const [loading, setLoading] = useState(false);
  let navigate=useNavigate();

  useEffect(() => {
    const fetchAllExams = async () => {
      try {
        const token = localStorage.getItem("token");
        console.log("Token:", token); 
        if (!token) {
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

  return (
    <div>
      <h1>This is Examiner Dashboard</h1>
      <div>
        {loading ? (
          <p>Loading...</p>
        ): exams.length===0 ? (
          <p>You have no exams</p>
        ): (
          exams.map((e)=>(
             <Exam key={e.examId} temp_exam={e}/>
          ))
        )}
      </div>
      <Button variant="info">
        <Link to="/manage-examinee">Manage examinees</Link>{" "}
      </Button>
      {/* This will show a list of all examinees added by examiner, where he/she can add, update or delete examinee. Can also assign an exam from here to examinee, also bulk upload examinee*/}
     
      <Button>
        <Link to="/create-exam">Create Exam</Link>
      </Button>
    </div>
  );
};

export default ExaminerDashBoard;

 {/* Dashboard will show a button to create exam and all the existing exams of examiner. Add a filter to show the most recent exam in first and finished exams at end
       Edit exam takes you to a new page where you can delete/Update/add questions  .... will see about question pool */}
      {/*have a dropdown that will show all examinees assigned to that exam and their respective result if exam done or nothing*/}
      {/*An exam on click will see all the questions in different sections and be able to edit */}




{/* A button create a exam, navigate where retrieve all the data then go to a page where add Mcq question or add programming question.
      Add technical question, add logical question and add programming question. save-> navigate to dashboard
      
      In add mcq question-> Pool of mcq questions for examiner to select based on difficulty and category and also add option
      Same for programming
      
      
      
      Try to complete All above things over in next week along with UI.
      Then focus on examinee stuff.
      
      By mid december complete project with only minor bugs and UI enhanced.
      Last week-> Deployment and extra features*/}