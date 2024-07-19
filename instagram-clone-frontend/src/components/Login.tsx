import React, { useState } from 'react';
import apiClient from '../api/axios';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [token, setToken] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await apiClient.post('/auth/login', {
        email,
        password,
      });
      setToken(response.data);
      setMessage('Login successful');
    } catch (error: any) {
      if (error.response) {
        setMessage(error.response.data);
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
        {token && (
          <Alert severity="info" sx={{ mt: 2 }}>
            Token: {token}
          </Alert>
        )}
      </Box>
    </Container>
  );
};

export default Login;
