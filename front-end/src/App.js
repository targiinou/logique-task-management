import './App.css';
import { LoginSignup } from './Components/LoginSignup/LoginSignup';
import { BrowserRouter, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { Home } from './Components/Home/Home'
import { CreateTask } from './Components/CreateTask/CreateTask';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/login' element={<LoginSignup initialAction="Login" />} />
        <Route path='/registrar' element={<LoginSignup initialAction="Sign Up" />} />
        <Route path='/' element={<Navigate to='/login' />} />
        <Route path='/home' element={<Home/>} />
        <Route path='/create-task' element={<CreateTask/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
