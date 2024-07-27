import React, { useState } from 'react';
import apiClient from '../api/axios';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';

const ImageUpload: React.FC = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [message, setMessage] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setMessage('Please select a file to upload');
      return;
    }

    const formData = new FormData();
    formData.append('image', selectedFile);

    try {
      const response = await apiClient.post('/images/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setMessage(response.data);
    } catch (error: any) {
      setMessage('Error uploading image');
    }
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 3 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Upload Image
        </Typography>
        <TextField type="file" onChange={handleFileChange} fullWidth />
        <Button variant="contained" color="primary" onClick={handleUpload} sx={{ mt: 2 }}>
          Upload
        </Button>
        {message && (
          <Alert severity="info" sx={{ mt: 2 }}>
            {message}
          </Alert>
        )}
      </Box>
    </Container>
  );
};

export default ImageUpload;
