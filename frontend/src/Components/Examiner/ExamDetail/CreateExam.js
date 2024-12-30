import React, { useState } from "react";
import { Button } from "react-bootstrap";
import axiosInstance from "../../../utils/axiosInstance";
import { Link, useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import InputGroup from "react-bootstrap/InputGroup";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";
import Navigationbar from "../Navigationbar";

const CreateExam = () => {
  const [data, setData] = useState({});
  const [assignToAllExaminees, setAssignToAllExaminees] = useState(false);
  let navigate = useNavigate();

  const onChange = (key, value) => {
    setData({ ...data, [key]: value });
  };
  
  const combineAndFormatDateTime = (date, time) => {
    if (dayjs(date).isValid() && dayjs(time).isValid()) {
      const combined = dayjs(date)
        .hour(dayjs(time).hour())
        .minute(dayjs(time).minute())
        .second(0); // Set seconds to 0 if not provided
      return combined.format("YYYY-MM-DDTHH:mm:ss");
    } else {
      return "Invalid date or time";
    }
  };

  const handleAdd = async (e) => {
    e.preventDefault();

    const { date, startTime, endTime, mcqPassingScore, duration } = data;
    // console.log(startTime, duration, mcqPassingScore);
    const formattedStartTime=combineAndFormatDateTime(date, startTime)
    const formattedEndTime=combineAndFormatDateTime(date, endTime)
    console.log(formattedStartTime)
    console.log(formattedEndTime)
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
          startTime:formattedStartTime,
          endTime:formattedEndTime,
          duration,
          mcqPassingScore,
          assignToAllExaminees,
        },

        {
          headers: {
            Authorization: `Bearer ${token}`, // Attach Bearer token here
          },
        }
      );

      if (response.status === 200) {
        const examId = response.data.examId;
        alert("Exam is successfully created");
        navigate(`/exam-detail/${examId}`);
      }
    } catch (error) {
      console.error(
        "Error adding exam:",
        error.response?.data || error.message
      );
      alert("Failed to add exam. Please try again.");
    }
  };

  const handleCheckboxChange = (e) => {
    setAssignToAllExaminees(e.target.checked);
  };

  return (
    <>
    <Navigationbar/>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8 bg-gray-100">
        <h2 className="text-center text-2xl font-bold mb-8">Add Exam</h2>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <div className="mx-auto bg-white max-w-lg rounded-lg shadow-lg p-6">
          <form className="space-y-6" onSubmit={handleAdd}>
            <div>
              <label className="block text-sm font-medium">Date</label>
              <DatePicker
                label="Pick a date"
                value={data.date}
                onChange={(value) => onChange("date", value)}
                className="mt-2 w-full shadow-sm rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium">Start Time</label>
              <TimePicker
                label="Select start time"
                value={data.startTime}
                onChange={(value) => onChange("startTime", value)}
                 className="mt-2 w-full shadow-sm rounded-md"
                 required
              />
            </div>
            <div>
              <label className="block text-sm font-medium">End Time</label>
              <TimePicker
                label="Select end time"
                value={data.endTime}
                onChange={(value) => onChange("endTime", value)}
                 className="mt-2 w-full shadow-sm rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium">MCQ Passing Score</label>
              <input
                type="number"
                value={data.mcqPassingScore}
                onChange={(e) => onChange("mcqPassingScore", e.target.value)}
                 className="mt-2 w-full shadow-sm rounded-md"
              />
            </div>
            <div>
              <label className="block text-sm font-medium">Duration</label>
              <input
                type="number"
                value={data.duration}
                 className="mt-2 w-full shadow-sm rounded-md"
                onChange={(e) => onChange("duration", e.target.value)}
                required
              />
            </div>
            <div className="flex items-center">
              <InputGroup>
                <InputGroup.Checkbox
                  checked={assignToAllExaminees}
                  onChange={handleCheckboxChange}
                />
                <div className="mx-2 mt-1">Assign to all Examinees</div>
              </InputGroup>
            </div>
            <Button type="submit" variant="dark" className="justify-center items-center w-full">Add Exam</Button>
          </form>
          </div>
        </LocalizationProvider>
      </div>
    </>
  );
};

export default CreateExam;
