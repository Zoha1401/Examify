import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axiosInstance from "../../../utils/axiosInstance";
import Dropdown from "react-bootstrap/Dropdown";
import { Button, Form } from "react-bootstrap"; 
import Navigationbar from '../Navigationbar';

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
      alert("Failed to add selected questions. Please try again.", error.message);
    }
  };
console.log(mcqQuestions)


  return (
    <div>
      <Navigationbar/>
      <div className="flex justify-center px-2 mx-2 py-3">
        <Dropdown className='px-2'>
          <Dropdown.Toggle variant="secondary" className='px-2'>
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

        <Link to={`/addMcqQuestion/${examId}`} className='px-2'>
        <Button>Add new MCQ Question</Button>
      </Link>
      </div>
      <div className="mx-2">
        {loading ? (
          <p>Loading questions...</p>
        ) : mcqQuestions.length > 0 ? (
          mcqQuestions.map((question) => (
            <div key={question.mcqId} className='px-2 border-1 rounded-lg py-2 my-2'>
              <div className='flex'>
              <Form.Check
                type="checkbox"
                checked={checkedState[question.mcqId] || false}
                onChange={() => handleCheckboxChange(question.mcqId)}
                className="mb-2 mx-2"
              />
              <div className='flex-col'><p>Question: {question.mcqQuestionText}</p>
             </div>
              
              </div>
              <div className='flex'>
              {question.options.map((option, index) => (
                  <div key={option.optionId || index} className='px-2'>
                    {option.optionText}{" "}
                    {option.isCorrect && (
                      <strong classname="font-light bg-green-200">
                        (Correct)
                      </strong>
                    )}
                    </div>
              ))}
              </div>
            </div>
            
          ))
        ) : (
          <p>No questions found for this category and difficulty level.</p>
        )}
      </div>
      
      <div className="flex justify-content-end">
      <Button onClick={handleAdditionOfSelected} disabled={selectedQuestions.length === 0} className="mx-2">
        {selectedQuestions.length > 0 ? "Add Selected Questions" : "No Questions Selected"}
      </Button>
      </div>
    </div>
  );
};

export default McqQuestionPool;
