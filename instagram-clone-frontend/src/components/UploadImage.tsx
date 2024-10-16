import React, { useState } from 'react';
import { Box, Button, CircularProgress, Typography } from '@mui/material';
import apiClient from '../api/axios';
import { useAuth } from '../context/AuthContext' // Adjust this import based on your project structure

interface UploadImageProps {
  onUploadSuccess: (imageId: number) => void;
}

const UploadImage: React.FC<UploadImageProps> = ({ onUploadSuccess }) => {
  const { token } = useAuth();
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState('');

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      setError('Please select a file before uploading.');
      return;
    }

    setUploading(true);
    setError('');

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await apiClient.post('/images/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`,
        },
      });

      const imageId = response.data.id; // Assuming the backend returns { "id": number }
      onUploadSuccess(imageId); // Call the callback with the uploaded image ID
    } catch (error: any) {
      setError('Error uploading the image');
    } finally {
      setUploading(false);
    }
  };

  return (
    <Box>
      <input
        type="file"
        accept="image/*"
        onChange={handleFileChange}
        disabled={uploading}
      />

      <Button
        variant="contained"
        color="primary"
        onClick={handleUpload}
        disabled={uploading || !selectedFile}
        sx={{ mt: 2 }}
      >
        {uploading ? 'Uploading...' : 'Upload Image'}
      </Button>

      {uploading && <CircularProgress sx={{ mt: 2 }} />}

      {error && (
        <Typography variant="body2" color="error" sx={{ mt: 2 }}>
          {error}
        </Typography>
      )}
    </Box>
  );
};

export default UploadImage;
