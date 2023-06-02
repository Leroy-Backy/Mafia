import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import api from "../utils/Api";
import LoadingSpinner from "../components/LoadingSpinner";
import TaskCard from "../components/TaskCard";

export default function TaskPage() {
  const {id} = useParams();
  const [renderedTask, setRenderedTask] = useState(null);
  const navigation = useNavigate();
  const [rerender, setRerender] = useState(false);

  useEffect(() => {
    if(id && navigation) {
      api.get(`/api/task/${id}`).then(res => {
        setRenderedTask(res.data);
      }).catch(err => {
        if(!err.response || err.response.status === 404) {
          navigation("/notfound");
        }

        if(err.response.status === 403) {
          navigation("/accessdenied")
        }
      });
    }
  }, [id, navigation, rerender]);
  
  return renderedTask ?
    <div>
      <TaskCard renderedTask={renderedTask} onTaskUpdate={() => setRerender(prev => !prev)}/>
    </div>
    :
    <LoadingSpinner/>
}