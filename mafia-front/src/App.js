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
import CreateGuardPage from "./pages/CreateGuardPage";
import ChangePasswordPage from "./pages/ChangePasswordPage";
import ChangePasswordRequestPage from "./pages/ChangePasswordRequestPage";
import PointsPage from "./pages/PointsPage";
import CreatePointPage from "./pages/CreatePointPage";
import CreateTaskPage from "./pages/CreateTaskPage";
import PointPage from "./pages/PointPage";
import TaskPage from "./pages/TaskPage";
import TasksPage from "./pages/TasksPage";
import ReportsPage from "./pages/ReportsPage";

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
              <Route path="/points/:id" element={<PointPage/>}/>
              <Route path="/tasks">
                <Route index element={<TasksPage/>}/>
                <Route path=":id" element={<TaskPage/>}/>
              </Route>
              <Route element={<ManagerRoutes/>}>
                <Route path="/guards">
                  <Route index element={<GuardsPage/>}/>
                  <Route path="new" element={<CreateGuardPage/>}/>
                </Route>
                <Route path="/points">
                  <Route index element={<PointsPage/>}/>
                  <Route path="new" element={<CreatePointPage/>}/>
                </Route>
                <Route path="/tasks/new" element={<CreateTaskPage/>}/>
                <Route path="/reports" element={<ReportsPage/>}/>
              </Route>
            </Route>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/logout" element={<LogoutPage/>}/>
            <Route path="/changepassword" element={<ChangePasswordRequestPage/>}/>
            <Route path="/changepassword/:token" element={<ChangePasswordPage/>}/>
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
