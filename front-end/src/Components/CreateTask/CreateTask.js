import React, { useState } from 'react';
import axios from 'axios';

export const CreateTask= () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/api/task', {
        title,
        description,
      });

      if (response.data.resultType === 'OK') {
        // Limpar os campos após a criação bem-sucedida da tarefa
        setTitle('');
        setDescription('');
        alert('Tarefa criada com sucesso!');
      } else {
        alert('Erro ao criar tarefa: ' + response.data.resultMessage);
      }
    } catch (error) {
      console.error('Erro ao criar tarefa:', error);
    }
  };

  return (
    <div className="create-task-form">
      <h2>Criar Nova Tarefa</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="title">Título:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="description">Descrição:</label>
          <textarea
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
          />
        </div>
        <button type="submit">Criar Tarefa</button>
      </form>
    </div>
  );
};
