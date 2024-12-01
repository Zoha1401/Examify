
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
import CreateExam from './Components/ExamDetail/CreateExam';
import ExamQuestions from './Components/ExamDetail/ExamQuestions';

function App() {
  const router=createBrowserRouter([
    {
      path:'/',
      element:<LandingPage/>
    },
    {
      path:'/examiner-signin',
      element:<ExaminerSignin/>
    },
    {
      path:'/examiner-login',
      element:<ExaminerLogin/>
    },
    {
      path:'/examinee-login',
      element:<ExamineeLogin/>
    },
    {
      path:'/examiner-dashboard',
      element:<ExaminerDashBoard/>
    },
    {
      path:'/examinee-dashboard',
      element:<ExamineeDashboard/>
    },
    {
      path:'/manage-examinee',
      element:<ManageExaminee/>
    },
    {
      path:'/add-examinee',
      element:<AddExaminee/>
    },
    {
      path:'/create-exam',
      element:<CreateExam/>
    },
    {
      path:'/exam-detail/:examId',
      element:<ExamQuestions/>
    }

  ])
  return (
    <>
    <RouterProvider router={router}/>
    </>
  );
}

export default App;
