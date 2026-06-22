import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Trash2, Save, ArrowLeft } from 'lucide-react';
import { motion } from 'framer-motion';
import api from '../api/axios';
import { AuthContext } from '../context/AuthContext';

const CreateSurvey = () => {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [questions, setQuestions] = useState([{ questionText: '', questionType: 'TEXT', options: [] }]);

  if (user?.role !== 'ADMIN') {
    return <div style={{ textAlign: 'center', marginTop: '4rem' }}>Unauthorized</div>;
  }

  const addQuestion = () => {
    setQuestions([...questions, { questionText: '', questionType: 'TEXT', options: [] }]);
  };

  const addOption = (qIndex) => {
    const newQuestions = [...questions];
    if (!newQuestions[qIndex].options) newQuestions[qIndex].options = [];
    newQuestions[qIndex].options.push('');
    setQuestions(newQuestions);
  };

  const updateOption = (qIndex, optIndex, value) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].options[optIndex] = value;
    setQuestions(newQuestions);
  };

  const removeOption = (qIndex, optIndex) => {
    const newQuestions = [...questions];
    newQuestions[qIndex].options = newQuestions[qIndex].options.filter((_, i) => i !== optIndex);
    setQuestions(newQuestions);
  };

  const removeQuestion = (index) => {
    setQuestions(questions.filter((_, i) => i !== index));
  };

  const updateQuestion = (index, field, value) => {
    const newQuestions = [...questions];
    newQuestions[index][field] = value;
    if (field === 'questionType' && value === 'MULTIPLE_CHOICE' && (!newQuestions[index].options || newQuestions[index].options.length === 0)) {
        newQuestions[index].options = ['Option 1', 'Option 2'];
    }
    setQuestions(newQuestions);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/surveys', { title, description, questions });
      navigate('/dashboard');
    } catch (error) {
      console.error('Failed to create survey', error);
      alert('Failed to create survey');
    }
  };

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }} style={{ maxWidth: '800px', margin: '0 auto' }}>
      <div className="header">
        <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Create New Survey</h1>
        <button onClick={() => navigate('/dashboard')} className="btn btn-secondary">
          <ArrowLeft size={18} /> Back
        </button>
      </div>

      <div className="card">
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label>Survey Title</label>
            <input 
              type="text" 
              className="input-control" 
              value={title} 
              onChange={e => setTitle(e.target.value)} 
              required 
            />
          </div>
          <div className="input-group">
            <label>Description</label>
            <textarea 
              className="input-control" 
              value={description} 
              onChange={e => setDescription(e.target.value)} 
              rows="3"
            />
          </div>

          <h3 style={{ margin: '2rem 0 1rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '0.5rem' }}>
            Questions
          </h3>

          {questions.map((q, index) => (
            <div key={index} style={{ marginBottom: '1.5rem', padding: '1rem', border: '1px solid var(--border-color)', borderRadius: '8px' }}>
              <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-start' }}>
                <div style={{ flex: 2 }}>
                  <input 
                    type="text" 
                    className="input-control" 
                    placeholder={`Question ${index + 1}`}
                    value={q.questionText} 
                    onChange={e => updateQuestion(index, 'questionText', e.target.value)} 
                    required 
                  />
                </div>
                <div style={{ flex: 1 }}>
                  <select 
                    className="input-control" 
                    value={q.questionType} 
                    onChange={e => updateQuestion(index, 'questionType', e.target.value)}
                  >
                    <option value="TEXT">Text Answer</option>
                    <option value="RATING">Rating (1-5)</option>
                    <option value="MULTIPLE_CHOICE">Multiple Choice</option>
                  </select>
                </div>
                {questions.length > 1 && (
                  <button type="button" onClick={() => removeQuestion(index)} className="btn btn-secondary" style={{ color: 'var(--danger-color)', borderColor: 'var(--danger-color)' }}>
                    <Trash2 size={18} />
                  </button>
                )}
              </div>

              {q.questionType === 'MULTIPLE_CHOICE' && (
                <div style={{ marginTop: '1rem', paddingLeft: '1rem', borderLeft: '2px solid var(--primary-color)' }}>
                  <label style={{ fontSize: '0.9rem', color: 'var(--text-secondary)' }}>Options</label>
                  {q.options?.map((opt, optIndex) => (
                    <div key={optIndex} style={{ display: 'flex', gap: '0.5rem', marginTop: '0.5rem' }}>
                      <input 
                        type="text" 
                        className="input-control" 
                        value={opt} 
                        onChange={e => updateOption(index, optIndex, e.target.value)} 
                        placeholder={`Option ${optIndex + 1}`}
                        required 
                      />
                      <button type="button" onClick={() => removeOption(index, optIndex)} className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                        <Trash2 size={16} />
                      </button>
                    </div>
                  ))}
                  <button type="button" onClick={() => addOption(index)} className="btn btn-secondary" style={{ marginTop: '0.5rem', padding: '0.5rem 1rem', fontSize: '0.9rem' }}>
                    <Plus size={16} /> Add Option
                  </button>
                </div>
              )}
            </div>
          ))}

          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '2rem' }}>
            <button type="button" onClick={addQuestion} className="btn btn-secondary">
              <Plus size={18} /> Add Question
            </button>
            <button type="submit" className="btn btn-primary">
              <Save size={18} /> Save Survey
            </button>
          </div>
        </form>
      </div>
    </motion.div>
  );
};

export default CreateSurvey;
