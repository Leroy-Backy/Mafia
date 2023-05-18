import {useAuth} from "../context/AuthProvider";
import {Navigate, Outlet} from "react-router-dom";
import {isManager} from "./authUtils";

export default function ManagerRoutes() {
  const { isLoggedIn, user } = useAuth();

  return isLoggedIn && isManager(user.role) ? <Outlet/> : <Navigate to='/accessdenied' exact replace/>
}