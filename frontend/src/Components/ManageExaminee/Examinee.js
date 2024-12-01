import React, {useState, useEffect} from 'react';
import axiosInstance from '../../utils/axiosInstance';
import {useNavigate} from 'react-router-dom'
import { Button } from 'react-bootstrap';

const Examinee = ({temp_examinee}) => {
    const [editableExaminee, setEditableExaminee]=useState(null);
    const[examinee, setExaminee]=useState({})
    let navigate=useNavigate();
    console.log(temp_examinee)
    const token=localStorage.getItem('token')
                console.log(token)
                if(!token){
                    alert("You are not authorized please login again")
                    navigate("/examiner-login")
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

    const handleEdit=()=>{
        setEditableExaminee(examinee);
    }

    const handleUpdate= async()=>{
        try{
            const response=await axiosInstance.post(`/examiner/updateExaminee?examineeId=${editableExaminee.examineeId}`,
                editableExaminee,
                {
                    headers:{
                        Authorization:`Bearer ${token}`
                    }
                }
            )
            if(response.status===200)
            {
                alert("Examinee updated")
                window.location.reload();
            }
        }
        catch(error)
        {
            console.error("Error updating examinee:", error.response?.data || error.message);
            alert("Error updating examinee. Please try again.");
        }
    }

    const onChange=(e)=>{
         setEditableExaminee({...editableExaminee, [e.target.name]:e.target.value})
    }
  return (
    <>
    <div className='flex'>
      <h1>{examinee.email}</h1>
      <h1>{examinee.degree}</h1> 
      <h1>{examinee.college}</h1>
      <Button onClick={handleEdit}>Update</Button>
      {
        editableExaminee && (
            <form onSubmit={handleUpdate}>
                <input type="email"
                name="email"
                value={editableExaminee.email}
                onChange={onChange}
                ></input>
                <input type="college"
                name="college"
                value={editableExaminee.college}
                onChange={onChange}
                ></input>
                <input type="degree"
                name="degree"
                value={editableExaminee.degree}
                onChange={onChange}
                ></input>
                <input type="year"
                name="year"
                value={editableExaminee.year}
                onChange={onChange}
                ></input>
                
                 <button type="submit">Update</button>
                 <button onClick={() => setEditableExaminee(null)}>Cancel</button>
            </form>
        )
      }
      <Button onClick={handleDelete}>Delete</Button>
    </div>
    </>
  );
}

export default Examinee;
