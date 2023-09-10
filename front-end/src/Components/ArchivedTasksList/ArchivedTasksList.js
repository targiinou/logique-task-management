import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { AiFillEye } from "react-icons/ai";
import { Link } from 'react-router-dom';

export const ArchivedTasksList = () => {
  const [archivedTasks, setArchivedTasks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchArchivedTasks = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/task/list');
        const result = response.data;

        if (result.resultType === 'OK') {
          const archivedTaskList = result.result.filter((task) => task.status === 'ARCHIVED');
          setArchivedTasks(archivedTaskList);
        } else {
          console.error('Erro na resposta:', result.resultMessage);
        }

        setIsLoading(false);
      } catch (error) {
        console.error('Erro ao buscar tarefas arquivadas:', error);
        setIsLoading(false);
      }
    };

    fetchArchivedTasks();
  }, []);

  const parseTaskStatus = (status) => {
    switch (status) {
      case 'NOT_STARTED':
        return 'Não iniciada';
      case 'IN_PROGRESS':
        return 'Em progresso';
      case 'FINISHED':
        return 'Finalizada';
      case 'ARCHIVED':
        return 'Arquivada';
      default:
        return status;
    }
  };

  return (
    <body className='main-container'>
      <div className='header-home'>
        <div className='header-title'>Gerenciador de tarefas</div>
      </div>
      <div className='container-title'>
        <div className='page-title'>
          <h1>Tarefas arquivadas</h1>
          <div className='button-container'>
            <Link className="link-no-underline" to="/home">
              <button className='create-task-button'><AiFillEye className='icon-large'/>Ver tarefas</button>
            </Link>
          </div>
        </div>
      </div>

      <div className='task-list'>
        {isLoading ? (
          <p>Carregando...</p>
        ) : (
          <div className='columns-container'>
            <div className='card-column'>
              <h2>{parseTaskStatus('ARCHIVED')}</h2>
              {archivedTasks.length === 0 ? ( // Verifique se a lista está vazia
                <div className='empty-message'>Ainda não há tarefas com o status: {parseTaskStatus('ARCHIVED')}</div>
              ) : (
                archivedTasks.map((task) => (
                  <div className='task-card' key={task.id}>
                    <div className='card-title'>{task.title}</div>
                    <div className='status'>{parseTaskStatus(task.status)}</div>
                    <div className='description'>{task.description}</div>
                  </div>
                ))
              )}
            </div>
          </div>
        )}
      </div>
    </body>
  );
};