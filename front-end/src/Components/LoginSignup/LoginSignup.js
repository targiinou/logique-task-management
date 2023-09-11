import React, { useState } from 'react';
import './LoginSignup.css';
import user_icon from '../Assets/person.png';
import email_icon from '../Assets/email.png';
import password_icon from '../Assets/password.png';
import axios from 'axios';
import { Navigate } from 'react-router-dom';

export const LoginSignup = ({ initialAction }) => {
  const [action, setAction] = useState(initialAction);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [navigate, setNavigate] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleLoginButtonClick = async () => {
    if (isLoading) return;
    setIsLoading(true);

    if (action === 'Login') {
      try {
        const response = await axios.post('http://localhost:8080/api/v1/auth/authenticate', {
          email,
          password,
        });

        const { access_token, refresh_token } = response.data.result;

        // Salvando os tokens no localStorage
        localStorage.setItem('access_token', access_token);
        localStorage.setItem('refresh_token', refresh_token);

        axios.defaults.headers.common['Authorization'] = `Bearer ${access_token}`;
        setNavigate(true);
      } catch (error) {
        console.error('Erro no login:', error);
        setErrorMessage(error.response?.data?.resultMessage || 'Usuário ou senha errado.');
      }
    } else if (action === 'Sign Up') {
      setAction('Login');
    }

    setIsLoading(false);
  };

  const handleRegisterButtonClick = async () => {
    if (isLoading) return;
    setIsLoading(true);

    if (action === 'Sign Up') {
      try {
        const response = await axios.post('http://localhost:8080/api/v1/auth/register', {
          email,
          password,
          name,
        });

        const { access_token, refresh_token } = response.data.result;

        // Salvando os tokens no localStorage
        localStorage.setItem('access_token', access_token);
        localStorage.setItem('refresh_token', refresh_token);

        setNavigate(true);
      } catch (error) {
        console.error('Erro no registro:', error);
        setErrorMessage(error.response?.data?.resultMessage || 'Erro no registro.');
      }
    } else if (action === 'Login') {
      setAction('Sign Up');
    }

    setIsLoading(false);
  };

  if (navigate) {
    return <Navigate to="/home"></Navigate>;
  }

  return (
    <div className='login-signup-container'>
      <div className='container'>
        <div className='header'>
          <div className='text'>{action === 'Login' ? 'Entrar' : 'Registrar'}</div>
          <div className='underline'></div>
        </div>
        <div className='inputs'>
          {action === 'Login' ? (
            <div></div>
          ) : (
            <div className='input'>
              <img src={user_icon} alt='' />
              <input
                type='text'
                placeholder='Nome'
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </div>
          )}
          <div className='input'>
            <img src={email_icon} alt='' />
            <input
              type='email'
              placeholder='Email'
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className='input'>
            <img src={password_icon} alt='' />
            <input
              type='password'
              placeholder='Senha'
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
        </div>
        {errorMessage && <div className='error-message-auth'>{errorMessage}</div>}
        <div className='submit-container'>
          <div
            className={`${
              action === 'Login' || isLoading ? 'gray' : 'submit'
            } ${isLoading ? 'disabled' : ''}`}
            onClick={handleRegisterButtonClick}
          >
            Registre-se
          </div>
          <div
            className={`${
              action === 'Sign Up' || isLoading ? 'gray' : 'submit'
            } ${isLoading ? 'disabled' : ''}`}
            onClick={handleLoginButtonClick}
          >
            Entrar
          </div>
        </div>
      </div>
    </div>
  );
};
