import React from 'react'
import { Container, Nav, NavDropdown, Navbar } from 'react-bootstrap'
import { useAuth } from '../helpers/contexts/AuthContext'
import '../stylesheets/NavigationBar.css'
import { useLocation } from 'react-router-dom';

function NavigationBar() {
  const auth = useAuth();
  const location = useLocation();
  const user = auth?.user;

  const isSigninOrSignUp = location.pathname.startsWith('/sign')
  const handleLogout = ()=>{
    auth.logout();
  }

  return (
    <Navbar collapseOnSelect expand="lg" className="nav-bar navbar-dark">
      <Container>
        <Navbar.Brand className="text-white">QuizIT</Navbar.Brand>
        {!isSigninOrSignUp && <Navbar.Toggle aria-controls="responsive-navbar-nav" />}
        {!isSigninOrSignUp && <Navbar.Collapse id="responsive-navbar-nav">
          <Nav className="ms-auto">
            {user && <Nav.Link href={user.roleType === "ADMIN"? "/admin-dashboard" :"/user-dashboard"}>Dashboard</Nav.Link>}
            {user?.roleType === "USER" && <Nav.Link href="/quiz">Attempt Quiz</Nav.Link>}
            {user?.roleType === "ADMIN" && <Nav.Link href="/quiz/create">Create Quiz</Nav.Link>}
            {!user && <Nav.Link href="/signin">Sign in</Nav.Link>}
            {user && <NavDropdown title={user.name} id="collasible-nav-dropdown">
              <NavDropdown.Item onClick={()=> handleLogout()}>Logout</NavDropdown.Item>
            </NavDropdown>}
          </Nav>
        </Navbar.Collapse>}
      </Container>
    </Navbar>
  )
}

export default NavigationBar