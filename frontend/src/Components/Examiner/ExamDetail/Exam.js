import React, {useState, useEffect} from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import {useNavigate} from 'react-router-dom'
import { Button } from 'react-bootstrap';

const Exam = ({ temp_exam }) => {
  const [editableExam, setEditableExam] = useState(null);
  const [exam, setExam] = useState({});
  let navigate = useNavigate();
  console.log(temp_exam);
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }
  useEffect(() => {
    const fetchExam = async () => {
      try {
        const response = await axiosInstance.get(
          `/exam/getExamById?examId=${temp_exam.examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response.data);
        setExam(response.data);
        // setExam(examData);
      } catch (error) {
        console.error("Error fetching exam", error.message);
      }
    };
    fetchExam();
  }, [exam.examId]);

  const handleDelete = async () => {
    try {
      const response = await axiosInstance.delete(
        `/exam/deleteExam?examId=${exam.examId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        alert("Exam deleted");
        window.location.reload();
      }
    } catch (error) {
      console.error(
        "Error deleting exam:",
        error.response?.data || error.message
      );
      alert("Error deleting exam. Please try again.");
    }
  };

  const handleEdit = () => {
    setEditableExam(exam);
  };

  const handleUpdate = async () => {
    try {
      const response = await axiosInstance.post(
        `/exam/updateExam?examId=${editableExam.examId}`,
        editableExam,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        alert("Exam updated");
        window.location.reload();
      }
    } catch (error) {
      console.error(
        "Error updating exam:",
        error.response?.data || error.message
      );
      alert("Error updating exam. Please try again.");
    }
  };

  const onChange = (e) => {
    setEditableExam({
      ...editableExam,
      [e.target.name]: e.target.value,
    });
  };

  const viewQuestions=()=>{
    navigate(`/exam-detail/${exam.examId}`);
  }
  return (
    <div className="flex">
      <h1>{temp_exam.examId}</h1>
      <h1>{exam.startTime}</h1>
      <h1>{exam.endTime}</h1>
      <h1>{exam.mcqPassingScore}</h1>
      <h1>{exam.duration}</h1> min
      <Button onClick={handleDelete}>Delete</Button>
      <Button onClick={handleEdit}>Update</Button>
      <Button onClick={viewQuestions}>View Questions</Button>
      {
        editableExam && (
            <form onSubmit={handleUpdate}>
                <input type="startTime"
                name="startTime"
                value={editableExam.startTime}
                onChange={onChange}
                ></input>
                <input type="endTime"
                name="endTime"
                value={editableExam.endTime}
                onChange={onChange}
                ></input>
                <input type="mcqPassingScore"
                name="mcqPassingScore"
                value={editableExam.mcqPassingScore}
                onChange={onChange}
                ></input>
                <input type="duration"
                name="duration"
                value={editableExam.duration}
                onChange={onChange}
                ></input>
                
                 <button type="submit">Update</button>
                 <button onClick={() => setEditableExam(null)}>Cancel</button>
                 
            </form>
        )
      }
    </div>
  );
};

export default Exam;
