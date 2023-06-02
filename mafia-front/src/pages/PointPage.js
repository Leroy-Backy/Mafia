import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import api from "../utils/Api";
import PointCard from "../components/PointCard";
import LoadingSpinner from "../components/LoadingSpinner";

export default function PointPage() {
  const {id} = useParams();
  const [renderedPoint, setRenderedPoint] = useState(null);
  const navigation = useNavigate();
  const [rerender, setRerender] = useState(false);
  
  useEffect(() => {
    if(id && navigation) {
      api.get(`/api/point/${id}`).then(res => {
        setRenderedPoint(res.data);
      }).catch(err => {
        if(!err.response || err.response.status === 404) {
          navigation("/notfound");
        }

        if(err.response.status === 403) {
          navigation("/accessdenied")
        }
      });
    }
  }, [id, rerender, navigation]);

  return (
    renderedPoint ?
      <div>
        <PointCard renderedPoint={renderedPoint} onPointUpdate={() => setRerender(prev => !prev)}/>
      </div>
      :
      <LoadingSpinner/>
  )
}