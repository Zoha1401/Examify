import React, {useState, useEffect} from 'react';
import axiosInstance from '../../../utils/axiosInstance';
import {useNavigate} from 'react-router-dom'
import { Button, Form, InputGroup } from 'react-bootstrap';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';

const Exam = ({ temp_exam }) => {
  const [editableExam, setEditableExam] = useState(null);
  const [examinees, setExaminees]=useState([]);
  const [checkedState, setCheckedState]=useState(false);
  const [showExaminees, setShowExaminees]=useState(false);
  const [selectedExaminees, setSelectedExaminees]=useState([])

  const toggleDropDown=()=>{
    setShowExaminees(!showExaminees);
  }

  let navigate = useNavigate();
  console.log(temp_exam);
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  useEffect(() => {
    const fetchExaminees = async () => {
      try {
        const examExamineesFetched = await axiosInstance.get(
          `/exam/getExaminees?examId=${temp_exam.examId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const response=await axiosInstance.get( `/examiner/getAllExaminee`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
        console.log(examExamineesFetched.data);
        setExaminees(response.data)


        const examExaminees = examExamineesFetched.data.map((e) => e.examineeId);
        const initialCheckedState = {};
        response.data.forEach((examinee) => {
          initialCheckedState[examinee.examineeId] = examExaminees.includes(examinee.examineeId);
          console.log("examinee Id ", examinee.examineeId, "Initial state ", initialCheckedState)
        });
        
        setCheckedState(initialCheckedState);
        
        // setExam(examData);
      } catch (error) {
        console.error("Error fetching examinees", error.message);
      }
    };
    fetchExaminees();
  }, [temp_exam.examId]);
 
  console.log("Tempp exam", temp_exam.startTime)
  const handleDelete = async () => {
    try {
      const response = await axiosInstance.delete(
        `/exam/deleteExam?examId=${temp_exam.examId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        alert("Exam deleted");
        window.location.reload();
      }
    } catch (error) {
      console.error(
        "Error deleting exam:",
        error.response?.data || error.message
      );
      alert("Error deleting exam. Please try again.");
    }
  };

  const handleEdit = () => {
    setEditableExam(temp_exam);
  };

  const handleUpdate = async () => {
    try {
      const response = await axiosInstance.post(
        `/exam/updateExam?examId=${editableExam.examId}`,
        editableExam,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.status === 200) {
        alert("Exam updated");
        window.location.reload();
      }
    } catch (error) {
      console.error(
        "Error updating exam:",
        error.response?.data || error.message
      );
      alert("Error updating exam. Please try again.");
    }
  };

  const onChange = (e) => {
    setEditableExam({
      ...editableExam,
      [e.target.name]: e.target.value,
    });
  };

  const viewQuestions=()=>{
    navigate(`/exam-detail/${temp_exam.examId}`);
  }

  const viewAnswers=()=>{
     navigate(`/examAnswers/${temp_exam.examId}`);
  }
  const extractDateTime = (datetime) => {
    const [date, time_temp] = datetime.split("T");
    const time=time_temp.slice(0,5)
    return { date, time};
  };

  const { date: date1, time: time1 } = extractDateTime(temp_exam.startTime);
  const { date: date2, time: time2 } = extractDateTime(temp_exam.endTime);

  const handleCheckboxChange = (id) => {
    setCheckedState((prevState) => {
      const updatedState = { ...prevState, [id]: !prevState[id] };
      updateSelectedExaminees(updatedState);
      return updatedState;
    });
  };

  const updateSelectedExaminees = (updatedState) => {
    const selectedExaminees = Object.keys(updatedState).filter(
      (id) => updatedState[id]
    );
    setSelectedExaminees(selectedExaminees);
  };

  const handleSearch=()=>{

  }

  return (
    <div className="flex flex-row mb-3 mx-2 mt-2 my-2 border-1 px-2 py-2 rounded-lg">
      <h4 className='mx-2'>{temp_exam.examId}</h4>
      <h4 className='mx-2'>{date1}</h4>
      <h4 className='mx-2'>{time1}</h4>
      <h4 className='mx-2'>{time2}</h4>
      <h4 className='mx-2'>{temp_exam.mcqPassingScore}</h4>
      <h4 className='mx-2'>{temp_exam.duration} min</h4> 
      <div className="flex mx-2">
      <Button onClick={handleDelete} variant="danger" className="mx-2">Delete</Button>
      <Button onClick={handleEdit} variant="primary" className="mx-2">Update</Button>
      <Button onClick={viewQuestions} variant="dark" className="mx-2">View Questions</Button>
      <Button onClick={viewAnswers} variant="dark" className="mx-2">View Answers</Button>
      
      </div>
      <div>
      <KeyboardArrowDownIcon onClick={toggleDropDown}/>
      </div>
      
      {
        editableExam && (
            <form onSubmit={handleUpdate}>
                <input type="startTime"
                name="startTime"
                value={editableExam.startTime}
                onChange={onChange}
                ></input>
                <input type="endTime"
                name="endTime"
                value={editableExam.endTime}
                onChange={onChange}
                ></input>
                <input type="mcqPassingScore"
                name="mcqPassingScore"
                value={editableExam.mcqPassingScore}
                onChange={onChange}
                ></input>
                <input type="duration"
                name="duration"
                value={editableExam.duration}
                onChange={onChange}
                ></input>
                
                 <button type="submit">Update</button>
                 <button onClick={() => setEditableExam(null)}>Cancel</button>
                 
            </form>
        )
      }

{showExaminees && (
        <div className="mt-3 p-3 border rounded bg-light shadow-sm">
          <h5 className="mb-3">Examinees</h5>
          <InputGroup className="mb-3">
            <Form.Control
              type="text"
              placeholder="Search examinees..."
              onChange={handleSearch}
              value=""
            />
          </InputGroup>
          <div className="max-h-64 overflow-y-auto">
            {examinees.map((examinee) => (
              <div
                key={examinee.examineeId}
                className="flex items-center justify-between mb-2"
              >
                <span>{examinee.email}</span>
                <span>{examinee.degree}</span>
                <span>{examinee.college}</span>
                <span>{examinee.year}</span>
                <Form.Check
                  type="checkbox"
                  checked={checkedState[examinee.examineeId || false]}
                  onChange={() => handleCheckboxChange(examinee.examineeId)}
                />
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default Exam;
