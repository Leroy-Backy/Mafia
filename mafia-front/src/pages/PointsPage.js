import {useEffect, useState} from "react";
import Card from "react-bootstrap/Card";
import {Link} from "react-router-dom";
import * as Icon from "react-bootstrap-icons";
import api from "../utils/Api";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import LoadingSpinner from "../components/LoadingSpinner";
import PointCard from "../components/PointCard";

export default function PointsPage() {
  const [data, setData] = useState(null);
  const [rerender, setRerender] = useState(false);

  const addNewPoint = () => {
    return (
      <Card className="mt-4" style={{width: "25rem"}}>
        <Card.Body className="d-flex justify-content-center">
          <Link to="/points/new" className="add-button">
            <Icon.PlusCircle size={30} />
            <div className="ms-2">Add new Point</div>
          </Link>
        </Card.Body>
      </Card>
    );
  }

  useEffect(() => {
    api.get(`/api/point`).then(res => {
      setData(res.data);
    });
  }, [rerender]);

  return (
    data ?
      <div>
        <Row>
          {data.map(point => (
            <Col key={point.id} className="d-flex justify-content-center"  md={12} lg={6}>
              <PointCard renderedPoint={point} onPointUpdate={() => setRerender(prev => !prev)} />
            </Col>)
          )}
          <Col key="new" className="d-flex justify-content-center"  md={12} lg={6}>
            {addNewPoint()}
          </Col>
        </Row>
      </div>
      :
      <LoadingSpinner/>
  );
}