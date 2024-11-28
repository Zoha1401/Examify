
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import LandingPage from './Components/LandingPage/LandingPage'
import ExaminerLogin from './Components/ExaminerLogin/ExaminerLogin'
import ExamineeLogin from './Components/ExamineeLogin/ExamineeLogin'
import ExamineeDashboard from './Components/ExamineeDashBoard/ExamineeDashBoard'
import ExaminerDashBoard from './Components/ExaminerDashBoard/ExaminerDashBoard';
import ExaminerSignin from './Components/ExaminerSignin/ExaminerSignin';
import ManageExaminee from './Components/ManageExaminee/ManageExaminee';
import AddExaminee from './Components/ManageExaminee/AddExaminee';

function App() {
  const router=createBrowserRouter([
    {
      path:'/',
      element:<LandingPage/>
    },
    {
      path:'/examinersignin',
      element:<ExaminerSignin/>
    },
    {
      path:'/examinerlogin',
      element:<ExaminerLogin/>
    },
    {
      path:'/examineelogin',
      element:<ExamineeLogin/>
    },
    {
      path:'/examinerdashboard',
      element:<ExaminerDashBoard/>
    },
    {
      path:'/examineedashboard',
      element:<ExamineeDashboard/>
    },
    {
      path:'/manageexaminee',
      element:<ManageExaminee/>
    },
    {
      path:'/addexaminee',
      element:<AddExaminee/>
    }

  ])
  return (
    <>
    <RouterProvider router={router}/>
    </>
  );
}

export default App;
