import React, { useState, useEffect } from "react";
import axiosInstance from "../../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { Button, Form, InputGroup } from "react-bootstrap";

const Exam = ({ temp_exam, onDelete, onUpdate }) => {
  const [editableExam, setEditableExam] = useState(null);
  const [examinees, setExaminees] = useState([]);
  const [checkedState, setCheckedState] = useState(false);
  const [showExaminees, setShowExaminees] = useState(false);
  const [selectedExaminees, setSelectedExaminees] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const toggleDropDown = () => {
    setShowExaminees(!showExaminees);
  };

  //Navigate to login if not authorized
  let navigate = useNavigate();
  console.log(temp_exam);
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  //Fetch all examinees belonging to exam
  useEffect(() => {
    const fetchExaminees = async () => {
      try {
        const examExamineesFetched = await axiosInstance.get(
          `/exam/getExaminees?examId=${temp_exam.examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const response = await axiosInstance.get(`/examiner/getAllExaminee`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        console.log(examExamineesFetched.data);
        setExaminees(response.data);

        //If examinee is assigned exam, set checked
        const examExaminees = examExamineesFetched.data.map(
          (e) => e.examineeId
        );
        const initialCheckedState = {};
        response.data.forEach((examinee) => {
          initialCheckedState[examinee.examineeId] = examExaminees.includes(
            examinee.examineeId
          );
          console.log(
            "examinee Id ",
            examinee.examineeId,
            "Initial state ",
            initialCheckedState
          );
        });

        setCheckedState(initialCheckedState);

        // setExam(examData);
      } catch (error) {
        console.error("Error fetching examinees", error.message);
      }
    };
    fetchExaminees();
  }, [temp_exam.examId]);

  console.log("Tempp exam", temp_exam.startTime);

  //Delete exam
  const handleDelete = async () => {
    try {
      const response = await axiosInstance.delete(
        `/exam/deleteExam?examId=${temp_exam.examId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        alert("Exam deleted");
        onDelete(temp_exam.examId);
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
    setEditableExam(temp_exam);
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

      alert("Exam updated");
      onUpdate(response.data);
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

  //Navigate to questions
  const viewQuestions = () => {
    navigate(`/exam-detail/${temp_exam.examId}`);
  };

  //Navigate to answers
  const viewAnswers = () => {
    navigate(`/examAnswers/${temp_exam.examId}`);
  };
  const extractDateTime = (datetime) => {
    const [date, time_temp] = datetime.split("T");
    const time = time_temp.slice(0, 5);
    return { date, time };
  };

  const { date: date1, time: time1 } = extractDateTime(temp_exam.startTime);
  const { date: date2, time: time2 } = extractDateTime(temp_exam.endTime);

  const handleCheckboxChange = (id) => {
    setCheckedState((prevState) => {
      const updatedState = { ...prevState, [id]: !prevState[id] };
      updateSelectedExaminees(updatedState);
      return updatedState;
    });
  };

  const updateSelectedExaminees = (updatedState) => {
    const selectedExaminees = Object.keys(updatedState).filter(
      (id) => updatedState[id]
    );
    setSelectedExaminees(selectedExaminees);
  };

  console.log(selectedExaminees);
  const handleAddSelectedExaminees = () => {
    const toBeAddedexaminees = examinees.filter((e) =>
      selectedExaminees.includes(String(e.examineeId))
    );
    try {
      const response = axiosInstance.post(
        `/examiner/assignToSpecificExaminee?examId=${temp_exam.examId}`,
        toBeAddedexaminees,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Examinees succesfully added to exam");
      navigate("/examiner-dashboard");
    } catch (error) {
      console.error(
        "Error adding examinees:",
        error.response?.data || error.message
      );
      alert("Error adding examinees. Please try again.");
    }
  };

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const filteredExaminees = examinees.filter(
    (examinee) =>
      examinee.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      examinee.degree.toLowerCase().includes(searchTerm.toLowerCase()) ||
      examinee.college.toLowerCase().includes(searchTerm.toLowerCase()) ||
      String(examinee.year).includes(searchTerm)
  );

  const handleAddAllExaminees = () => {
    try {
      const response = axiosInstance.post(
        `/examiner/assignToAll?examId=${temp_exam.examId}`,
        examinees,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Examinees succesfully added to exam");
      navigate("/examiner-dashboard");
    } catch (error) {
      console.error(
        "Error adding examinees:",
        error.response?.data || error.message
      );
      alert("Error adding examinees. Please try again.");
    }
  };

  return (
    <div className="flex flex-col">
      <div className="flex flex-row mb-3 mx-2 mt-2 my-2 border-1 px-2 py-2 rounded-lg justify-between">
        <div className="flex flex-wrap">
          <h4 className="mx-2">{temp_exam.examId}</h4>
          <h4 className="mx-2 px-2">{date1}</h4>
          <h4 className="mx-2 px-2">{time1}</h4>
          <h4 className="mx-2 px-2">{time2}</h4>
          <h4 className="mx-2 px-2">{temp_exam.mcqpassingScore}</h4>
          <h4 className="mx-2 px-10">{temp_exam.duration} min</h4>
        </div>

        <div className="flex mx-2">
          <Button onClick={handleDelete} variant="danger" className="mx-2">
            Delete
          </Button>
          <Button onClick={handleEdit} variant="primary" className="mx-2">
            Update
          </Button>
          <Button onClick={viewQuestions} variant="dark" className="mx-2">
            View Questions
          </Button>
          <Button onClick={viewAnswers} variant="dark" className="mx-2">
            View Answers
          </Button>
          <Button
            onClick={toggleDropDown}
            className="hover:cursor-pointer"
            variant="success"
          >
            Examinees
          </Button>
        </div>
      </div>

      {editableExam && (
        <form
          onSubmit={handleUpdate}
          className="mt-4 p-4 bg-gray-100 rounded-lg shadow-sm"
        >
          <div className="grid grid-cols-2 gap-4">
            <input
              type="text"
              name="startTime"
              value={editableExam.startTime}
              onChange={onChange}
              placeholder="Start Time"
              className="p-2 border border-gray-300 rounded-md"
            />
            <input
              type="text"
              name="endTime"
              value={editableExam.endTime}
              onChange={onChange}
              placeholder="End Time"
              className="p-2 border border-gray-300 rounded-md"
            />
            <input
              type="text"
              name="mcqpassingScore"
              value={editableExam.mcqpassingScore}
              onChange={onChange}
              placeholder="MCQ Passing Score"
              className="p-2 border border-gray-300 rounded-md"
            />
            <input
              type="text"
              name="duration"
              value={editableExam.duration}
              onChange={onChange}
              placeholder="Duration (min)"
              className="p-2 border border-gray-300 rounded-md"
            />
          </div>
          <div className="mt-4 flex justify-end space-x-3">
            <button
              type="submit"
              className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition"
            >
              Update
            </button>
            <button
              onClick={() => setEditableExam(null)}
              className="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition"
            >
              Cancel
            </button>
          </div>
        </form>
      )}

      {showExaminees && (
        <div className="mt-3 p-3 border rounded bg-light shadow-sm">
          <h5 className="mb-3">Examinees</h5>
          <InputGroup className="mb-3">
            <Form.Control
              type="text"
              placeholder="Search examinees..."
              onChange={handleSearch}
              value={searchTerm}
            />
          </InputGroup>
          <div className="max-h-64 overflow-y-auto">
            {filteredExaminees.map((examinee) => (
              <div
                key={examinee.examineeId}
                className="flex flex-row items-center border-b justify-between mb-2"
              >
                <div className="flex-1 px-2">{examinee.email}</div>
                <div className="flex-1 px-2">{examinee.degree}</div>
                <div className="flex-1 px-2">{examinee.college}</div>
                <div className="flex-1 px-2">{examinee.year}</div>
                <Form.Check
                  type="checkbox"
                  checked={checkedState[examinee.examineeId || false]}
                  onChange={() => handleCheckboxChange(examinee.examineeId)}
                />
              </div>
            ))}
            <Button variant="dark" onClick={handleAddSelectedExaminees}>
              Add selected examinees
            </Button>
            <Button
              variant="dark"
              onClick={handleAddAllExaminees}
              className="mx-2"
            >
              Add all examinees
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Exam;
