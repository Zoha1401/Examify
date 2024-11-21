
import './App.css';
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import LandingPage from './Components/LandingPage/LandingPage'
import ExaminerLogin from './Components/ExaminerLogin/ExaminerLogin'
import ExamineeLogin from './Components/ExamineeLogin/ExamineeLogin'
import ExamineeDashboard from './Components/ExamineeDashBoard/ExamineeDashBoard'
import ExaminerDashBoard from './Components/ExaminerDashBoard/ExaminerDashBoard';

function App() {
  const router=createBrowserRouter([
    {
      path:'/',
      element:<LandingPage/>
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
      path:'/examinerDashboard',
      element:<ExaminerDashBoard/>
    },
    {
      path:'/examineeDashboard',
      element:<ExamineeDashboard/>
    }
    
  ])
  return (
    <>
    <RouterProvider router={router}/>
    </>
  );
}

export default App;
