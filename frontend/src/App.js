
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import LandingPage from './Components/LandingPage/LandingPage'
import ExaminerLogin from './Components/Examiner/ExaminerLogin'
import ExamineeLogin from './Components/Examinee/ExamineeLogin'
import ExamineeDashboard from './Components/Examinee/ExamineeDashBoard'
import ExaminerDashBoard from './Components/Examiner/ExaminerDashBoard';
import ExaminerSignin from './Components/Examiner/ExaminerSignin';
import ManageExaminee from './Components/Examiner/ManageExaminee/ManageExaminee';
import AddExaminee from './Components/Examiner/ManageExaminee/AddExaminee';
import CreateExam from './Components/Examiner/ExamDetail/CreateExam';
import ExamQuestions from './Components/Examiner/ExamDetail/ExamQuestions';
import AddMcq from './Components/Examiner/Question/AddMcq';
import AddProgrammingQuestion from './Components/Examiner/Question/AddProgrammingQuestion';
import McqQuestionPool from './Components/Examiner/Question/McqQuestionPool';
import ProgrammingQuestionPool from './Components/Examiner/Question/ProgrammingQuestionPool';
import GiveExam from './Components/Examinee/GiveExam';
import ExamAnswers from './Components/Examiner/ExamDetail/ExamAnswers';
import Answer from './Components/Examiner/ExamDetail/Answer';

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
    },
    {
      path:'/startExam/:examId',
      element:<GiveExam/>
    },
    {
      path:'/examAnswers/:examId',
      element:<ExamAnswers/>
    },
    {
      path:'/detail-answer/:examineeId/:examId',
      element:<Answer/>
    }

  ])
  return (
    <>
    <RouterProvider router={router}/>
    </>
  );
}

export default App;
