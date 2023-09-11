import React, { useState, useEffect, useCallback } from 'react';
import './CreateTaskModal.css';
import { GrClose } from 'react-icons/gr';
import axios from 'axios';

const CreateTaskModal = ({ isOpen, onRequestClose }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const areFieldsFilled = useCallback(() => {
    return title.trim() !== '' && description.trim() !== '';
  }, [title, description]);

  useEffect(() => {
    setIsButtonDisabled(!areFieldsFilled());
  }, [title, description, areFieldsFilled]);

  const handleCreateTask = async () => {
    if (isButtonDisabled || isSubmitting) {
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await axios.post('http://localhost:8080/api/task', {
        title,
        description,
      });

      if (response && response.data && response.data.resultType === 'OK') {
        setTitle('');
        setDescription('');
        setIsButtonDisabled(true);
        setShowSuccessMessage(true);
        setTimeout(() => {
          setShowSuccessMessage(false);
          setErrorMessage('');
          onRequestClose();
        }, 1000);
      } else {
        setErrorMessage(response?.data?.resultMessage || 'Erro desconhecido ao criar a tarefa.');
      }
    } catch (error) {
      console.error('Erro ao enviar o formulário:', error.response.data.resultMessage);
      setErrorMessage(error.response.data.resultMessage);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCloseModal = () => {
    setErrorMessage('');
    onRequestClose();
  };

  if (!isOpen) {
    return null;
  }

  return (
    <div className="create-task-modal">
      <div className="modal-content">
        <div className="title-container">
          <div className="modal-title">Criar tarefa</div>
          <button className="button-close-modal" onClick={handleCloseModal}>
            <GrClose />
          </button>
        </div>
        {showSuccessMessage ? (
          <div className="success-message">Tarefa criada com sucesso!</div>
        ) : (
          <div>
            {errorMessage && (
              <div className="error-message">{errorMessage}</div>
            )}
            <div className="input-container">
              <div className="task-name-container">
                <label className="label">Título</label>
                <input
                  className="input-form"
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </div>
              <div className="description-container">
                <label className="label">Descrição</label>
                <textarea
                  className="textarea-form"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>
            </div>
            <div className="submit-container-form">
              <button
                className="button-submit"
                style={{
                  backgroundColor: isButtonDisabled ? '#ccc' : '#8252d0',
                  cursor: isButtonDisabled || isSubmitting ? 'not-allowed' : 'pointer',
                }}
                onClick={handleCreateTask}
                disabled={isButtonDisabled || isSubmitting}
              >
                {isSubmitting ? 'Enviando...' : 'Criar'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CreateTaskModal;
