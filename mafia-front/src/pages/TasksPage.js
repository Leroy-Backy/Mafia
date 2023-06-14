import {useEffect, useState} from "react";
import api from "../utils/Api";
import LoadingSpinner from "../components/LoadingSpinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import TaskCard from "../components/TaskCard";
import Form from "react-bootstrap/Form";
import {taskStatusOptions} from "../utils/consts";
import Card from "react-bootstrap/Card";
import {Link} from "react-router-dom";
import * as Icon from "react-bootstrap-icons";
import {useAuth} from "../context/AuthProvider";
import {isManager} from "../utils/authUtils";

export default function TasksPage() {
  const [data, setData] = useState(null);
  const [status, setStatus] = useState("ALL");
  const {user} = useAuth();
  
  useEffect(() => {
    api.get( `/api/task`, {params: {status: status}}).then(res => {
      setData(res.data);
    });
  }, [status])

  const addNewTask = () => {
    return (
      <Card className="mt-4" style={{width: "25rem"}}>
        <Card.Body className="d-flex justify-content-center">
          <Link to="/tasks/new" className="add-button">
            <Icon.FileEarmarkPlus size={30} />
            <div className="ms-2">Add new Task</div>
          </Link>
        </Card.Body>
      </Card>
    );
  }
  
  return (
    data && user ?
      <div>
        <Form className="mt-4">
          <Form.Select value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="ALL">All</option>
            {taskStatusOptions.map((opt, idx) => (
              <option key={idx} value={opt.value}>{opt.label}</option>
            ))}
          </Form.Select>
        </Form>
        <Row>
          {isManager(user.role) &&
            <Col key="new" className="d-flex justify-content-center mb-4"  md={12} lg={6}>
              {addNewTask()}
            </Col>
          }
          {data.map(task => (
            <Col key={task.id} className="d-flex justify-content-center" md={12} lg={6}>
              <TaskCard renderedTask={task} min={true}/>
            </Col>
          ))}
        </Row>
      </div>
      :
      <LoadingSpinner/>
  );
}