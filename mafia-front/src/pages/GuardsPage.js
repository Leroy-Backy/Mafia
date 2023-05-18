import {useEffect, useState} from "react";
import api from "../utils/Api";
import UserCard from "../components/UserCard";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row"
import LoadingSpinner from "../components/LoadingSpinner";

export default function GuardsPage() {
  const [data, setData] = useState(null);
  
  useEffect(() => {
    api.get(`/api/guard`).then(res => {
      setData(res.data);
    });
  }, []);

  return (
    data ?
      <div>
        <Row>
          {data.map(guard => (
            <Col key={guard.id} className="d-flex justify-content-center"  md={12} lg={6}>
              <UserCard user={guard}></UserCard>
            </Col>)
          )}
        </Row>
      </div>
      :
      <LoadingSpinner/>
  );
}