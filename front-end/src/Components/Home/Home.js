import './Home.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import _ from 'lodash';
import { VscDebugStart, VscDebugRestart } from "react-icons/vsc";
import { BsFileEarmarkExcelFill } from "react-icons/bs";
import { AiOutlineCheck } from "react-icons/ai";
import { Link } from 'react-router-dom';
import { IoIosAddCircle } from "react-icons/io";



export const Home = () => {
  const [tasks, setTasks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/task/list');
        const result = response.data;

        if (result.resultType === 'OK') {
          const taskList = result.result.filter((task) => task.status !== 'ARCHIVED');
          setTasks(taskList);
        } else {
          console.error('Erro na resposta:', result.resultMessage);
        }

        setIsLoading(false);
      } catch (error) {
        console.error('Erro ao buscar tarefas:', error);
        setIsLoading(false);
      }
    };

    fetchTasks();
  }, []);

  const statusOrder = ['NOT_STARTED', 'IN_PROGRESS', 'FINISHED'];

  const changeTaskStatus = async (taskId, newStatus) => {
    try {
      const response = await axios.put(
        `http://localhost:8080/api/task/${taskId}/update-status`,
        null,
        {
          params: { newStatus },
        }
      );

      const updatedTasks = tasks.map((task) =>
        task.id === taskId ? { ...task, status: newStatus } : task
      );

      setTasks(updatedTasks);
    } catch (error) {
      console.error('Erro ao alterar o status da tarefa:', error);
    }
  };

  const archiveTask = async (taskId) => {
    try {
      const response = await axios.put(
        `http://localhost:8080/api/task/${taskId}/archive`
      );

      const updatedTasks = tasks.filter((task) => task.id !== taskId);

      setTasks(updatedTasks);
    } catch (error) {
      console.error('Erro ao arquivar a tarefa:', error);
    }
  };

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

  useEffect(() => {
    const fetchTasksAgain = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/task/list');
        const result = response.data;

        if (result.resultType === 'OK') {
          const taskList = result.result.filter((task) => task.status !== 'ARCHIVED');
          setTasks(taskList);
        } else {
          console.error('Erro na resposta:', result.resultMessage);
        }
      } catch (error) {
        console.error('Erro ao buscar tarefas:', error);
      }
    };

    fetchTasksAgain();
  }, [tasks]);

  return (
    <body className='body-home'>
      <div className='header-home'></div>
      <Link className="link-no-underline" to="/create-task">
        <button className='create-task-button'><IoIosAddCircle className='icon-large'/>Criar Tarefa</button>
      </Link>
      <div className='task-list'>
        {isLoading ? (
          <p>Carregando...</p>
        ) : (
          <div className='columns-container'>
            {statusOrder.map((status) => (
              <div className='card-column' key={status}>
                <h2>{parseTaskStatus(status)}</h2>
                {tasks
                  .filter((task) => task.status === status)
                  .map((task) => (
                    <div className='task-card' key={task.id}>
                      <div className='card-title'>{task.title}</div>
                      <div className='status'>{parseTaskStatus(task.status)}</div>
                      <div className='description'>{task.description}</div>
                      <div className='options'>
                        {task.status === 'NOT_STARTED' && (
                          <>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'IN_PROGRESS')}>
                                <VscDebugStart/>
                            </button>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'FINISHED')}>
                                <AiOutlineCheck/>
                            </button>
                          </>
                        )}
                        {task.status === 'IN_PROGRESS' && (
                          <>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'NOT_STARTED')}>
                                <VscDebugRestart/>
                            </button>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'FINISHED')}>
                                <AiOutlineCheck/>
                            </button>
                          </>
                        )}
                        {task.status !== 'FINALIZED' && (
                          <button className='action-button' onClick={() => archiveTask(task.id)}><BsFileEarmarkExcelFill/></button>
                        )}
                      </div>
                    </div>
                  ))}
              </div>
            ))}
          </div>
        )}
      </div>
    </body>
  );
};
