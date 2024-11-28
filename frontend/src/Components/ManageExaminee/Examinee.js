import React, {useState, useEffect} from 'react';
import axiosInstance from '../../utils/axiosInstance';
import {useNavigate} from 'react-router-dom'
import { Button } from 'react-bootstrap';

const Examinee = ({temp_examinee}) => {
    const[examinee, setExaminee]=useState({})
    let navigate=useNavigate();
    console.log(temp_examinee)
    const token=localStorage.getItem('token')
                console.log(token)
                if(!token){
                    alert("You are not authorized please login again")
                    navigate("/examinerlogin")
                }
    useEffect(() => {
        const fetchExaminee=async()=>{
            try{
                const response=await axiosInstance.get(`/examiner/getExamineeById?examineeId=${temp_examinee.examineeId}`,
                    {
                        headers:{
                            Authorization:`Bearer ${token}`,
                          },
                    }
                )
                console.log(response.data)
                setExaminee(response.data)
                // setExaminee(examineeData);
            }
            catch(error)
            {
               console.error("Error fetching examinee", error.message);
            }
        }
        fetchExaminee();
    }, [examinee.examineeId]);

    const handleDelete=async()=>{
        try{
        const response=await axiosInstance.delete(`/examiner/deleteExaminee?examineeId=${examinee.examineeId}`,
            {
                headers:{
                    Authorization:`Bearer ${token}`
                }
            }
        )
        if(response.status===200)
        {
            alert("Examinee deleted")
            window.location.reload();
        }
    }
    catch(error)
    {
        console.error("Error deleting examinee:", error.response?.data || error.message);
        alert("Error deleting examinee. Please try again.");
    }
    }
  return (
    <div>
      <h1>{examinee.email}</h1>
      <h1>{examinee.degree}</h1> 
      <h1>{examinee.college}</h1>
      <Button>Update</Button>
      <Button onClick={handleDelete}>Delete</Button>
    </div>
  );
}

export default Examinee;
