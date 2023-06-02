import UserCard from "../components/UserCard";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useAuth} from "../context/AuthProvider";
import {isManager} from "../utils/authUtils";
import api from "../utils/Api";
import LoadingSpinner from "../components/LoadingSpinner";

export default function ProfilePage() {
  const {id} = useParams();
  const [userId, setUserId] = useState(null);
  const [renderedUser, setRenderedUser] = useState(null);
  const {user} = useAuth();
  const navigation = useNavigate();
  const [rerender, setRerender] = useState(false);
  
  useEffect(() => {
    if(!navigation || !userId) {
      return;
    }
    api.get(`/api/user/${userId}`).then(res => {
      setRenderedUser(res.data);
    }).catch(err => {
      if(!err.response || err.response.status === 404) {
        navigation("/notfound");
      }

      if(err.response.status === 403) {
        navigation("/accessdenied")
      }
    });
  }, [userId, navigation, rerender]);
  
  useEffect(() => {
    if(user) {
      if (!id) {
        setUserId(user.id)
      } else {
        setUserId(id);
      }
    }
  }, [id, user]);

  return (
    renderedUser ?
      <div>
        <UserCard manager={isManager(renderedUser.role)} onUserEdit={() => setRerender(prev => !prev)} renderedUser={renderedUser}/>
      </div>
      :
      <LoadingSpinner/>
  );
}