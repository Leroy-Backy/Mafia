import UserCard from "../components/UserCard";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useAuth} from "../context/AuthProvider";
import {isManager} from "../utils/authUtils";
import api from "../utils/Api";
import LoadingSpinner from "../components/LoadingSpinner";

export default function ProfilePage() {
  const {id} = useParams();
  const [renderedUser, setRenderedUser] = useState(null);
  const {user} = useAuth();
  const navigation = useNavigate();
  
  useEffect(() => {
    if(!user) {
      return;
    }
    if(!id) {
      setRenderedUser(user);
    } else {
      api.get(`/api/user/${id}`).then(res => {
        setRenderedUser(res.data);
      }).catch(err => {
        if(!err.response || err.response.status === 404) {
          navigation("/notfound");
        }
        
        if(err.response.status === 403) {
          navigation("")
        }
      });
    }
  }, [user]);

  return (
    renderedUser ?
      <div>
        <UserCard full manager={isManager(renderedUser.role)} user={renderedUser}/>
      </div>
      :
      <LoadingSpinner/>
  );
}