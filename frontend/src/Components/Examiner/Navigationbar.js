import React from 'react';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

const Navigationbar = () => {
  return (
    <>
      <Navbar bg="dark" data-bs-theme="dark">
        <Container>
          <Navbar.Brand href="#home">Examify</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/examiner-dashboard">Home</Nav.Link>
            <Nav.Link href="/manage-examinee">Examinees</Nav.Link>
          </Nav>
        </Container>
        <Button variant="primary" className='d-flex mx-4'>Logout</Button>
      </Navbar> 
      
      </>
  );
}

export default Navigationbar;