import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import ProgrammingQuestion from "./ProgrammingQuestion";
import Mcq from "./Mcq";
import { Button } from "react-bootstrap";
import Navigationbar from "../Navigationbar";

const ExamQuestions = () => {
  const json = useParams();
  console.log(json.examId);
  const examId = json.examId;
  console.log(examId);
  let navigate = useNavigate();

  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  const [technicalMcqs, setTechnicalMcqs] = useState([]);
  const [aptitudeMcqs, setAptitudeMcqs] = useState([]);
  const [loading, setLoading] = useState(false);

  const [programmingQuestions, setProgrammingQuestions] = useState([]);
  const [mcqFile, setMcqFile] = useState(null);
  const [programmingFile, setProgrammingFile] = useState(null);

  const handleMcqFileChange = (e) => {
    setMcqFile(e.target.files[0]);
  };

  const handleProgrammingFileChange = (e) => {
    setProgrammingFile(e.target.files[0]);
  };

  const handleMcqUpload = async () => {
    if (!mcqFile) {
      alert("Please select a mcq data file first");
      return;
    }

    const formData = new FormData();
    formData.append("file", mcqFile);

    try {
      const response = await axiosInstance.post(
        `/mcqQuestion/import-McqQuestions?examId=${examId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert(response.data);
      window.location.reload();
    } catch (error) {
      alert("Error uploading file: " + error.response?.data || error.message);
    }
  };

  //Upload programming xcel file
  const handleProgrammingUpload = async () => {
    if (!programmingFile) {
      alert("Please select a programming data file first");
      return;
    }

    const formData = new FormData();
    formData.append("file", programmingFile);

    try {
      const response = await axiosInstance.post(
        `/programmingQuestion/import-ProgrammingQuestions?examId=${examId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${token}`,
          },
        }
      );
      window.location.reload();
      alert(response.data);
    } catch (error) {
      alert("Error uploading file: " + error.response?.data || error.message);
    }
  };

  //Fetch pre existing questions in exam
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const [technicalRes, aptitudeRes, programmingRes] = await Promise.all([
          axiosInstance.get(`/mcqQuestion/getMcqTechnical?examId=${examId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axiosInstance.get(`/mcqQuestion/getMcqAptitude?examId=${examId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axiosInstance.get(
            `/programmingQuestion/getAllProgrammingQuestions?examId=${examId}`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          ),
        ]);
        console.log(technicalRes.data);
        console.log(aptitudeRes.data);
        console.log(programmingRes.data);
        if (technicalRes.status === 200) setTechnicalMcqs(technicalRes.data);
        if (aptitudeRes.status === 200) setAptitudeMcqs(aptitudeRes.data);
        if (programmingRes.status === 200)
          setProgrammingQuestions(programmingRes.data);
      } catch (error) {
        console.error(
          "Error fetching data:",
          error.response?.data || error.message
        );
        alert("Failed to fetch data. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [examId, token]);

  //State management
  const handleDeleteTechnicalMcq = (mcqId) => {
    setTechnicalMcqs((prev) => prev.filter((mcq) => mcq.mcqId !== mcqId));
  };

  const handleDeleteAptitudeMcq = (mcqId) => {
    setAptitudeMcqs((prev) => prev.filter((mcq) => mcq.mcqId !== mcqId));
  };

  const handleDeleteProgQuestion = (programmingQuestionId) => {
    setProgrammingQuestions((prev) =>
      prev.filter((pq) => pq.programmingQuestionId !== programmingQuestionId)
    );
  };

  const handleMcqUpdate = (updatedMcq) => {
    setTechnicalMcqs((prevMcqs) =>
      prevMcqs.map((mcq) => (mcq.mcqId === updatedMcq.mcqId ? updatedMcq : mcq))
    );

    setAptitudeMcqs((prevMcqs) =>
      prevMcqs.map((mcq) => (mcq.mcqId === updatedMcq.mcqId ? updatedMcq : mcq))
    );
  };

  const handleProgUpdate = (programmingQuestion) => {
    setProgrammingQuestions((prevProg) =>
      prevProg.map((progQuestion) =>
        progQuestion.programmingQuestionId ===
        programmingQuestion.programmingQuestionId
          ? programmingQuestion
          : progQuestion
      )
    );
  };

  return (
    <div>
      {/*This will contain exam details, all mcqs and programming, they can update mcq and programming question optiion will be provided in a single page only */}
      {/*They will also be able to add mcq and programming */}
      <Navigationbar />
      <div className="flex flex-row justify-center">
        <Link to={`/addMcqQuestion/${examId}`} className="mx-2 my-2 py-2">
          <Button>Add new MCQ Question</Button>
        </Link>
        <Link to={`/addProgQuestion/${examId}`} className="mx-2 my-2 py-2">
          <Button>Add new Programming Question</Button>
        </Link>
        <Link to={`/mcqQuestionPool/${examId}`} className="mx-2 my-2 py-2">
          <Button>Add MCQ Question from pool</Button>
        </Link>
        <Link
          to={`/programmingQuestionPool/${examId}`}
          className="mx-2 my-2 py-2"
        >
          <Button>Add Programming Question from pool</Button>
        </Link>
        <div className="flex mx-4">
          <input type="file" onChange={handleMcqFileChange} className="mt-2" />
          <Button
            variant="dark"
            onClick={handleMcqUpload}
            className="mb-2 mt-2 px-4"
          >
            Import Mcq Questions
          </Button>
        </div>
        <div className="flex mx-4">
          <input
            type="file"
            onChange={handleProgrammingFileChange}
            className="mt-2"
          />
          <Button
            variant="dark"
            onClick={handleProgrammingUpload}
            className="mb-2 mt-2 px-4"
          >
            Import Programming Questions
          </Button>
        </div>
      </div>

      <div className="bg-yellow-100 border-1 px-4 py-2">
        Technical Questions
      </div>
      <div className="py-2">
        {loading ? (
          <p>Loading...</p>
        ) : technicalMcqs.length === 0 ? (
          <p>You have no technical mcqs</p>
        ) : (
          technicalMcqs.map((m) => (
            <Mcq
              key={m.mcqId}
              mcq={m}
              onDelete={handleDeleteTechnicalMcq}
              onUpdate={handleMcqUpdate}
            />
          ))
        )}
      </div>
      <div className="bg-yellow-100 border-1 px-4 py-2">Aptitude Questions</div>
      <div className="py-2">
        {loading ? (
          <p>Loading...</p>
        ) : aptitudeMcqs.length === 0 ? (
          <p>You have no aptitude mcqs</p>
        ) : (
          aptitudeMcqs.map((m) => (
            <Mcq
              key={m.mcqId}
              mcq={m}
              onDelete={handleDeleteAptitudeMcq}
              onUpdate={handleProgUpdate}
            />
          ))
        )}
      </div>
      <div className="bg-yellow-100 border-1 px-4 py-2">
        Programming Questions
      </div>
      <div>
        {loading ? (
          <p>Loading...</p>
        ) : programmingQuestions.length === 0 ? (
          <p>You have no programming questions</p>
        ) : (
          programmingQuestions.map((q) => (
            <ProgrammingQuestion
              key={q.programmingQuestionId}
              pq={q}
              onDelete={handleDeleteProgQuestion}
            />
          ))
        )}
      </div>
    </div>
  );
};

export default ExamQuestions;
