import {useEffect, useState} from "react";
import api from "../utils/Api";
import UserCard from "../components/UserCard";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row"
import LoadingSpinner from "../components/LoadingSpinner";
import Card from "react-bootstrap/Card";
import * as Icon from "react-bootstrap-icons";
import {Link} from "react-router-dom";

export default function GuardsPage() {
  const [data, setData] = useState(null);
  
  const addNewCard = () => {
    return (
      <Card className="mt-4" style={{width: "25rem"}}>
        <Card.Body className="d-flex justify-content-center">
          <Link to="/guards/new" className="add-button">
            <Icon.PersonAdd size={30} />
            <div className="ms-2">Add new Guard</div>
          </Link>
        </Card.Body>
      </Card>
    );
  }
  
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
          <Col key="new" className="d-flex justify-content-center"  md={12} lg={6}>
            {addNewCard()}
          </Col>
        </Row>
      </div>
      :
      <LoadingSpinner/>
  );
}