import React, {useState} from 'react';
import { Link, useNavigate } from 'react-router-dom'
import axiosInstance from '../../../utils/axiosInstance';
import { Button } from 'react-bootstrap';
import Navigationbar from '../Navigationbar';

const AddExaminee = () => {
    const [credentials, setCredentials]=useState({});
    let navigate=useNavigate();
    const onChange=(e)=>{
        setCredentials({...credentials, [e.target.name]:e.target.value})
    }

    const handleAdd=async(e)=>{
      e.preventDefault();

      const{email, degree, year, college, phoneNumber}=credentials;
      console.log(email, college, degree, phoneNumber);
      try{

        const token=localStorage.getItem('token')
        console.log(token)
        if(!token){
            alert("You are not authorized please login again")
            navigate("/examiner-login")
        }

      const response=await axiosInstance.post("/examiner/addExaminee", 
        {
        email,
        college,
        degree,
        year,
        phoneNumber,
        },
    
        {
            headers: {
              Authorization: `Bearer ${token}`, // Attach Bearer token here
            },
        }
    )

      if(response.status===201){
        alert("Examinee is successfully created");
        navigate("/manage-examinee")  
      }
    }
    catch(error)
    {
        console.error("Error adding examinee:", error.response?.data || error.message);
        alert("Failed to add examinee. Please try again.");
    }



      

    }
    
  return (
    <>
    <Navigationbar/>
      <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
          <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
            Add Examinee
          </h2>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form action="#" method="POST" className="space-y-6" onSubmit={handleAdd}>
            <div>
              <label htmlFor="email" className="block text-sm/6 font-medium text-gray-900">
                Email address
              </label>
              <div className="mt-2">
                <input
                  id="email"
                  name="email"
                  type="email"
                  required
                  autoComplete="email"
                  value={credentials.email}
                  onChange={onChange}
                  className="block w-full rounded-md border-1 px-2 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="college" className="block text-sm/6 font-medium text-gray-900">
                  College
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="college"
                  name="college"
                  required
                  autoComplete="current-password"
                  value={credentials.college}
                  onChange={onChange}
                  className="block w-full rounded-md px-2 border-1 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="degree" className="block text-sm/6 font-medium text-gray-900">
                  Degree
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="degree"
                  name="degree"
                  required
                  autoComplete="current-password"
                  value={credentials.degree}
                  onChange={onChange}
                  className="block w-full rounded-md px-2 border-1 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="year" className="block text-sm/6 font-medium text-gray-900">
                  Year
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="year"
                  name="year"
                  required
                  autoComplete="current-password"
                  value={credentials.year}
                  onChange={onChange}
                  className="block w-full rounded-md px-2 border-1 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="phoneNumber" className="block text-sm/6 font-medium text-gray-900">
                  Phone Number     
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="phoneNumber"
                  name="phoneNumber"
                  type='number'
                  required
                  value={credentials.phoneNumber}
                  onChange={onChange}
                  className="block w-full rounded-md px-2 border-1 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>



            <div>
              <Button 
                type="submit"
                variant='dark'
                className="flex w-full justify-center rounded-md px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
               Add Examinee
              </Button>
            </div>
          </form>

        </div>
      </div>
    </>
  );
}

export default AddExaminee;
