import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../utils/axiosInstance";
import DeleteIcon from "@mui/icons-material/Delete";
import ModeEditOutlineIcon from "@mui/icons-material/ModeEditOutline";
import { Button } from "react-bootstrap";

const ProgrammingQuestion = ({ pq, onDelete, onUpdate }) => {
  const [editableProQ, setEditableProQ] = useState(null);
  const [testCases, setTestCases] = useState([]);

  const { examId } = useParams();

  let navigate = useNavigate();
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }

  const handleUpdateProQ = async () => {
    try {
      const payload = {
        ...editableProQ,
        testCases,
      };

      const response = await axiosInstance.post(
        `/programmingQuestion/updateProgrammingQuestion?examId=${examId}&proId=${editableProQ.programmingQuestionId}`,
        {
          ...payload,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Programming Question Updated");
      window.location.reload();
      setEditableProQ(null);
    } catch (error) {
      console.error(
        "Error updating pro q:",
        error.response?.data || error.message
      );
      alert("Error updating pro q. Please try again.");
    }
  };

  const handleDeleteProgrammingQuestion = async () => {
    try {
      const response = await axiosInstance.delete(
        `/programmingQuestion/deleteProgrammingQuestion?examId=${examId}&proId=${pq.programmingQuestionId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      alert("Programming Question is deleted");
      onDelete(pq.programmingQuestionId);
    } catch (error) {
      console.error("Error deleting:", error.response?.data || error.message);
      alert("Failed to delete question. Please try again.");
    }
  };

  const onChange = (e) => {
    setEditableProQ({ ...editableProQ, [e.target.name]: e.target.value });
  };

  const handleEdit = () => {
    setEditableProQ(pq);
    setTestCases(pq.testCases || []);
  };

  const handleTestCaseChange = (index, field, value) => {
    const updatedTestCases = testCases.map((testCase, i) =>
      i === index ? { ...testCase, [field]: value } : testCase
    );
    setTestCases(updatedTestCases);
  };
  return (
    <>
      <div className="flex-col bg-gray-50 p-2 py-4 mx-2 mt-3 rounded-md shadow-md">
        <div className="flex flex-row px-2 mb-2">
          <div className="text-lg font-semibold">
            {pq.programmingQuestionText}
          </div>
          <div>
            <ModeEditOutlineIcon onClick={handleEdit} />
            <DeleteIcon onClick={handleDeleteProgrammingQuestion} />
          </div>
        </div>
        <div className="flex-col">
          {pq.testCases &&
            pq.testCases.map((t) => (
              <div className="flex px-2 mx-auto px-auto mb-1">
                <span className="font-semibold">Input :</span> {t.input}
                <span className="font-semibold px-2"> Output:</span>{" "}
                {t.expectedOutput}
              </div>
            ))}
        </div>
        <div className="px-2 mt-2 font-semibold">
          Reference Answer:{pq.referenceAnswer}
        </div>

        {editableProQ && (
          <form
            onSubmit={handleUpdateProQ}
            className="mt-4 bg-gray-100 p-2 rounded-md"
          >
            <input
              type="text"
              name="programmingQuestionText"
              value={editableProQ.programmingQuestionText || ""}
              onChange={onChange}
              className="rounded-md px-2 w-full py-1"
            ></input>
            <div className="flex mt-3">
              {testCases.map((t, index) => (
                <div key={t.programmingQuestionId || index}>
                  Input:
                  <input
                    type="text"
                    name="input"
                    value={t.input || ""}
                    onChange={(e) =>
                      handleTestCaseChange(index, "input", e.target.value)
                    }
                    className="px-2 rounded-md py-1 mx-2"
                  ></input>
                  Expected Output:
                  <input
                    type="text"
                    name="expectedOutput"
                    value={t.expectedOutput || ""}
                    onChange={(e) =>
                      handleTestCaseChange(
                        index,
                        "expectedOutput",
                        e.target.value
                      )
                    }
                    className="px-2 rounded-md py-1 mx-2"
                  ></input>
                </div>
              ))}
            </div>
            <Button type="submit" variant="primary" className="mt-2">
              Update Programming Question
            </Button>
            <Button
              type="submit"
              variant="warning"
              onClick={() => {
                setEditableProQ(null);
              }}
              className="mx-2 mt-2"
            >
              Cancel
            </Button>
          </form>
        )}
      </div>
    </>
  );
};

export default ProgrammingQuestion;
