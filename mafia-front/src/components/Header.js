import Navbar from "react-bootstrap/Navbar";
import React from "react";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import {Link} from "react-router-dom";

export default function Header({isLoggedIn, isManager}) {
  return (
    <Navbar collapseOnSelect bg="light" expand="sm">
      <Container>
        <Navbar.Brand className="fw-bold" as={Link} to="/" eventkey="/">Mafia</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/user" eventKey="/user">
              <div>Profile</div>
            </Nav.Link>
            {isManager &&
              <>
                <Nav.Link as={Link} to="/guards" eventKey="/guards">
                  <div>Guards</div>
                </Nav.Link>
                <Nav.Link as={Link} to="/points" eventKey="/points">
                  <div>Points</div>
                </Nav.Link>
                <Nav.Link as={Link} to="/tasks" eventKey="/tasks">
                  <div>Tasks</div>
                </Nav.Link>
              </>
            }
          </Nav>
          <div>{isLoggedIn ? 
            <Nav.Link as={Link} to="/logout" eventKey="/logout">
              <div>Logout</div>
            </Nav.Link>
            :
            <Nav.Link as={Link} to="/login" eventKey="/login">
              <div>Login</div>
            </Nav.Link>
          }</div>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}