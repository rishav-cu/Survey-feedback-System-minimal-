import React, { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { PieChart, Pie, Cell, Tooltip, BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer } from 'recharts';
import api from '../api/axios';
import { X, Users, Star } from 'lucide-react';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

const Analytics = ({ surveyId, onClose }) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const res = await api.get(`/surveys/${surveyId}/analytics`);
        setData(res.data);
      } catch (error) {
        console.error('Failed to load analytics', error);
      } finally {
        setLoading(false);
      }
    };
    fetchAnalytics();
  }, [surveyId]);

  if (loading) {
    return <div style={{ padding: '2rem', textAlign: 'center' }}>Loading analytics...</div>;
  }

  if (!data) return null;

  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      className="card"
      style={{ position: 'relative', marginBottom: '2rem', border: '1px solid var(--primary-color)' }}
    >
      <button 
        onClick={onClose} 
        style={{ position: 'absolute', top: '1rem', right: '1rem', background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-color)' }}
      >
        <X size={24} />
      </button>

      <h2 style={{ fontSize: '1.5rem', marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
        Analytics for Survey #{surveyId}
      </h2>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginBottom: '2rem' }}>
        <div className="card" style={{ textAlign: 'center', padding: '1rem', background: 'rgba(255,255,255,0.05)' }}>
          <Users size={32} color="var(--primary-color)" style={{ margin: '0 auto 0.5rem' }} />
          <h3>Total Responses</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>{data.totalResponses}</p>
        </div>
        <div className="card" style={{ textAlign: 'center', padding: '1rem', background: 'rgba(255,255,255,0.05)' }}>
          <Star size={32} color="var(--success-color)" style={{ margin: '0 auto 0.5rem' }} />
          <h3>Average Rating</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--success-color)' }}>{data.averageRating} / 5</p>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
        {data.questionAnalytics.map((q, idx) => (
          <div key={q.questionId} style={{ padding: '1.5rem', borderRadius: '12px', background: 'rgba(0,0,0,0.2)' }}>
            <h3 style={{ marginBottom: '1rem', fontSize: '1.2rem' }}>{idx + 1}. {q.questionText}</h3>
            
            {q.questionType === 'MULTIPLE_CHOICE' && q.answerCounts && (
              <div style={{ height: 300 }}>
                <ResponsiveContainer width="100%" height="100%">
                  <PieChart>
                    <Pie
                      data={Object.entries(q.answerCounts).map(([name, value]) => ({ name, value }))}
                      dataKey="value"
                      nameKey="name"
                      cx="50%"
                      cy="50%"
                      outerRadius={100}
                      label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                    >
                      {Object.entries(q.answerCounts).map((_, i) => (
                        <Cell key={`cell-${i}`} fill={COLORS[i % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip contentStyle={{ backgroundColor: 'var(--surface-color)', border: 'none', borderRadius: '8px' }} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
            )}

            {q.questionType === 'RATING' && (
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                <div style={{ height: 100, flex: 1 }}>
                  <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={[{ name: 'Average', value: q.averageRating }]} layout="vertical">
                      <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="rgba(255,255,255,0.1)" />
                      <XAxis type="number" domain={[0, 5]} stroke="var(--text-secondary)" />
                      <YAxis type="category" dataKey="name" stroke="var(--text-secondary)" width={80} />
                      <Tooltip cursor={{ fill: 'rgba(255,255,255,0.05)' }} contentStyle={{ backgroundColor: 'var(--surface-color)', border: 'none', borderRadius: '8px' }} />
                      <Bar dataKey="value" fill="var(--success-color)" radius={[0, 4, 4, 0]} />
                    </BarChart>
                  </ResponsiveContainer>
                </div>
              </div>
            )}

            {q.questionType === 'TEXT' && (
              <p style={{ color: 'var(--text-secondary)' }}>Text answers are omitted from summary visualizations.</p>
            )}
          </div>
        ))}
      </div>
    </motion.div>
  );
};

export default Analytics;
