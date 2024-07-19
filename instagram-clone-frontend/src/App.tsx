import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Signup from './components/Signup';
import Login from './components/Login';
import { AppBar, Tabs, Tab, Box, Container } from '@mui/material';
import { useState } from 'react';

const App: React.FC = () => {
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Router>
      <Container>
        <AppBar position="static">
          <Tabs value={value} onChange={handleChange} centered>
            <Tab label="Signup" component={Link} to="/signup" />
            <Tab label="Login" component={Link} to="/login" />
          </Tabs>
        </AppBar>
        <Box mt={3}>
          <Routes>
            <Route path="/signup" element={<Signup />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </Box>
      </Container>
    </Router>
  );
};

export default App;
