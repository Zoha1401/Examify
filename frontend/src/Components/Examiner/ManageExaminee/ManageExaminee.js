import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "react-bootstrap/Button";
import axiosInstance from "../../../utils/axiosInstance";
import Examinee from "./Examinee";

const ManageExaminee = () => {
  let navigate = useNavigate();
  const [examinees, setExaminees] = useState([]);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const fetchAllExaminees = async () => {
      //Getting the tokenn for authorization
      setLoading(true);
      try {
        const token = localStorage.getItem("token");
        console.log(token);
        if (!token) {
          alert("You are not authorized please login again");
          navigate("/examiner-login");
        }

        const response = await axiosInstance("/examiner/getAllExaminee", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        console.log(response);
        console.log(Array.isArray(response.data)); // Should return true

        if (response.status === 200) {
          console.log("Response data:", response.data);
          setExaminees(response.data); // Update state with response data
        } else {
          console.error("Failed to fetch all examinees.");
        }
      } catch (error) {
        console.error(
          "Error fetching examinees:",
          error.response?.data || error.message
        );
        alert("Failed to fetch examinees. Please try again.");
      } finally {
        setLoading(false);
      }
    };
    fetchAllExaminees();
  }, []);

  useEffect(() => {
    console.log("Updated examinees:", examinees);
  }, [examinees]); // This will log the updated state whenever it changes

  return (
    <>
    <div className="flex flex-col justify-center items-center my-4">
      <h1 className="text-2xl font-bold text-center mb-6">Examinees</h1>
      <div className="flex flex-row my-2 mb-4 px-2">
      <Link to="/add-examinee">
        <Button variant="primary">Add Examinee</Button>
      </Link>
      </div>
    </div>   
      <div className="">
        {loading ? (
          <p>Loading...</p>
        ) : examinees.length === 0 ? (
          <p>You have no examinees</p>
        ) : (
          examinees.map((e) => (
            <Examinee key={e.examineeId} temp_examinee={e} />
          ))
        )}
      </div>
    </>
  );
};

export default ManageExaminee;
