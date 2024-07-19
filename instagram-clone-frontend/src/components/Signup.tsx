import React, { useState } from 'react';
import apiClient from '../api/axios';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';

const Signup: React.FC = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await apiClient.post('/auth/signup', {
        username,
        email,
        password,
      });
      setMessage(response.data);
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
      <Box component="form" onSubmit={handleSignup} sx={{ mt: 3 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Signup
        </Typography>
        <TextField
          label="Username"
          variant="outlined"
          fullWidth
          margin="normal"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
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
          Signup
        </Button>
        {message && (
          <Alert severity={message === 'User registered successfully' ? 'success' : 'error'} sx={{ mt: 2 }}>
            {message}
          </Alert>
        )}
      </Box>
    </Container>
  );
};

export default Signup;