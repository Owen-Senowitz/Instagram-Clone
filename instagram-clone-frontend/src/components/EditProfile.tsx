import React, { useState, useEffect } from 'react';
import apiClient from '../api/axios';
import { Container, TextField, Button, Typography, Box, Alert } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import ImageUpload from './ImageUpload';

interface UserProfile {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  bio: string;
  profilePictureUrl: string;
}

const EditProfile: React.FC = () => {
  const { token } = useAuth();
  const [profile, setProfile] = useState<UserProfile>({
    id: '',
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    bio: '',
    profilePictureUrl: ''
  });
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await apiClient.get('/user/profile', {
          headers: { Authorization: `Bearer ${token}` }
        });
        setProfile(response.data);
      } catch (error: any) {
        setMessage('Error fetching profile');
      }
    };

    fetchProfile();
  }, [token]);

  const handleUpdateProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await apiClient.put('/user/update-user', profile, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setMessage('Profile updated successfully');
    } catch (error: any) {
      setMessage('Error updating profile');
    }
  };

  return (
    <Container maxWidth="sm">
      <Box component="form" onSubmit={handleUpdateProfile} sx={{ mt: 3 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Edit Profile
        </Typography>
        <TextField
          label="Username"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.username}
          onChange={(e) => setProfile({ ...profile, username: e.target.value })}
          disabled
        />
        <TextField
          label="Email"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.email}
          onChange={(e) => setProfile({ ...profile, email: e.target.value })}
          disabled
        />
        <TextField
          label="First Name"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.firstName}
          onChange={(e) => setProfile({ ...profile, firstName: e.target.value })}
          required
        />
        <TextField
          label="Last Name"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.lastName}
          onChange={(e) => setProfile({ ...profile, lastName: e.target.value })}
          required
        />
        <TextField
          label="Bio"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.bio}
          onChange={(e) => setProfile({ ...profile, bio: e.target.value })}
          required
        />
        <TextField
          label="Profile Picture URL"
          variant="outlined"
          fullWidth
          margin="normal"
          value={profile.profilePictureUrl}
          onChange={(e) => setProfile({ ...profile, profilePictureUrl: e.target.value })}
          required
        />
        <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>
          Update Profile
        </Button>
        {message && (
          <Alert severity={message === 'Profile updated successfully' ? 'success' : 'error'} sx={{ mt: 2 }}>
            {message}
          </Alert>
        )}
      </Box>
      <ImageUpload/>
    </Container>
  );
};

export default EditProfile;
