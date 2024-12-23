import React, { useState } from "react";
import { Button } from "react-bootstrap";
import axiosInstance from "../../utils/axiosInstance";
import { Link, useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import InputGroup from "react-bootstrap/InputGroup";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { TimePicker } from "@mui/x-date-pickers/TimePicker";

const CreateExam = () => {
  const [data, setData] = useState({});
  const [assignToAllExaminee, setAssignToAllExaminee] = useState(false);
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
          assignToAllExaminee,
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
    setAssignToAllExaminee(e.target.checked);
  };

  return (
    <>
      <Button variant="secondary">
        <Link to="/examiner-dashboard">Back</Link>
      </Button>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
        <h2 className="text-center text-2xl font-bold">Add Exam</h2>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <form className="space-y-6" onSubmit={handleAdd}>
            <div>
              <label>Date</label>
              <DatePicker
                label="Pick a date"
                value={data.date}
                onChange={(value) => onChange("date", value)}
              />
            </div>
            <div>
              <label>Start Time</label>
              <TimePicker
                label="Select start time"
                value={data.startTime}
                onChange={(value) => onChange("startTime", value)}
              />
            </div>
            <div>
              <label>End Time</label>
              <TimePicker
                label="Select end time"
                value={data.endTime}
                onChange={(value) => onChange("endTime", value)}
              />
            </div>
            <div>
              <label>MCQ Passing Score</label>
              <input
                type="number"
                value={data.mcqPassingScore}
                onChange={(e) => onChange("mcqPassingScore", e.target.value)}
              />
            </div>
            <div>
              <label>Duration</label>
              <input
                type="number"
                value={data.duration}
                onChange={(e) => onChange("duration", e.target.value)}
              />
            </div>
            <div>
              <InputGroup>
                <InputGroup.Checkbox
                  checked={assignToAllExaminee}
                  onChange={handleCheckboxChange}
                />
                Assign to all Examinees
              </InputGroup>
            </div>
            <button type="submit">Add Exam</button>
          </form>
        </LocalizationProvider>
      </div>
    </>
  );
};

export default CreateExam;
