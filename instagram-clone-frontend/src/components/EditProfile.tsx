import React, { useState } from 'react';
import { Container, Box, Button, TextField, Typography } from '@mui/material';
import UploadImage from './UploadImage'; // Adjust the import based on your file structure
import apiClient from '../api/axios';

const EditProfile: React.FC = () => {
  const [username, setUsername] = useState('');
  const [bio, setBio] = useState('');
  const [profileImageId, setProfileImageId] = useState<number | null>(null);
  const [error, setError] = useState('');

  const handleUploadSuccess = (imageId: number) => {
    setProfileImageId(imageId); // Save the uploaded image ID
  };

  const handleSubmit = async () => {
    try {
      // Assuming you're updating the user profile
      await apiClient.put('/users/profile', {
        username,
        bio,
        profileImageId,
      });
      alert('Profile updated successfully');
    } catch (error: any) {
      setError('Error updating profile');
    }
  };

  return (
    <Container maxWidth="sm">
      <Box display="flex" flexDirection="column" gap={2} mt={5}>
        <Typography variant="h4" align="center">Edit Profile</Typography>

        <TextField
          label="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          fullWidth
        />

        <TextField
          label="Bio"
          value={bio}
          onChange={(e) => setBio(e.target.value)}
          fullWidth
          multiline
          rows={3}
        />

        <Typography variant="h6" align="left" mt={2}>
          Upload Profile Picture
        </Typography>

        {/* UploadImage component */}
        <UploadImage onUploadSuccess={handleUploadSuccess} />

        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit}
          disabled={!username || !bio || !profileImageId}
          sx={{ mt: 2 }}
        >
          Save Changes
        </Button>

        {error && (
          <Typography variant="body2" color="error" align="center" sx={{ mt: 2 }}>
            {error}
          </Typography>
        )}
      </Box>
    </Container>
  );
};

export default EditProfile;
