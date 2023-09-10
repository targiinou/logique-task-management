import './App.css';
import { LoginSignup } from './Components/LoginSignup/LoginSignup';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Home } from './Components/Home/Home'
import { ArchivedTasksList } from './Components/ArchivedTasksList/ArchivedTasksList';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/login' element={<LoginSignup initialAction="Login" />} />
        <Route path='/registrar' element={<LoginSignup initialAction="Sign Up" />} />
        <Route path='/' element={<Navigate to='/login' />} />
        <Route path='/home' element={<Home/>} />
        <Route path='/tarefas-arquivadas' element={<ArchivedTasksList/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
