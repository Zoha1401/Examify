import React, { useState } from "react";
import { Button, Dropdown } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import Navigationbar from "../Navigationbar";

const AddProgrammingQuestion = () => {
  const { examId } = useParams();
  const [progQuestion, setProgQuestion] = useState([
    { programmingQuestionText: "", referenceAnswer: "", testCases: [] },
  ]);
  const [testCases, setTestCases] = useState([
    { input: "", expectedOutput: "" },
  ]);
  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  const handleAddProgrammingQuestion = async (e) => {
    e.preventDefault();

    console.log("Exam ID:", examId);
    console.log("Details sent to API:", { ...progQuestion, testCases });
    try {
      const response = await axiosInstance.post(
        `/programmingQuestion/addProgrammingQuestion?examId=${examId}`,
        {
          ...progQuestion,
          testCases,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response);
      if (response.status === 200) {
        alert("programming question added successfully");
        console.log("Sending data to backend:", progQuestion);
        navigate(`/exam-detail/${examId}`);
      }
    } catch (error) {
      console.error("Error adding programming:", error, error.message);
      alert("Failed to add programming. Please try again.");
    }
  };

  const handleAddTestCase = async () => {
    setTestCases([...testCases, { input: "", expectedOutput: "" }]);
  };

  const onChange = (e) => {
    setProgQuestion({ ...progQuestion, [e.target.name]: e.target.value });
  };

  const handleRemoveTestCase = (index) => {
    const updatedTestCases = testCases.filter((_, i) => i !== index);
    setTestCases(updatedTestCases);
  };

  const handleTestCaseChange = (index, field, value) => {
    const updatedTestCases = testCases.map((testCase, i) =>
      i === index ? { ...testCase, [field]: value } : testCase
    );
    setTestCases(updatedTestCases);
  };
  return (
    <>
      <Navigationbar />
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
            Add Programming Question
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form
            onSubmit={handleAddProgrammingQuestion}
            className="space-y-6 mt-10"
          >
            <div>
              <label htmlFor="mcqQuestionText" className="block font-medium">
                Question
              </label>
              <input
                id="programmingQuestionText"
                name="programmingQuestionText"
                required
                value={progQuestion.programmingQuestionText}
                onChange={onChange}
                className="block w-full rounded-md border px-3 py-2"
              />
            </div>

            {/* Difficulty Dropdown */}
            <Dropdown>
              <Dropdown.Toggle variant="secondary">
                {progQuestion.difficulty || "Select Difficulty"}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {["Easy", "Medium", "Hard"].map((level) => (
                  <Dropdown.Item
                    key={level}
                    onClick={() =>
                      setProgQuestion({ ...progQuestion, difficulty: level })
                    }
                  >
                    {level}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>

            <div>
              <label htmlFor="referenceAnswer" className="block font-medium">
                Reference Answer
              </label>
              <input
                id="referenceAnswer"
                name="referenceAnswer"
                required
                value={progQuestion.referenceAnswer}
                onChange={onChange}
                className="block w-full rounded-md border px-3 py-2"
              />
            </div>

            {/* Testcase Management */}
            <div>
              <h4>Test Cases</h4>
              {testCases.map((testCase, index) => (
                <div key={index} className="flex items-center space-x-2 mb-2">
                  <input
                    type="text"
                    placeholder="Example"
                    value={testCase.input}
                    onChange={(e) =>
                      handleTestCaseChange(index, "input", e.target.value)
                    }
                    className="flex-1 rounded-md border px-3 py-2"
                  />
                  <input
                    type="text"
                    placeholder="Expected Output"
                    value={testCase.expectedOutput}
                    onChange={(e) =>
                      handleTestCaseChange(
                        index,
                        "expectedOutput",
                        e.target.value
                      )
                    }
                    className="flex-1 rounded-md border px-3 py-2"
                  />

                  <Button
                    variant="danger"
                    onClick={() => handleRemoveTestCase(index)}
                  >
                    Remove
                  </Button>
                </div>
              ))}
              <Button variant="secondary" onClick={handleAddTestCase}>
                Add Test Case
              </Button>
            </div>

            <button
              type="submit"
              className="w-full bg-indigo-600 text-white py-2 rounded-md"
            >
              Add Programming Question
            </button>
          </form>
        </div>
      </div>
    </>
  );
};

export default AddProgrammingQuestion;
