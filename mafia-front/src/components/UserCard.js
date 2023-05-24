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

export default function UserCard({manager = false, renderedUser, onUserEdit}) {
  const [showModal, setShowModal] = useState(false);
  const {user} = useAuth();
  
  return (
    <>
      <Card className={`mt-4 m-auto`} style={{width: "25rem"}}>
        <div className="position-relative">
          {(isManager(user.role) || renderedUser.id === user.id) &&
            <Button className="position-absolute m-2" variant="primary" style={{right: 0}}
                    type="button" onClick={() => setShowModal(prev => !prev)}>
              Edit
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
    </>
  );
}