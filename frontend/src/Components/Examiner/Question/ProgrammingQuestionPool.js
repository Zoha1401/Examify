import React, { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Dropdown from "react-bootstrap/Dropdown";
import { Button } from "react-bootstrap";
import { Form } from 'react-bootstrap'; 
import Navigationbar from "../Navigationbar";

const ProgrammingQuestionPool = () => {
  
  const json=useParams()
  const examId=json.examId
  const [checkedState, setCheckedState] = useState({});
  const [progQuestions, setProgQuestions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [difficulty, setDifficulty] = useState(null);
  const [selectedQuestions, setSelectedQuestions] = useState([]);

  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }
  const handleClick = async (value) => {
    setDifficulty(value); // Update state for display
    setLoading(true); 
    try {
        const response = await axiosInstance.get(
          `/programmingQuestion/getProgrammingQuestionByDifficulty?difficulty=${value}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
  
        if (response.status === 200) {
          setProgQuestions(response.data);
        }
      } catch (error) {
        console.error(
          "Error fetching pro questions:",
          error.response?.data || error.message
        );
        alert("Failed to fetch pro questions. Please try again.");
      } finally {
        setLoading(false);
      }
  };


  useEffect(() => {
    setLoading(true);
    const fetchAllProgrammingQuestions = async () => {
      try {
        const response = await axiosInstance.get(
          "/programmingQuestion/getProgrammingQuestionByDifficulty",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        if (response.status === 200) {
          setProgQuestions(response.data);
        }

        const examQuestionsResponse = await axiosInstance.get(
          `/programmingQuestion/getAllProgrammingQuestions?examId=${examId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const examQuestions = examQuestionsResponse.data.map((q) => q.programmingQuestionId);
        const initialCheckedState = {};
        response.data.forEach((question) => {
          initialCheckedState[question.programmingQuestionId] = examQuestions.includes(question.programmingQuestionId);
        });
        setCheckedState(initialCheckedState);

      } catch (error) {
        console.error(
          "Error fetching pro questions:",
          error.response?.data || error.message
        );
        alert("Failed to fetch pro questions. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchAllProgrammingQuestions();
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
    const fullQuestions = progQuestions.filter(q => 
        selectedQuestions.includes(String(q.programmingQuestionId))
      );
    console.log(fullQuestions)
    try{
       const response=await axiosInstance.post(`/programmingQuestion/addProgrammingQuestionList?examId=${examId}`,
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
  
  //Add the selected questions to exam
 
  return (
    <>
    <Navigationbar/>
    <div className="flex justify-center px-2 mx-2 py-3" >

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
      <Link to="/addProgQuestion" className="mx-2">
        <Button>Add new Programming Question</Button>
      </Link> 
      </div>
      <div className="flex-row px-2">
        {progQuestions.length > 0 ? (
          progQuestions.map((question) => (
            <div key={question.programmingQuestionId}  className='px-2 border-1 rounded-lg py-2 my-2'>
                 <Form.Check
                type="checkbox"
                checked={checkedState[question.programmingQuestionId] || false} // Check if the question's checkbox is checked
                onChange={() => handleCheckboxChange(question.programmingQuestionId)} // Handle state change for the specific question
                className="mb-2"
              />
              <p>Question: {question.programmingQuestionText}</p>
              <p>Reference answer: {question.referenceAnswer}</p>
              <div className="flex-col">
          {question.testCases && question.testCases.map((t)=>(
            <div className='flex'>
              <b className="px-2">Input </b>: {t.input}{" "}
              <b className="px-2">Output:</b> {t.expectedOutput}
            </div>
           
          ))}
        </div>
            </div>
          ))
        ) : (
          <p>No questions found for this difficulty level.</p>
        )}
      </div>
      <div className="flex justify-content-end">
      
      <Button onClick={handleAdditionOfSelected} className="mx-2" disabled={selectedQuestions.length === 0}> {selectedQuestions.length > 0 ? "Add Selected Questions" : "No Questions Selected"}</Button> 
      </div>
  
    </>
  );
};

export default ProgrammingQuestionPool;

//ADD question endpoints to be integrated
//Select from pool and add
//Add context for better state management
