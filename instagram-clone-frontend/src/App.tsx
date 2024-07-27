import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Signup from './components/Signup';
import Login from './components/Login';
import EditProfile from './components/EditProfile';
import Home from './components/Home';
import { AppBar, Tabs, Tab, Box, Container, Button, Typography } from '@mui/material';
import { AuthProvider, useAuth } from './context/AuthContext';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Container>
          <NavTabs />
          <Box mt={3}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/signup" element={<Signup />} />
              <Route path="/login" element={<Login />} />
              <Route path="/edit-profile" element={<PrivateRoute component={EditProfile} />} />
            </Routes>
          </Box>
        </Container>
      </Router>
    </AuthProvider>
  );
};

const NavTabs: React.FC = () => {
  const { token, logout } = useAuth();
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <>
      <AppBar position="static">
        <Tabs value={value} onChange={handleChange} centered>
          <Tab label="Home" component={Link} to="/" />
          <Tab label="Signup" component={Link} to="/signup" />
          <Tab label="Login" component={Link} to="/login" />
          {token && <Tab label="Edit Profile" component={Link} to="/edit-profile" />}
        </Tabs>
      </AppBar>
      {token && (
        <Box mt={2} textAlign="center">
          <Button variant="contained" color="secondary" onClick={logout}>
            Logout
          </Button>
        </Box>
      )}
    </>
  );
};

const PrivateRoute: React.FC<{ component: React.ComponentType }> = ({ component: Component }) => {
  const { token } = useAuth();

  return token ? <Component /> : <Box textAlign="center" mt={5}><Typography variant="h6">You need to log in to access this page</Typography></Box>;
};

export default App;
