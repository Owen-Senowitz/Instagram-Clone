import React, { useState } from 'react';
import apiClient from '../api/axios';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';
import { useAuth } from '../context/AuthContext';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const { login } = useAuth();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await apiClient.post('/user/login', {
        email,
        password,
      });
      login(response.data);
      setMessage('Login successful');
    } catch (error: any) {
      if (error.response) {
        if (error.response.status === 401) {
          setMessage('Wrong email or password');
        } else {
          setMessage(error.response.data || 'An error occurred');
        }
      } else if (error.request) {
        setMessage('No response received from server');
      } else {
        setMessage('Error in setting up the request');
      }
    }
  };

  return (
    <Container maxWidth="sm">
      <Box component="form" onSubmit={handleLogin} sx={{ mt: 3 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Login
        </Typography>
        <TextField
          label="Email"
          variant="outlined"
          fullWidth
          margin="normal"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <TextField
          label="Password"
          variant="outlined"
          fullWidth
          margin="normal"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
          Login
        </Button>
        {message && (
          <Alert severity={message === 'Login successful' ? 'success' : 'error'} sx={{ mt: 2 }}>
            {message}
          </Alert>
        )}
      </Box>
    </Container>
  );
};

export default Login;
