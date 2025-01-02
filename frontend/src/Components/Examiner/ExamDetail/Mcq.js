import React, {useState} from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import ModeEditOutlineIcon from "@mui/icons-material/ModeEditOutline";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import { Button } from "react-bootstrap";

const Mcq = ({ mcq, onDelete, onUpdate }) => {
  const { examId } = useParams();
  const [editableMcq, setEditableMcq] = useState(null);
  const [options, setOptions]=useState([]);

  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  const handleUpdateMcq = async () => {
    try{

      const payload = {
        ...editableMcq, 
        options,        
    };

      const response=await axiosInstance.post(`/mcqQuestion/updateMcqQuestion?mcqId=${editableMcq.mcqId}&examId=${examId}`,
        {
          ...payload,
        },

          {
              headers:{
                  Authorization:`Bearer ${token}`
              }
          }
      )
      if(response.status===200)
      {
          alert("Mcq updated")
          onUpdate(response.data)
          setEditableMcq(null); 
      }
  }
  catch(error)
  {
      console.error("Error updating mcq:", error.response?.data || error.message);
      alert("Error updating mcq. Please try again.");
  }
  };

  const handleDeleteMcq = async () => {
    try {
      const response = await axiosInstance.delete(
        `/mcqQuestion/deleteMcqQuestion?mcqId=${mcq.mcqId}&examId=${examId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (response.status === 200) {
        alert("Mcq is deleted");
        onDelete(mcq.mcqId);
      }
    } catch (error) {
      console.error("Error deleting:", error.response?.data || error.message);
      alert("Failed to delete mcq. Please try again.");
    }
  };

  const onChange = (e) => {
    setEditableMcq({ ...editableMcq, [e.target.name]: e.target.value });
  };

  const handleEdit = () => {
    setEditableMcq(mcq);
    setOptions(mcq.options || []);
  };

 
  const onChangeOption = (index, field, value) => {
    const updatedOptions = options.map((option, i) =>
      i === index ? { ...option, [field]: value } : option
    );

    if (field === "isCorrect" && value) {
      setEditableMcq({ ...editableMcq, correctAnswer: options[index].optionText });
    } else {
      setEditableMcq({ ...editableMcq, correctAnswer: "" });
    }
    setOptions(updatedOptions);
  };

  return (
    <>
      <div className="flex-col px-2 border-1 py-2 mx-2 mb-2 bg-gray-50 rounded-lg shadow-md">
        <div className="font-semibold mb-3 mx-2">{mcq.mcqQuestionText}</div>
        <div className="flex">
          {mcq.options.map((option, index) => (
            <div key={option.optionId || index} className="mx-2">
              {option.optionText}{" "}
              {option.isCorrect && (
                <strong className="font-light bg-green-200">(Correct)</strong>
              )}
            </div>
          ))}
          <ModeEditOutlineIcon onClick={handleEdit} />
          <DeleteIcon onClick={handleDeleteMcq} />
          </div>
          {editableMcq && (
            <form onSubmit={handleUpdateMcq} className="mt-4 p-2 rounded-md bg-gray-100">
              <input
                type="text"
                name="mcqQuestionText"
                value={editableMcq.mcqQuestionText || ""}
                onChange={onChange}
                className="rounded-lg p-1 w-full"
              ></input>
              <div className="flex mt-2">
                {options.map((option, index) => (
                  <div key={option.optionId || index} className="p-2">
                    <input
                      type="checkbox"
                      checked={option.isCorrect || false}
                      onChange={(e) =>
                        onChangeOption(index, "isCorrect", e.target.checked)
                      }
                      className="mx-2"
                    />
                    <input
                      type="text"
                      name="optionText"
                      value={option.optionText || ""}
                      onChange={(e) =>
                        onChangeOption(index, "optionText", e.target.value)
                      }
                      className="rounded-lg p-1"
                    ></input>
                   
                    
                  </div>
                ))}
              </div>
              <Button type="submit" variant="primary">Update MCQ</Button>
              <Button type="cancel" onClick={()=>{setEditableMcq(null)}} variant="warning" className="mx-2">Cancel</Button>
            </form>
          )}
          
        </div>
      
    </>
  );
};

export default Mcq;


//Programming question updation to be done
//Assign exams to examinee after creation of exam. (Dropdown, fetches all examinees, Assigned->checked, Not Assigned->Unchecked, state manage)
//Assign all examinee one or more exam after exam creation (Checkbox beside every exam)
