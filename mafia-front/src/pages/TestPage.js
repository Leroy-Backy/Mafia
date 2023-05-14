import api from "../utils/Api";
import {useEffect, useState} from "react";

export default function TestPage() {
  const [res, setRes] = useState(null);
  
  useEffect(() => {
    api.get("/api/test").then(resp => {
      setRes(resp.data);
    });
  }, []);
  
  return (
    <div>
      {res ?
        <div>
          {res}
        </div>
        :
        <div>
          BRUH(
        </div>
      }
    </div>
  );
}