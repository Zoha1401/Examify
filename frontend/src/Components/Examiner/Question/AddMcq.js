import React, { useState } from "react";
import { Button, Dropdown } from "react-bootstrap";
import { Link, useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";


const AddMcq = () => {

  const json=useParams();
  const examId=json.examId;
  const [details, setDetails] = useState({
    mcqQuestionText: "",
    correctAnswer: "",
    difficulty: null,
    category: null,
    options:[]
  });
  const [options, setOptions] = useState([{optionText:"", isCorrect:false}]);
  console.log(options)

  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }
  const handleAddMcq=async(e)=>{
    e.preventDefault()
    console.log("Exam ID:", examId);
    console.log("Details sent to API:", { ...details, options });
       try{
        const response=await axiosInstance.post(`/mcqQuestion/addMcqQuestion?examId=${examId}`,
          {
            ...details,
            options
          },
          {
            headers:{
              Authorization: `Bearer ${token}`
            },
           
          }
        )
        console.log(response)
        if(response.status===201){
          window.alert("MCQ added successfully")
          console.log("Sending data to backend:", details);
          navigate(`/exam-detail/${examId}`)
          
        }
       }
       catch(error){
        console.error("Error adding MCQ:", error, error.message);
        alert("Failed to add MCQ. Please try again.");
       }
  }

  const handleAddOption = () => {
    setOptions([...options, { optionText: "", isCorrect: false }]);
  };

  const onChange=(e)=>{
    setDetails({...details, [e.target.name]:e.target.value})
  }

  const handleRemoveOption = (index) => {
    const updatedOptions = options.filter((_, i) => i !== index);
    setOptions(updatedOptions);
  };

  const handleOptionChange = (index, field, value) => {
    const updatedOptions = options.map((option, i) =>
      i === index ? { ...option, [field]: value } : option
    );

    if (field === "isCorrect" && value) {
      setDetails({ ...details, correctAnswer: options[index].optionText });
    } else {
      setDetails({ ...details, correctAnswer: "" });
    }
    setOptions(updatedOptions);
  };

  return (
    <>

      <Navigationbar/>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-10 lg:px-8">
       
          <h2 className="mt-1 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
            Add Mcq
          </h2>
       

        <div className="mt-6 sm:mx-auto sm:w-full sm:max-w-sm bg-gray-100 px-4 rounded-lg py-2 shadow-xl">
        <form onSubmit={handleAddMcq} className="mt-4 space-y-5">
          <div>
            <label htmlFor="mcqQuestionText" className="block font-medium">
              Question
            </label>
            <input
              id="mcqQuestionText"
              name="mcqQuestionText"
              required
              value={details.mcqQuestionText}
              onChange={onChange}
              className="block w-full rounded-md border px-3 py-2"
            />
          </div>

          {/* Difficulty Dropdown */}
          <Dropdown>
            <Dropdown.Toggle variant="secondary">
              {details.difficulty || "Select Difficulty"}
            </Dropdown.Toggle>
            <Dropdown.Menu>
              {["Easy", "Medium", "Hard"].map((level) => (
                <Dropdown.Item
                  key={level}
                  onClick={() => setDetails({ ...details, difficulty: level })}
                >
                  {level}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>

          {/* Category Dropdown */}
          <Dropdown>
            <Dropdown.Toggle variant="secondary">
              {details.category || "Select Category"}
            </Dropdown.Toggle>
            <Dropdown.Menu>
              {["Aptitude", "Technical", "Other"].map((cat) => (
                <Dropdown.Item
                  key={cat}
                  onClick={() => setDetails({ ...details, category: cat })}
                >
                  {cat}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>

          {/* Options Management */}
          <div>
            <h4>Options</h4>
            {options.map((option, index) => (
              <div key={index} className="flex items-center space-x-2 mb-2">
                <input
                  type="text"
                  placeholder="Option Text"
                  value={option.optionText}
                  onChange={(e) =>
                    handleOptionChange(index, "optionText", e.target.value)
                  }
                  className="flex-1 rounded-md border px-3 py-2"
                />
                <input
                  type="checkbox"
                  checked={option.isCorrect}
                  onChange={(e) =>
                    handleOptionChange(index, "isCorrect", e.target.checked)
                  }
                />
                <Button variant="danger" onClick={() => handleRemoveOption(index)}>
                  Remove
                </Button>
              </div>
            ))}
            <Button variant="secondary" onClick={handleAddOption}>
              Add Option
            </Button>
          </div>

          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 rounded-md"
          >
            Add MCQ
          </button>
        </form>
        </div>
      </div>
    </>
  );
};

export default AddMcq;
