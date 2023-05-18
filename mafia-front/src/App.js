import './App.css';
import {Navigate, Route, Routes} from "react-router-dom";
import ProfilePage from "./pages/ProfilePage";
import Container from "react-bootstrap/Container";
import Header from "./components/Header";
import {useEffect, useState} from "react";
import LoginPage from "./pages/LoginPage";
import {useAuth} from "./context/AuthProvider";
import NotFoundPage from "./pages/NotFoundPage";
import AccessDeniedPage from "./pages/AccessDeniedPage";
import ProtectRoutes from "./utils/ProtectedRoutes";
import ManagerRoutes from "./utils/ManagerRoutes";
import LogoutPage from "./pages/LogoutPage";
import GuardsPage from "./pages/GuardsPage";
import {isManager} from "./utils/authUtils";
import LoadingSpinner from "./components/LoadingSpinner";

function App() {
  const [isInit, setInit] = useState(false);
  const {onAppInit, isLoggedIn, user} = useAuth();

  useEffect(() => {
    if(!isInit && onAppInit) {
      onAppInit(setInit);
    }
  }, [isInit, onAppInit]);

  return (
    isInit ?
      <div className="App">
        <Header isLoggedIn={isLoggedIn} isManager={isLoggedIn && isManager(user.role)}/>
        <Container>
          <Routes>
            <Route element={<ProtectRoutes/>}>
              <Route path="/" element={<Navigate to="/user" replace />}/>
              <Route path="/user" element={<ProfilePage/>}/>
              <Route path="/user/:id" element={<ProfilePage/>}/>
              <Route element={<ManagerRoutes/>}>
                <Route path="/guards" element={<GuardsPage/>}/>
              </Route>
            </Route>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/logout" element={<LogoutPage/>}/>
            <Route path="/notfound" element={<NotFoundPage/>}/>
            <Route path="/accessdenied" element={<AccessDeniedPage/>}/>
            <Route path="*" element={<Navigate to="/notfound" replace />}/>
          </Routes>
        </Container>
      </div>
      :
      <LoadingSpinner/>
  );
}

export default App;
