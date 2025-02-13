import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Button from "react-bootstrap/Button";
import axiosInstance from "../../../utils/axiosInstance";
import Examinee from "./Examinee";
import Navigationbar from "../Navigationbar";

const ManageExaminee = () => {
  let navigate = useNavigate();
  const [examinees, setExaminees] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [file, setFile] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) {
    alert("You are not authorized please login again");
    navigate("/examiner-login");
  }
  //Upload examinees
  const handleUpload = async () => {
    if (!file) {
      alert("Please select a file first");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axiosInstance.post(
        "/examiner/import-examinees",
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

  const handleDeleteExaminee = (examineeId) => {
    setExaminees((prev) =>
      prev.filter((examinee) => examinee.examineeId !== examineeId)
    );
  };

  const handleUpdateExaminee = (updatedExaminee) => {
    setExaminees((prevExaminees) =>
      prevExaminees.map((examinee) =>
        examinee.examineeId === updatedExaminee.examineeId
          ? updatedExaminee
          : examinee
      )
    );
  };

  //Search query
  useEffect(() => {
    console.log("Updated examinees:", examinees);
  }, [examinees]);

  const filteredExaminees = examinees.filter(
    (examinee) =>
      examinee.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
      examinee.degree.toLowerCase().includes(searchQuery.toLowerCase()) ||
      examinee.college.toLowerCase().includes(searchQuery.toLowerCase()) ||
      String(examinee.year).includes(searchQuery)
  );
  return (
    <>
      <Navigationbar />
      <div className="flex flex-col justify-center items-center my-4">
        <h1 className="text-2xl font-bold text-center mb-6">Examinees</h1>
        <div className="flex flex-row my-2 mb-4 ">
          <Link to="/add-examinee">
            <Button variant="primary">Add Examinee</Button>
          </Link>
          <input
            type="text"
            placeholder="Search examinees..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="mx-2 px-2 rounded-md border-2"
          />

          <div className="flex mx-4">
            <input type="file" onChange={handleFileChange} className="mt-2" />
            <Button variant="dark" onClick={handleUpload}>
              Import Examinees
            </Button>
          </div>
        </div>
      </div>
      <div className="">
        {loading ? (
          <p>Loading...</p>
        ) : filteredExaminees.length === 0 ? (
          <p>You have no examinees</p>
        ) : (
          filteredExaminees.map((e) => (
            <Examinee
              key={e.examineeId}
              temp_examinee={e}
              onUpdate={handleUpdateExaminee}
              onDelete={handleDeleteExaminee}
            />
          ))
        )}
      </div>
    </>
  );
};

export default ManageExaminee;
