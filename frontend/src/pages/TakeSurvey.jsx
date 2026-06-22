import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Send, ArrowLeft } from 'lucide-react';
import { motion } from 'framer-motion';
import api from '../api/axios';

const TakeSurvey = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [survey, setSurvey] = useState(null);
  const [answers, setAnswers] = useState({});

  useEffect(() => {
    const fetchSurvey = async () => {
      try {
        const res = await api.get(`/surveys/${id}`);
        setSurvey(res.data);
      } catch (error) {
        console.error('Failed to fetch survey', error);
      }
    };
    fetchSurvey();
  }, [id]);

  const handleAnswer = (questionId, value) => {
    setAnswers(prev => ({ ...prev, [questionId]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const formattedAnswers = Object.entries(answers).map(([qId, val]) => {
        const q = survey.questions.find(qu => qu.id === parseInt(qId));
        return {
          questionId: parseInt(qId),
          answerText: (q.questionType === 'TEXT' || q.questionType === 'MULTIPLE_CHOICE') ? val : null,
          ratingValue: q.questionType === 'RATING' ? parseInt(val) : null
        };
      });

      await api.post('/feedback', { surveyId: id, answers: formattedAnswers });
      alert('Thank you for your feedback!');
      navigate('/dashboard');
    } catch (error) {
      console.error('Failed to submit feedback', error);
      alert('Failed to submit feedback');
    }
  };

  if (!survey) return <div>Loading...</div>;

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }} style={{ maxWidth: '800px', margin: '0 auto' }}>
      <div className="header">
        <div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{survey.title}</h1>
          <p style={{ color: 'var(--text-secondary)' }}>{survey.description}</p>
        </div>
        <button onClick={() => navigate('/dashboard')} className="btn btn-secondary">
          <ArrowLeft size={18} /> Back
        </button>
      </div>

      <div className="card">
        <form onSubmit={handleSubmit}>
          {survey.questions.map((q, index) => (
            <div key={q.id} style={{ marginBottom: '2rem' }}>
              <label style={{ display: 'block', marginBottom: '0.75rem', fontWeight: '500', fontSize: '1.1rem' }}>
                {index + 1}. {q.questionText}
              </label>
              
              {q.questionType === 'TEXT' && (
                <textarea 
                  className="input-control" 
                  rows="3"
                  value={answers[q.id] || ''}
                  onChange={e => handleAnswer(q.id, e.target.value)}
                  required
                />
              )}
              
              {q.questionType === 'RATING' && (
                <div style={{ display: 'flex', gap: '1rem' }}>
                  {[1, 2, 3, 4, 5].map(rating => (
                    <label key={rating} style={{ display: 'flex', alignItems: 'center', gap: '0.25rem', cursor: 'pointer' }}>
                      <input 
                        type="radio" 
                        name={`rating-${q.id}`} 
                        value={rating} 
                        onChange={e => handleAnswer(q.id, e.target.value)}
                        required
                        style={{ width: '1.2rem', height: '1.2rem', accentColor: 'var(--primary-color)' }}
                      />
                      <span style={{ fontSize: '1.1rem' }}>{rating}</span>
                    </label>
                  ))}
                </div>
              )}

              {q.questionType === 'MULTIPLE_CHOICE' && (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                  {q.options?.map((opt, index) => (
                    <label key={index} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                      <input 
                        type="radio" 
                        name={`mc-${q.id}`} 
                        value={opt} 
                        onChange={e => handleAnswer(q.id, e.target.value)}
                        required
                        style={{ width: '1.2rem', height: '1.2rem', accentColor: 'var(--primary-color)' }}
                      />
                      <span style={{ fontSize: '1.1rem' }}>{opt}</span>
                    </label>
                  ))}
                </div>
              )}
            </div>
          ))}

          <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }}>
            <Send size={18} /> Submit Feedback
          </button>
        </form>
      </div>
    </motion.div>
  );
};

export default TakeSurvey;
