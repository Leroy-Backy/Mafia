import Navbar from "react-bootstrap/Navbar";
import React from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import {Link} from "react-router-dom";

export default function Header({isLoggedIn}) {
  return (
    <Navbar collapseOnSelect bg="light" expand="sm">
      <Container>
        <Navbar.Brand className="fw-bold" as={Link} to="/" eventkey="/">Mafia</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/" eventKey="/">
              <div>Home</div>
            </Nav.Link>
            <Nav.Link as={Link} to="/login" eventKey="/login">
              <div>Login</div>
            </Nav.Link>
            <Nav.Link as={Link} to="/test" eventKey="/test">
              <div>Test</div>
            </Nav.Link>
          </Nav>
          <div>{!isLoggedIn ? <div className="text-danger">Not logged In</div> : <div className="text-success">Logged in</div> }</div>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}