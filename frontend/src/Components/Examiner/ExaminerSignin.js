import React, {useState} from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axiosInstance from '../../utils/axiosInstance'
import EditIcon from '@mui/icons-material/Edit';


const ExaminerSignin = () => {

  const[credentials, setCredentials]=useState({email:'', password:''})
  let navigate=useNavigate()
  const onChange=(e)=>{
    setCredentials({...credentials, [e.target.name]:e.target.value})
  }
  const handleSignin=async(e) => {
       e.preventDefault()
       

       const{email, password}=credentials;
       console.log(email, password);

       try{
        const response=await axiosInstance.post('/examiner/signin',{
          email,
          password,
        })

        
         if(response.status===201){
          console.log("User signed in: ", response.data)
          navigate("/examiner-login")
        }
        
      }
       catch(error)
       {
         if (error.response){
          console.error('Sign in failed', error.response?.data || error.message);
          alert('Examiner already exists. Redirecting to login.');
          navigate('/examiner-login');
         }
        
       }

  }
  return (
    <>
    <div className="flex flex-1 flex-col justify-center px-6 py-12 lg:px-8 bg-black min-h-screen">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm flex flex-col items-center">
         <EditIcon className='text-white text-lg '/>
          <h2 className="mt-10 text-center text-2xl/9 font-bold tracking-tight text-white font-serif">
            Sign in to your account
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form action="#" method="POST" className="space-y-6" onSubmit={handleSignin}>
            <div>
              <label htmlFor="email" className="block text-sm/6 font-medium text-white font-serif">
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
                  className="block w-full rounded-md border-0 py-1.5 px-2 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label htmlFor="password" className="block text-sm/6 font-medium text-white font-serif">
                  Password
                </label>
                <div className="text-sm">
                  <Link to="/" className="font-semibold text-indigo-600 hover:text-indigo-500">
                    Forgot password?
                  </Link>
                </div>
              </div>
              <div className="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  required
                  autoComplete="current-password"
                  value={credentials.password}
                  onChange={onChange}
                  className="block w-full rounded-md px-2 border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm/6"
                />
              </div>
            </div>

            <div>
              <button 
                type="submit"
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Sign in
              </button>
            </div>
          </form>

          <p className="mt-10 text-center text-sm/6 text-gray-500">
            Already signed in?{' '}
            <Link to="/examiner-login" className="font-semibold text-indigo-600 hover:text-indigo-500">
              Login
            </Link>
          </p>
        </div>
      </div>
    </>
  )
}

export default ExaminerSignin
