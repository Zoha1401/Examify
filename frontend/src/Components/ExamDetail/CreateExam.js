import React, { useState } from "react";
import { Button } from "react-bootstrap";
import axiosInstance from "../../utils/axiosInstance";
import { Link, useNavigate } from "react-router-dom";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";

const CreateExam = () => {
  const [data, setData] = useState({});
  const [assignToAllExaminee, setAssignToAllExaminee]=useState(false);
  let navigate = useNavigate();

  const onChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
  };

  const handleAdd = async (e) => {
    e.preventDefault();

    const { startTime, endTime, mcqPassingScore, duration } = data;
    console.log(startTime, duration, mcqPassingScore);
    try {
      const token = localStorage.getItem("token");
      console.log(token);
      if (!token) {
        alert("You are not authorized please login again");
        navigate("/examiner-login");
      }

      const response = await axiosInstance.post(
        "/exam/createExam",
        {
          startTime,
          endTime,
          duration,
          mcqPassingScore,
          assignToAllExaminee
        },

        {
          headers: {
            Authorization: `Bearer ${token}`, // Attach Bearer token here
          },
        }
      );

      if (response.status === 200) {
        alert("Exam is successfully created");
        navigate("/examiner-dashboard");
      }
    } catch (error) {
      console.error(
        "Error adding exam:",
        error.response?.data || error.message
      );
      alert("Failed to add exam. Please try again.");
    }
  };

  const handleCheckboxChange=(e)=>{
     setAssignToAllExaminee(e.target.checked);
  }

  return (
    <>
      <Button variant="secondary">
        <Link to="/examiner-dashboard">Back Icon</Link>
      </Button>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
            Add Exam
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form
            action="#"
            method="POST"
            className="space-y-6"
            onSubmit={handleAdd}
          >
            <div>
              <label
                htmlFor="startTime"
                className="block text-sm/6 font-medium text-gray-900"
              >
                Start Time
              </label>
              <div className="mt-2">
                <input
                  id="startTime"
                  name="startTime"
                  type="startTime"
                  required
                  autoComplete="startTime"
                  value={data.startTime}
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="endTime"
                  className="block text-sm/6 font-medium text-gray-900"
                >
                  End Time
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="endTime"
                  name="endTime"
                  required
                  autoComplete="current-password"
                  value={data.endTime}
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="mcqPassingScore"
                  className="block text-sm/6 font-medium text-gray-900"
                >
                  MCQ Passing Score
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="mcqPassingScore"
                  name="mcqPassingScore"
                  required
                  autoComplete="current-password"
                  value={data.mcqPassingScore}
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="duration"
                  className="block text-sm/6 font-medium text-gray-900"
                >
                  Duration
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="duration"
                  name="duration"
                  required
                  autoComplete="current-password"
                  value={data.duration}
                  onChange={onChange}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <InputGroup className="mb-3">
                <InputGroup.Checkbox aria-label="Checkbox for following text input" 
                checked={assignToAllExaminee}
                onChange={handleCheckboxChange} />
                Assign to all Examinees
                <Form.Control aria-label="Assign to all examinees" className="mx-2" /> 
              </InputGroup>
            </div>

            <div>
              <button
                type="submit"
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Add Examinee
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default CreateExam;
