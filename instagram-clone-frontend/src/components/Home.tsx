import React, { useState, useEffect } from 'react';
import apiClient from '../api/axios';
import { Container, Typography, Box, CircularProgress, Alert } from '@mui/material';
import { useAuth } from '../context/AuthContext';

interface FeedItem {
  id: string;
  username: string;
  profilePictureUrl: string;
  imageUrl: string;
  caption: string;
  timestamp: string;
}

const Home: React.FC = () => {
  const { token } = useAuth();
  const [feed, setFeed] = useState<FeedItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchFeed = async () => {
      try {
        const response = await apiClient.get('/feed', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setFeed(response.data);
      } catch (error: any) {
        setError('Error fetching feed');
      } finally {
        setLoading(false);
      }
    };

    fetchFeed();
  }, [token]);

  if (loading) {
    return (
      <Container maxWidth="sm">
        <Box display="flex" justifyContent="center" mt={5}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="sm">
        <Alert severity="error" sx={{ mt: 2 }}>
          {error}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="sm">
      {feed.length === 0 ? (
        <Typography variant="h6" align="center" sx={{ mt: 2 }}>
          No posts available
        </Typography>
      ) : (
        feed.map((item) => (
          <Box key={item.id} sx={{ mb: 4, border: '1px solid #ccc', borderRadius: '8px', padding: '16px' }}>
            <Box display="flex" alignItems="center" mb={2}>
              <img src={item.profilePictureUrl} alt={item.username} style={{ width: '50px', borderRadius: '50%' }} />
              <Typography variant="h6" ml={2}>
                {item.username}
              </Typography>
            </Box>
            <img src={`http://localhost:8080/images/${item.imageUrl}`} alt={item.caption} style={{ width: '100%', borderRadius: '8px' }} />
            <Typography variant="body1" mt={2}>
              {item.caption}
            </Typography>
            <Typography variant="body2" color="textSecondary">
              {new Date(item.timestamp).toLocaleString()}
            </Typography>
          </Box>
        ))
      )}
    </Container>
  );
};

export default Home;
