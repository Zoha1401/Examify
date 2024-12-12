import React, {useState} from "react";
import DeleteIcon from "@mui/icons-material/Delete";
import ModeEditOutlineIcon from "@mui/icons-material/ModeEditOutline";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../utils/axiosInstance";

const Mcq = ({ mcq, onDelete }) => {
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
      const response=await axiosInstance.post(`/mcqQuestion/updateMcqQuestion?mcqId=${editableMcq.mcqId}&examId=${examId}`,
         { editableMcq,
          ...options,},

          {
              headers:{
                  Authorization:`Bearer ${token}`
              }
          }
      )
      if(response.status===200)
      {
          alert("Mcq updated")
          window.location.reload();
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
      <div className="flex">
        <div>{mcq.mcqQuestionText}</div>
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
          {editableMcq && (
            <form onSubmit={handleUpdateMcq}>
              <input
                type="text"
                name="mcqQuestionText"
                value={editableMcq.mcqQuestionText || ""}
                onChange={onChange}
              ></input>
              <div className="flex">
                {options.map((option, index) => (
                  <div key={option.optionId || index}>
                    {option.optionText}{" "}
                    {option.isCorrect && (
                      <strong classname="font-light bg-green-200">
                        (Correct)
                      </strong>
                    )}
                    <input
                      type="text"
                      name="optionText"
                      value={option.optionText || ""}
                      onChange={(e) =>
                        onChangeOption(index, "optionText", e.target.value)
                      }
                    ></input>
                    <input
                      type="checkbox"
                      checked={option.isCorrect || false}
                      onChange={(e) =>
                        onChangeOption(index, "isCorrect", e.target.checked)
                      }
                    />
                  </div>
                ))}
              </div>
              <button type="submit">Update MCQ</button>
            </form>
          )}
          <DeleteIcon onClick={handleDeleteMcq} />
        </div>
      </div>
    </>
  );
};

export default Mcq;
