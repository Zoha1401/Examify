import React, { useState, useEffect } from "react";
import axiosInstance from "../../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";

const Examinee = ({ temp_examinee, onUpdate, onDelete }) => {
  const [editableExaminee, setEditableExaminee] = useState(null);
  const [examinee, setExaminee] = useState({});
  let navigate = useNavigate();
  console.log(temp_examinee);
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }
  //Fetch examinee
  useEffect(() => {
    const fetchExaminee = async () => {
      try {
        const response = await axiosInstance.get(
          `/examiner/getExamineeById?examineeId=${temp_examinee.examineeId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        console.log(response.data);
        setExaminee(response.data);
        // setExaminee(examineeData);
      } catch (error) {
        console.error("Error fetching examinee", error.message);
      }
    };
    fetchExaminee();
  }, [examinee.examineeId]);

  const handleDelete = async () => {
    try {
      const response = await axiosInstance.delete(
        `/examiner/deleteExaminee?examineeId=${examinee.examineeId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Examinee deleted");
      onDelete(temp_examinee.examineeId);
    } catch (error) {
      console.error(
        "Error deleting examinee:",
        error.response?.data || error.message
      );
      alert("Error deleting examinee. Please try again.");
    }
  };

  //Set if want to edit
  const handleEdit = () => {
    setEditableExaminee(examinee);
  };

  const handleUpdate = async () => {
    try {
      const response = await axiosInstance.post(
        `/examiner/updateExaminee?examineeId=${editableExaminee.examineeId}`,
        editableExaminee,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      alert("Examinee updated");
      onUpdate(response.data);
    } catch (error) {
      console.error(
        "Error updating examinee:",
        error.response?.data || error.message
      );
      alert("Error updating examinee. Please try again.");
    }
  };

  const onChange = (e) => {
    setEditableExaminee({
      ...editableExaminee,
      [e.target.name]: e.target.value,
    });
  };
  return (
    <>
      <div className="flex flex-row mb-3 mx-2 mt-2 my-2 border-1 px-2 py-2 rounded-lg justify-between">
        <div className="flex flex-row">
          {" "}
          <h4 className="mx-2">{examinee.email}</h4>
          <h4 className="mx-2">{examinee.degree}</h4>
          <h4 className="mx-2">{examinee.college}</h4>
          <h4 className="mx-4">{examinee.year}</h4>
          <h4 className="mx-2">{examinee.phoneNumber}</h4>
        </div>

        <div className="flex justify-content-end">
          <Button onClick={handleEdit} className="mx-2" size="sm">
            Update
          </Button>
          <Button onClick={handleDelete} variant="danger" size="sm">
            Delete
          </Button>
        </div>
      </div>

      {editableExaminee && (
        <form onSubmit={handleUpdate} className="flex">
          <input
            type="email"
            name="email"
            value={editableExaminee.email}
            onChange={onChange}
            className="rounded-lg border-1 mx-2 px-2"
          ></input>
          <input
            type="college"
            name="college"
            value={editableExaminee.college}
            onChange={onChange}
            className="rounded-lg border-1 mx-1 px-2"
          ></input>
          <input
            type="degree"
            name="degree"
            value={editableExaminee.degree}
            onChange={onChange}
            className="rounded-lg border-1 mx-1 px-2"
          ></input>
          <input
            type="year"
            name="year"
            value={editableExaminee.year}
            onChange={onChange}
            className="rounded-lg border-1 mx-2 px-2"
          ></input>
          <input
            type="phoneNumber"
            name="phoneNumber"
            value={editableExaminee.phoneNumber}
            onChange={onChange}
            className="rounded-lg border-1 mx-2 px-2"
          ></input>

          <Button type="submit" className="mx-1" variant="success" size="sm">
            Save
          </Button>
          <Button
            onClick={() => setEditableExaminee(null)}
            className="mx-1"
            variant="warning"
          >
            Cancel
          </Button>
        </form>
      )}
    </>
  );
};

export default Examinee;
