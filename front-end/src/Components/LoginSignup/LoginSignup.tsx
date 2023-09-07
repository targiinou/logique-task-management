import React, { useState } from 'react'
import './LoginSignup.css'
import user_icon from '../Assests/person.png'
import email_icon from '../Assests/email.png'
import password_icon from '../Assests/password.png'

export const LoginSignup = () => {

    const [action, setAction] = useState("Sign Up");

  return (
    <div className='container'>
        <div className='header'>
            <div className='text'>{action}</div>
            <div className='underline'></div>
        </div>
        <div className='inputs'>
            {action==="Login" ? <div></div> : 
            <div className='input'>
                <img src={user_icon} alt=''/>
                <input type='text' placeholder='Nome'/>
            </div>}
            <div className='input'>
                <img src={email_icon} alt=''/>
                <input type='email' placeholder='Email'/>
            </div>
            <div className='input'>
                <img src={password_icon} alt=''/>
                <input type='password' placeholder='Senha'/>
            </div>
        </div>
        {action==="Sign Up" ? <div></div> : 
        <div className="forgot-password">Esqueceu a senha? <span>Clique aqui!</span></div>}
        <div className='submit-container'>
            <div className={action === "Login" ? "submit gray" : "submit"} onClick={()=>{setAction("Sign Up")}}>Registre-se</div>
            <div className={action === "Sign Up" ? "submit gray" : "submit"} onClick={()=>{setAction("Login")}}>Login</div>
        </div>
    </div>
  )
}
