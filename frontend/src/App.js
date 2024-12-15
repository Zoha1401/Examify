
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import LandingPage from './Components/LandingPage/LandingPage'
import ExaminerLogin from './Components/ExaminerLogin/ExaminerLogin'
import ExamineeLogin from './Components/Examinee/ExamineeLogin'
import ExamineeDashboard from './Components/Examinee/ExamineeDashBoard'
import ExaminerDashBoard from './Components/ExaminerDashBoard/ExaminerDashBoard';
import ExaminerSignin from './Components/ExaminerSignin/ExaminerSignin';
import ManageExaminee from './Components/ManageExaminee/ManageExaminee';
import AddExaminee from './Components/ManageExaminee/AddExaminee';
import CreateExam from './Components/ExamDetail/CreateExam';
import ExamQuestions from './Components/ExamDetail/ExamQuestions';
import AddMcq from './Components/Question/AddMcq';
import AddProgrammingQuestion from './Components/Question/AddProgrammingQuestion';
import McqQuestionPool from './Components/Question/McqQuestionPool';
import ProgrammingQuestionPool from './Components/Question/ProgrammingQuestionPool';

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
      path:'/examinee-dashboard/:email',
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
    },
    {
      path:'/addMcqQuestion/:examId',
      element:<AddMcq/>
    },
    {
      path:'/addProgQuestion/:examId',
      element:<AddProgrammingQuestion/>
    },
    {
      path:'/mcqQuestionPool/:examId',
      element:<McqQuestionPool/>
    },
    {
      path:'/programmingQuestionPool/:examId',
      element:<ProgrammingQuestionPool/>
    }

  ])
  return (
    <>
    <RouterProvider router={router}/>
    </>
  );
}

export default App;
