import React, { useState, useEffect, useContext } from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { PlusCircle, LogOut, BarChart2, CheckCircle, Edit, ShieldAlert, FileText, Check, Lock } from 'lucide-react';
import api from '../api/axios';
import { AuthContext } from '../context/AuthContext';
import Analytics from './Analytics';

const Dashboard = () => {
  const { user, logout } = useContext(AuthContext);
  const [surveys, setSurveys] = useState([]);
  const [analytics, setAnalytics] = useState(null);

  useEffect(() => {
    fetchSurveys();
  }, []);

  const fetchSurveys = async () => {
    try {
      const res = await api.get('/surveys');
      setSurveys(res.data);
    } catch (error) {
      console.error('Failed to fetch surveys', error);
    }
  };

  const viewAnalytics = (id) => {
    setAnalytics(id);
  };

  const updateStatus = async (id, status) => {
    try {
      await api.put(`/surveys/${id}/status`, { status });
      fetchSurveys();
    } catch (error) {
      console.error('Failed to update status', error);
      alert('Failed to update status');
    }
  };

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.5 }}>
      <div className="header">
        <div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>Surveys Dashboard</h1>
          <p style={{ color: 'var(--text-secondary)' }}>Welcome, {user.username} ({user.role})</p>
        </div>
        <div style={{ display: 'flex', gap: '1rem' }}>
          {user.role === 'ADMIN' && (
            <Link to="/create-survey" className="btn btn-primary">
              <PlusCircle size={18} /> New Survey
            </Link>
          )}
          <button onClick={logout} className="btn btn-secondary">
            <LogOut size={18} /> Logout
          </button>
        </div>
      </div>

      {analytics && (
        <Analytics surveyId={analytics} onClose={() => setAnalytics(null)} />
      )}

      <div className="grid">
        {surveys.filter(s => user.role === 'ADMIN' || s.status === 'ACTIVE').map(survey => (
          <motion.div whileHover={{ scale: 1.02 }} key={survey.id} className="card" style={{ display: 'flex', flexDirection: 'column' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <h3 style={{ marginBottom: '0.5rem', fontSize: '1.25rem' }}>{survey.title}</h3>
              <span style={{ 
                padding: '0.2rem 0.5rem', borderRadius: '4px', fontSize: '0.8rem', fontWeight: 'bold',
                backgroundColor: survey.status === 'ACTIVE' ? 'rgba(16,185,129,0.2)' : survey.status === 'CLOSED' ? 'rgba(239,68,68,0.2)' : 'rgba(245,158,11,0.2)',
                color: survey.status === 'ACTIVE' ? '#10b981' : survey.status === 'CLOSED' ? '#ef4444' : '#f59e0b'
              }}>
                {survey.status}
              </span>
            </div>
            
            <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem', minHeight: '3rem' }}>
              {survey.description}
            </p>

            <div style={{ display: 'flex', gap: '0.5rem', marginTop: 'auto', flexWrap: 'wrap' }}>
              {user.role === 'RESPONDENT' && survey.status === 'ACTIVE' ? (
                <Link to={`/survey/${survey.id}`} className="btn btn-primary" style={{ flex: 1, justifyContent: 'center' }}>
                  <CheckCircle size={18} /> Take Survey
                </Link>
              ) : user.role === 'ADMIN' ? (
                <>
                  <button onClick={() => viewAnalytics(survey.id)} className="btn btn-primary" style={{ flex: 1, justifyContent: 'center' }}>
                    <BarChart2 size={18} /> Analytics
                  </button>
                  {survey.status === 'DRAFT' && (
                    <button onClick={() => updateStatus(survey.id, 'ACTIVE')} className="btn btn-secondary" style={{ flex: 1, justifyContent: 'center', color: '#10b981', borderColor: '#10b981' }}>
                      <Check size={18} /> Publish
                    </button>
                  )}
                  {survey.status === 'ACTIVE' && (
                    <button onClick={() => updateStatus(survey.id, 'CLOSED')} className="btn btn-secondary" style={{ flex: 1, justifyContent: 'center', color: '#ef4444', borderColor: '#ef4444' }}>
                      <Lock size={18} /> Close
                    </button>
                  )}
                </>
              ) : null}
            </div>
          </motion.div>
        ))}
      </div>
    </motion.div>
  );
};

export default Dashboard;
