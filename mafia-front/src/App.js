import './App.css';
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home";
import Container from "react-bootstrap/Container";
import Header from "./components/Header";
import {useEffect, useState} from "react";
import LoginPage from "./pages/LoginPage";
import {useAuth} from "./context/AuthProvider";
import TestPage from "./pages/TestPage";

function App() {
  const [isInit, setInit] = useState(false);
  const {onAppInit, isLoggedIn} = useAuth();

  useEffect(() => {
    if(!isInit && onAppInit) {
      onAppInit(setInit);
    }
  }, [isInit, onAppInit]);

  return (
    isInit ?
      <div className="App">
        <Header isLoggedIn={isLoggedIn}/>
        <Container>
          <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/test" element={<TestPage/>}/>
          </Routes>
        </Container>
      </div>
      : 
      <div>LOADING...</div>
  );
}

export default App;
