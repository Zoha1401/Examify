import React, { useEffect, useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Dropdown } from "react-bootstrap";
import { ClipboardEvent } from "react"

const GiveExam = () => {
  const { examId } = useParams();

  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);
  const [exam, setExam] = useState({});
  const [mcqQuestions, setMcqQuestions] = useState([]);
  const [programmingQuestions, setProgrammingQuestions] = useState([]);
  const [codeAnswers, setCodeAnswers] = useState([]);
  const [mcqAnswers, setMcqAnswers] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [deadline, setDeadline] = useState(0);
  const [clipBoardContent, setClipBoardContent]= useState("")
  const [isExamPaused, setIsExamPaused] = useState(false);

  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examinee-login");
  }

  
  useEffect(() => {
    const fetchExamById = async () => {
      try {
        const examResponse = await axiosInstance.get(
          `/examinee/getExamById?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(examResponse);
        const { duration } = examResponse.data;
        setExam(examResponse.data);
        console.log("Duration:", duration);
        if (!duration || duration <= 0) {
          console.error("Invalid duration:", duration);
          alert("Invalid exam duration.");
          return;
        }
        const calculatedEndTime = Date.now() + duration * 60 * 1000;
        setDeadline(calculatedEndTime);
      } catch (error) {
        console.error("Error fetching exam:", error, error.message);
        alert("Failed to fetch exam. Please try again.");
      }
    };
    const fetchQuestions = async () => {
      try {
        const mcqResponse = await axiosInstance.get(
          `/examinee/getAllMcqQuestions?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const programmingResponse = await axiosInstance.get(
          `/examinee/getAllProgrammingQuestions?examId=${examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(mcqResponse.data);
        setMcqQuestions(mcqResponse.data);
        setProgrammingQuestions(programmingResponse.data);
      } catch (error) {
        console.error("Error fetching MCQ:", error, error.message);
        alert("Failed to fetch MCQ. Please try again.");
      }
    };

    if (token && examId) {
      fetchQuestions();
      fetchExamById();
    }
  }, [token, examId]);


  useEffect(() => {
    const handleVisibilityChange = () => {
      if (document.hidden) {
        setIsExamPaused(true);
      }
    };
    const disableKeys=(event)=>{
      const forbiddenKeys=["Tab", "Alt", "Shift", "Meta"]
      if(forbiddenKeys.includes(event.key)){
        event.preventDefault();
        alert(`Usage of ${event.key} is not allowed outside exam`)
      }
    }

    window.addEventListener("keydown", disableKeys)
    document.addEventListener("visibilitychange", handleVisibilityChange);

    return () => {
      window.addEventListener("keydown", disableKeys)
      document.removeEventListener("visibilitychange", handleVisibilityChange);
    };
  }, []);

 

  

  useEffect(() => {
    if (!deadline) return; // Ensure deadline is set
    console.log("Deadline is ", deadline);
    const interval = setInterval(() => {
      
      const now = Date.now();
      const timeLeft = deadline - now;

      if (timeLeft <= 0) {
        clearInterval(interval);
        setMinutes(0);
        setSeconds(0);
        alert("Time's up!");
        handleSubmit(); // Automatically submit the exam
        return;
      }

      setMinutes(Math.floor((timeLeft / 1000 / 60) % 60));
      setSeconds(Math.floor((timeLeft / 1000) % 60));
    }, 1000);

    return () => clearInterval(interval); // Cleanup on unmount
  }, [deadline, isExamPaused]);

  const handleOptionChange = (mcqQuestionId, optionId) => {
    setMcqAnswers((prevAnswers) => {
      const updatedAnswers = prevAnswers.filter(
        (a) => a.mcqQuestionId !== mcqQuestionId
      );
      return [...updatedAnswers, { mcqQuestionId, selectedOptionId: optionId }];
    });
    console.log(mcqAnswers);
  };

  const onChangeCodeAnswer = (pqId, language, code) => {
    setCodeAnswers((prev) => {
      const updatedAnswers = prev.filter((a) => a.pqId !== pqId);
      return [...updatedAnswers, { pqId, language, codeSubmission: code }];
    });
  };

  const handleLanguageChange = (pqId, newLanguage) => {
    setCodeAnswers((prev) => {
      const existingAnswer = prev.find((a) => a.pqId === pqId);
      if (existingAnswer) {
        return prev.map((a) =>
          a.pqId === pqId ? { ...a, language: newLanguage } : a
        );
      } else {
        return [...prev, { pqId, language: newLanguage, codeSubmission: "" }];
      }
    });
  };

  // const preventCopyPaste = (e) => {
  //   e.preventDefault()
  //   alert("Copying and pasting is not allowed!")
  // }

  const handleCopy = (e) => {
    const selectedText = e.target.value.substring(
      e.target.selectionStart,
      e.target.selectionEnd
    );
    if (selectedText) {
      e.clipboardData.setData("text/plain", selectedText);
      setClipBoardContent(selectedText); // Store copied text internally
      e.preventDefault();
    }
  };

  const handleCut = (e) => {
    const selectedText = e.target.value.substring(
      e.target.selectionStart,
      e.target.selectionEnd
    );
    if (selectedText) {
      e.clipboardData.setData("text/plain", selectedText);
      setClipBoardContent(selectedText);
      e.target.value = e.target.value.selectionEnd
      e.preventDefault();
    }
  };
  

  const handlePaste = (e) => {
    const pastedText = e.clipboardData.getData("text/plain");
    if (pastedText !== clipBoardContent) {
      e.preventDefault();
      alert("Pasting from outside is not allowed!");
    }
  };


  const renderMcq = (mcq) => {
    return (
      <div className="bg-gray-50 rounded-md px-2">
        <h2 className="select-none text-lg font-bold mb-4">Question {currentQuestionIndex + 1}</h2>
        <p className="select-none text-gray-700 mb-4">{mcq.mcqQuestionText}</p>
        <div className="options-container space-y-3">
          {mcq.options.map((option) => (
            <div key={option.optionId} className="flex items-center space-x-2 bg-white px-2 py-4 rounded-md shadow-sm">
              <input
                type="radio"
                name={`mcq-${mcq.mcqId}`}
                value={option.optionId}
                checked={
                  mcqAnswers.find((a) => a.mcqQuestionId === mcq.mcqId)
                    ?.selectedOptionId === option.optionId
                }
                onChange={() => handleOptionChange(mcq.mcqId, option.optionId)}
              />
              {option.optionText}
            </div>
          ))}
        </div>
      </div>
    );
  };

  const renderProgrammingQuestion = (pq) => {
    const currentAnswer =
      codeAnswers.find((a) => a.pqId === pq.programmingQuestionId) || {};
    const { language = "C++", codeSubmission = "" } = currentAnswer;
    console.log(codeAnswers);
  
    return (
      <div className="flex flex-col lg:flex-row gap-6">
      {/* Left Column: Question and Test Cases */}
      <div className="lg:w-1/2 w-full bg-gray-50 p-6 rounded-lg shadow-md">
        <h2 className="text-lg font-bold mb-4 select-none">Question {currentQuestionIndex + 1}</h2>
        <p className="text-gray-700 mb-6 select-none">{pq.programmingQuestionText}</p>
    
        <div className="space-y-4">
          <h3 className="text-md font-semibold select-none">Test Cases:</h3>
          {pq.testCases.map((t) => (
            <div key={t.testcaseId} className="bg-white p-3 rounded-md shadow-sm">
              <p>
                <span className="font-medium select-none">Input:</span> {t.input}
              </p>
              <p>
                <span className="font-medium select-none">Expected Output:</span> {t.expectedOutput}
              </p>
            </div>
          ))}
        </div>
      </div>
    
      {/* Right Column: Language Dropdown and Text Area */}
      <div className="lg:w-1/2 w-full bg-gray-50 p-6 rounded-lg shadow-md">
        <div className="mb-6">
          <label htmlFor={`languageDropdown-${pq.programmingQuestionId}`} className="block mb-2 font-medium">
            Select Language:
          </label>
          <Dropdown>
            <Dropdown.Toggle variant="secondary">{language}</Dropdown.Toggle>
            <Dropdown.Menu>
              {["C++", "Java", "Python"].map((lang) => (
                <Dropdown.Item
                  key={lang}
                  onClick={() =>
                    handleLanguageChange(pq.programmingQuestionId, lang)
                  }
                >
                  {lang}
                </Dropdown.Item>
              ))}
            </Dropdown.Menu>
          </Dropdown>
        </div>
    
        <label
          htmlFor={`codeSubmission-${pq.programmingQuestionId}`}
          className="block mb-4 font-medium"
        >
          Your Code:
        </label>
        <textarea
          id={`codeSubmission-${pq.programmingQuestionId}`}
          rows="15"
          className="w-full p-3 rounded-md border shadow-sm focus:ring focus:ring-indigo-300"
          value={codeSubmission}
          onChange={(e) =>
            onChangeCodeAnswer(pq.programmingQuestionId, language, e.target.value)
          }
          onCopy={(e) => handleCopy(e)}
          onPaste={(e) => handlePaste(e)}
          onCut={(e) => handleCut(e)}
        ></textarea>
      </div>
    </div>
    
    );
  };

  const handleSubmit = async () => {
    try {
      const payload = {
        mcqAnswers,
        programmingQuestionAnswers: codeAnswers,
      };

      console.log(payload);
      const response = await axiosInstance.post(
        `/examinee/submitExam?examId=${examId}`,
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(response);
      alert("Exam submitted");
      //Submit screen
      navigate("/submitPage");
    } catch (error) {
      console.error("Error submiting exam:", error, error.message);
      alert("Failed to submit exam. Please try again.");
    }
  };
  const currentMcq = mcqQuestions[currentQuestionIndex];
  const currentProgrammingQuestion =
    programmingQuestions[currentQuestionIndex - mcqQuestions.length];

  return (
    <>
     <div className="flex flex-row justify-between">

      <div className="flex flex-rows px-2 py-2 bg-indigo-100 rounded-md ml-3 mt-3">
           <span className="px-2 font-bold">Time Left:</span>
           <p id="minute">{minutes < 10 ? "0" + minutes : minutes}</p>
           :
       
            <p id="second">{seconds < 10 ? "0" + seconds : seconds}</p>
      </div>
      
      <div className="px-2"><Button
    onClick={handleSubmit}
    variant="success"
    className="px-4 py-2 mt-3 mr-3 font-semibold rounded-lg shadow-md hover:bg-green-700"
  >
    Submit
  </Button></div>
     </div>
           

      <div className="bg-white p-6 mb-6">
        {currentQuestionIndex < mcqQuestions.length ? (
          renderMcq(currentMcq)
        ) : currentProgrammingQuestion ? (
          renderProgrammingQuestion(currentProgrammingQuestion)
        ) : (
          <div>No questions found.</div>
        )}

        <div className="navigation-container flex justify-between items-center py-4">
          <button
            disabled={currentQuestionIndex === 0}
            onClick={() => setCurrentQuestionIndex((prev) => prev - 1)}
            className="px-4 py-2 bg-gray-200 rounded-lg shadow-md disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Previous
          </button>

          <button
            disabled={
              currentQuestionIndex ===
              mcqQuestions.length + programmingQuestions.length - 1
            }
            onClick={() => setCurrentQuestionIndex((prev) => prev + 1)}
            className="px-4 py-2 bg-indigo-600 text-white rounded-lg shadow-md hover:bg-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            Next
          </button>
        </div>
      
      </div>
    </>
  );
};

export default GiveExam;

//For this week

//Integrate Frontend with submit exam
//Integrate assign to all examinees
