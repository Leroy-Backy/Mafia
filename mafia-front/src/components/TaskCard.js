import Card from "react-bootstrap/Card";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import {statusColor, taskStatusOptions, taskTypeOptions} from "../utils/consts";
import * as Icon from "react-bootstrap-icons";
import Modal from "react-bootstrap/Modal";
import {useState} from "react";
import TaskForm from "./TaskForm";

export default function TaskCard({renderedTask, onTaskUpdate, min = false}) {
  const [showModal, setShowModal] = useState(false);
  
  const getMapsUrl = () => {
    if(renderedTask.point.latitude && renderedTask.point.longitude) {
      return `${renderedTask.point.longitude},${renderedTask.point.latitude}`;
    } else {
      return `${renderedTask.point.address.street}+${renderedTask.point.address.houseNumber},+${renderedTask.point.address.city}`;
    }
  }

  const editTask = () => {
    return (
      <Card className="mt-4 mb-4 m-auto" style={{width: "25rem", cursor: "pointer"}} onClick={() => {
        setShowModal(true);
      }}>
        <Card.Body className="d-flex justify-content-center">
          <div className="add-button">
            <Icon.PencilSquare size={30} />
            <div className="ms-2">Edit Task</div>
          </div>
        </Card.Body>
      </Card>
    );
  }
  
  return (
    <>
      <Card className="mt-4 m-auto" style={{width: "25rem"}}>
        <div className="position-relative">
          <Button className="position-absolute m-2" style={{right: 0}} variant={statusColor[renderedTask.taskStatus]}
                  type="button" disabled={true}>
            {renderedTask.taskStatus ? taskStatusOptions.find(opt => opt.value === renderedTask.taskStatus).label : "Unknown"}
          </Button>
          <iframe title={renderedTask.id} src={`https://maps.google.com/maps?q=${getMapsUrl()}&z=14&output=embed`} height="450" width="100%"></iframe>
        </div>
        <Card.Body>
          <div className="text-muted">
            {new Date(renderedTask.createdAt).toLocaleString("pl")}
          </div>
          <Card.Title>
            <Link to={`/tasks/${renderedTask.id}`} className="text-dark text-decoration-none">
              {renderedTask.name} ({taskTypeOptions.find(opt => opt.value === renderedTask.taskType).label})
            </Link>
          </Card.Title>
          
          <Card.Subtitle className="text-muted mt-2">
            Point:
          </Card.Subtitle>
          <Card>
            <Card.Body className="p-1">
              <Card.Text>
                <Link to={`/points/${renderedTask.point.id}`} className="text-dark">
                  {renderedTask.point.name}
                </Link>
              </Card.Text>
            </Card.Body>
          </Card>

          <Card.Subtitle className="text-muted mt-2">
            Guard:
          </Card.Subtitle>
          <Card>
            <Card.Body className="p-1">
              <Card.Text>
                <Link to={`/user/${renderedTask.guard.id}`} className="text-dark">
                  {renderedTask.guard.lastName} {renderedTask.guard.firstName}
                </Link>
              </Card.Text>
            </Card.Body>
          </Card>

          <Card.Subtitle className="text-muted mt-2">
            Manager:
          </Card.Subtitle>
          <Card>
            <Card.Body className="p-1">
              <Card.Text>
                <Link to={`/user/${renderedTask.manager.id}`} className="text-dark">
                  {renderedTask.manager.lastName} {renderedTask.manager.firstName}
                </Link>
              </Card.Text>
            </Card.Body>
          </Card>
        </Card.Body>
      </Card>
      
      {!min &&
        <>
          <Card className="mt-2 m-auto" style={{width: "25rem"}}>
            <Card.Body >
              <Card.Subtitle className="text-muted mt-2">
                Description:
              </Card.Subtitle>
              {renderedTask.description}
            </Card.Body>
          </Card>
          {renderedTask.resultDescription &&
            <Card className="mt-2 m-auto" style={{width: "25rem"}}>
              <Card.Body >
                <Card.Subtitle className="text-muted mt-2">
                  Result Description:
                </Card.Subtitle>
                {renderedTask.resultDescription}
              </Card.Body>
            </Card>
          }
          {editTask()}

          <Modal show={showModal} onHide={() => setShowModal(false)}>
            <Modal.Header closeButton>
              <Modal.Title>Edit Task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <TaskForm create={false} renderedTask={renderedTask} onSuccess={() => {
                setShowModal(false);
                if(onTaskUpdate) {
                  onTaskUpdate();
                }
              }}/>
            </Modal.Body>
          </Modal>
        </>
      }
    </>
  );
}