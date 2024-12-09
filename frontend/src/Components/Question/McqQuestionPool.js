import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axiosInstance from "../../utils/axiosInstance";
import Dropdown from "react-bootstrap/Dropdown";
import { Button, Form } from "react-bootstrap"; 

const McqQuestionPool = () => {
  const { examId } = useParams();
  const [checkedState, setCheckedState] = useState({});
  const [mcqQuestions, setMcqQuestions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [difficulty, setDifficulty] = useState(null);
  const [category, setCategory] = useState(null);
  const [selectedQuestions, setSelectedQuestions] = useState([]);

  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      alert("You are not authorized, please login again");
      navigate("/examiner-login");
    }
  }, [token, navigate]);

  useEffect(() => {
    const fetchQuestions = async () => {
      setLoading(true);
      try {
        const response = await axiosInstance.get(
          `/mcqQuestion/getMcqQuestionByDifficultyAndCategory?category=${category}&difficulty=${difficulty}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setMcqQuestions(response.data);

        const examQuestionsResponse = await axiosInstance.get(
          `/exam/getAllMcqQuestions?examId=${examId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const examQuestions = examQuestionsResponse.data.map((q) => q.mcqId);
        const initialCheckedState = {};
        response.data.forEach((question) => {
          initialCheckedState[question.mcqId] = examQuestions.includes(question.mcqId);
        });
        setCheckedState(initialCheckedState);
      } catch (error) {
        console.error("Error fetching questions:", error);
        alert("Failed to fetch questions. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchQuestions();
  }, [difficulty, category, token, examId]);

  const handleCheckboxChange = (id) => {
    setCheckedState((prevState) => {
      const updatedState = { ...prevState, [id]: !prevState[id] };
      updateSelectedQuestions(updatedState);
      return updatedState;
    });
  };

  const updateSelectedQuestions = (updatedState) => {
    const selectedQuestions = Object.keys(updatedState).filter(
      (id) => updatedState[id]
    );
    setSelectedQuestions(selectedQuestions);
  };

  const handleAdditionOfSelected = async () => {
    const fullQuestions = mcqQuestions.filter((q) =>
      selectedQuestions.includes(String(q.mcqId))
    );

    try {
      const response = await axiosInstance.post(
        `/exam/addMcqQuestionList?examId=${examId}`,
        fullQuestions,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        alert("Selected questions added successfully!");
        setSelectedQuestions([]);
        navigate(`/exam-detail/${examId}`);
      }
    } catch (error) {
      console.error("Error adding selected questions:", error);
      alert("Failed to add selected questions. Please try again.");
    }
  };

  return (
    <div>
      <div className="flex">
        <Dropdown>
          <Dropdown.Toggle variant="secondary">
            {difficulty || "Select Difficulty"}
          </Dropdown.Toggle>
          <Dropdown.Menu>
            <Dropdown.Item onClick={() => setDifficulty("Easy")}>Easy</Dropdown.Item>
            <Dropdown.Item onClick={() => setDifficulty("Medium")}>Medium</Dropdown.Item>
            <Dropdown.Item onClick={() => setDifficulty("Hard")}>Hard</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
        <Dropdown>
          <Dropdown.Toggle variant="secondary">
            {category || "Select Category"}
          </Dropdown.Toggle>
          <Dropdown.Menu>
            <Dropdown.Item onClick={() => setCategory("Aptitude")}>Aptitude</Dropdown.Item>
            <Dropdown.Item onClick={() => setCategory("Technical")}>Technical</Dropdown.Item>
            <Dropdown.Item onClick={() => setCategory("Other")}>Other</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </div>
      <div className="mx-2">
        {loading ? (
          <p>Loading questions...</p>
        ) : mcqQuestions.length > 0 ? (
          mcqQuestions.map((question) => (
            <div key={question.mcqId} className="flex">
              <Form.Check
                type="checkbox"
                checked={checkedState[question.mcqId] || false}
                onChange={() => handleCheckboxChange(question.mcqId)}
                className="mb-2"
              />
              <p>Question: {question.mcqQuestionText}</p>
              <p>Answer: {question.correctAnswer}</p>
              <p>Difficulty: {question.difficulty}</p>
              <p>Category: {question.category}</p>
            </div>
          ))
        ) : (
          <p>No questions found for this category and difficulty level.</p>
        )}
      </div>
      <Link to={`/addMcqQuestion/${examId}`}>
        <Button>Add MCQ Question</Button>
      </Link>
      <Button onClick={handleAdditionOfSelected} disabled={selectedQuestions.length === 0}>
        {selectedQuestions.length > 0 ? "Add Selected Questions" : "No Questions Selected"}
      </Button>
    </div>
  );
};

export default McqQuestionPool;
