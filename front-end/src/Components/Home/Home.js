import './Home.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { VscDebugStart, VscDebugRestart } from "react-icons/vsc";
import { BsFileEarmarkExcelFill } from "react-icons/bs";
import { AiOutlineCheck, AiFillEye } from "react-icons/ai";
import { Link } from 'react-router-dom';
import { IoIosAddCircle } from "react-icons/io";
import CreateTaskModal from '../CreateTaskModal/CreateTaskModal';

export const Home = () => {
  const [tasks, setTasks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [isSticky, setIsSticky] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      const scrollTop = window.scrollY;
      if (scrollTop > 0) {
        setIsSticky(true);
      } else {
        setIsSticky(false);
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, []);

  const openModal = () => {
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
  };

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

  useEffect(() => {
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

      fetchTasks();
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

  return (
    <div className='main-container'>
      <div className={`header-home ${isSticky ? 'sticky-header' : ''}`}>
        <div className='header-title'>Gerenciador de tarefas</div>
      </div>
      <div className='container-title'>
        <div className='page-title'>
          <h1>Lista de tarefas</h1>
          <div className='button-container'>
            <Link className="link-no-underline" onClick={openModal} style={{ marginRight: '10px' }}>
              <button className='create-task-button'><IoIosAddCircle className='icon-large'/>Criar Tarefa</button>
            </Link>
            <CreateTaskModal
              isOpen={modalIsOpen}
              onRequestClose={closeModal}
              className="modal-container" // Adicione esta classe ao modal
            />

            <Link className="link-no-underline" to="/tarefas-arquivadas">
              <button className='create-task-button'><AiFillEye className='icon-large'/>Ver arquivadas</button>
            </Link>
          </div>
        </div>
      </div>
        
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
                      <div className='grid-container'>
                        <div className='card-title'>{task.title}</div>
                        <div className='description'>{task.description}</div>
                      </div>
                      
                      <div className='options'>
                        {task.status === 'NOT_STARTED' && (
                          <>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'IN_PROGRESS')}>
                              <VscDebugStart />
                              <span className='status-popup'>Iniciar</span>
                            </button>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'FINISHED')}>
                              <AiOutlineCheck />
                              <span className='status-popup'>Finalizar</span>
                            </button>
                          </>
                        )}
                        {task.status === 'IN_PROGRESS' && (
                          <>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'NOT_STARTED')}>
                              <VscDebugRestart />
                              <span className='status-popup'>Não iniciada</span>
                            </button>
                            <button className='action-button' onClick={() => changeTaskStatus(task.id, 'FINISHED')}>
                              <AiOutlineCheck />
                              <span className='status-popup'>Finalizar</span>
                            </button>
                          </>
                        )}
                        {task.status !== 'FINALIZED' && (
                          <>
                            <button className='action-button' onClick={() => archiveTask(task.id)}>
                              <BsFileEarmarkExcelFill />
                              <span className='status-popup'>Arquivar</span>
                            </button>
                          </>
                        )}
                      </div>
                    </div>
                  ))}
                  {tasks.filter((task) => task.status === status).length === 0 && (
                  <div className='empty-message'>Ainda não tarefas com o status: {parseTaskStatus(status)}</div>
                  )}
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};
