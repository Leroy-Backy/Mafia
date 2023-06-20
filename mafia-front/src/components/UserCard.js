import Card from "react-bootstrap/Card";
import managerImg from "../images/manager.jpeg"
import guardImg from "../images/guard.jpg"
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {useState} from "react";
import UserForm from "./UserForm";
import {isManager} from "../utils/authUtils";
import {useAuth} from "../context/AuthProvider";
import CardField from "./CardField";
import Api from "../utils/Api";
import {useNavigate} from "react-router-dom";

export default function UserCard({manager = false, renderedUser, onUserEdit}) {
  const [showModal, setShowModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const navigate = useNavigate();
  const {user} = useAuth();
  
  const deleteUser = () => {
    Api.delete("/api/guard/" + renderedUser.id).then(() => {
      navigate("/");
    }
    ).finally(() => {
      setShowDeleteModal(false);
    });
  }
  
  return (
    <>
      <Card className={`mt-4 m-auto`} style={{width: "25rem"}}>
        <div className="position-relative">
          {(isManager(user.role) || renderedUser.id === user.id) &&
            <Button className="position-absolute m-2" variant="primary" style={{right: 0, width: 75}}
                    type="button" onClick={() => setShowModal(prev => !prev)}>
              Edit
            </Button>
          }
          {(isManager(user.role) && !isManager(renderedUser.role) && renderedUser.managerId === user.id) &&
            <Button className="position-absolute m-2" variant="danger" style={{right: 0, top: 45}}
                    type="button" onClick={() => setShowDeleteModal(prev => !prev)}>
              Delete
            </Button>
          }
          <Card.Img variant="top" src={manager ? managerImg : guardImg}/>
        </div>
        <Card.Body>
          <Card.Title>
            {renderedUser.firstName} {renderedUser.lastName}
          </Card.Title>
          <CardField name="Email" value={renderedUser.email}/>
          <CardField name="Phone" value={renderedUser.phone}/>
          {renderedUser.city &&
            <CardField name="City" value={renderedUser.city}/>
          }
          {renderedUser.district &&
            <CardField name="District" value={renderedUser.district}/>
          }
        </Card.Body>
      </Card>
      
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit {manager ? "manager" : "guard"}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <UserForm create={false} guard={!manager} renderedUser={renderedUser} onSuccess={() => {
            setShowModal(false);
            if(onUserEdit) {
              onUserEdit();
            }
          }}/>
        </Modal.Body>
      </Modal>

      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Body>
          Are you sure you want to delete this user?
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={deleteUser}>Yes</Button>
          <Button onClick={() => setShowDeleteModal(false)}>No</Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}