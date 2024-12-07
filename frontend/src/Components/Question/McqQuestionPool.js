import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import axiosInstance from "../../utils/axiosInstance";
import Dropdown from "react-bootstrap/Dropdown";
import { Button } from "react-bootstrap";
import { Form } from 'react-bootstrap'; 

const McqQuestionPool = () => {
    const json=useParams()
    const examId=json.examId
    const [checkedState, setCheckedState] = useState({});
    const [mcqQuestions, setMcqQuestions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [difficulty, setDifficulty] = useState(null);
    const [category, setCategory]=useState(null);
    const [selectedQuestions, setSelectedQuestions] = useState([]);
  
    let navigate = useNavigate();
    const token = localStorage.getItem("token");
    console.log(token);
    if (!token) {
      alert("You are not authorized please login again");
      navigate("/examiner-login");
    }
    const handleClick = async (difficulty, category) => {
      setDifficulty(difficulty);
      setCategory(category) // Update state for display
      setLoading(true); 
      try {
          const response = await axiosInstance.get(
            `/mcqQuestion/getMcqQuestionByDifficultyAndCategory?category=${category}&difficulty=${difficulty}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
    
          if (response.status === 200) {
            setMcqQuestions(response.data);
          }
        } catch (error) {
          console.error(
            "Error fetching mcq questions:",
            error.response?.data || error.message
          );
          alert("Failed to fetch mcq questions. Please try again.");
        } finally {
          setLoading(false);
        }
    };
  
  
    useEffect(() => {
      setLoading(true);
      const fetchAllMcqQuestions = async () => {
        try {
          const response = await axiosInstance.get(
            "/mcqQuestion/getMcqQuestionByDifficultyAndCategory",
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          if (response.status === 200) {
            setMcqQuestions(response.data);
          }
        } catch (error) {
          console.error(
            "Error fetching mcq questions:",
            error.response?.data || error.message
          );
          alert("Failed to fetch mcq questions. Please try again.");
        } finally {
          setLoading(false);
        }
      };
      fetchAllMcqQuestions();
    }, [token]);
  
  //   const handleChange = (event) => {
  //     setChecked(event.target.checked);
  //   };
    const handleCheckboxChange = (id) => {
      setCheckedState((prevState) => {
          const updatedState = { ...prevState, [id]: !prevState[id] };
          updateSelectedQuestions(updatedState);
          return updatedState;
        });
  
    };
  
    // Update the selected questions list based on checked state
    const updateSelectedQuestions = (updatedState) => {
      const selectedQuestions = Object.keys(updatedState).filter(
        (id) => updatedState[id] // Only include checked questions
      );
      setSelectedQuestions(selectedQuestions);
    };
  
    const handleAdditionOfSelected=async()=>{
      console.log("Selected Questions:", selectedQuestions)
      const fullQuestions = mcqQuestions.filter(q => 
          selectedQuestions.includes(String(q.mcqId))
        );
      console.log(fullQuestions)
      try{
         const response=await axiosInstance.post(`/exam/addMcqQuestionList?examId=${examId}`,
           fullQuestions,
          {headers: { Authorization: `Bearer ${token}` }},
         
         )
         if (response.status === 200) {
          alert("Selected questions added successfully!");
          // Optionally reset the selected questions
          setSelectedQuestions([]);
          navigate(`/exam-detail/${examId}`)
        }
      }
  
      catch(error){
          console.error("Error adding selected questions:", error);
          alert("Failed to add selected questions. Please try again.");
      }
    }
    
  return (
    <div>
        <div className="flex">
        <Dropdown data-bs-theme="dark">
        <Dropdown.Toggle id="dropdown-button-dark-example1" variant="secondary">
          {difficulty || "Select Difficulty"}
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item value="Easy" name="easy" onClick={() => handleClick("Easy")}>
            Easy
          </Dropdown.Item>
          <Dropdown.Item value="Medium" name="medium" onClick={() => handleClick("Medium")}>
            Medium
          </Dropdown.Item>
          <Dropdown.Item value="Hard" name="hard" onClick={() => handleClick("Hard")}>
            Hard
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>

      <Dropdown data-bs-theme="dark">
        <Dropdown.Toggle id="dropdown-button-dark-example1" variant="secondary">
          {category || "Select Category"}
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item value="Aptitude" name="aptitude" onClick={() => handleClick("Easy")}>
            Aptitude
          </Dropdown.Item>
          <Dropdown.Item value="Technical" name="technical" onClick={() => handleClick("Medium")}>
            Technical
          </Dropdown.Item>
          <Dropdown.Item value="Other" name="other" onClick={() => handleClick("Hard")}>
            Other
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>
      </div>
      <div className="mx-2">
      {mcqQuestions.length > 0 ? (
          mcqQuestions.map((question) => (
            <div key={question.mcqId} className="flex">
                 <Form.Check
                type="checkbox"
                checked={checkedState[question.mcqId] || false} // Check if the question's checkbox is checked
                onChange={() => handleCheckboxChange(question.mcqId)} // Handle state change for the specific question
                className="mb-2"
              />
              <p>Question: {question.questionText}</p>
              <p>Answer: {question.correctAnswer}</p>
              <p>Difficulty: {question.difficulty}</p>
              <p>Category: {question.category}</p>
            </div>
          ))
        ) : (
          <p>No questions found for this category and difficulty level.</p>
        )}
      </div>
      <Link to="/addMcqQuestion"><Button>Add new MCQ</Button></Link>
      <Button onClick={handleAdditionOfSelected} disabled={selectedQuestions.length === 0}> {selectedQuestions.length > 0 ? "Add Selected Questions" : "No Questions Selected"}</Button> 
    </div>
  );
}

export default McqQuestionPool;
