import {useState} from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import CardField from "./CardField";
import PointForm from "./PointForm";
import {isManager} from "../utils/authUtils";
import {useAuth} from "../context/AuthProvider";

export default function PointCard({renderedPoint, onPointUpdate}) {
  const [showModal, setShowModal] = useState(false);
  const {user} = useAuth();
  
  const getMapsUrl = () => {
    if(renderedPoint.latitude && renderedPoint.longitude) {
      return `${renderedPoint.longitude},${renderedPoint.latitude}`;
    } else {
      return `${renderedPoint.address.street}+${renderedPoint.address.houseNumber},+${renderedPoint.address.city}`;
    }
  }

  return (
    <>
      <Card className={`mt-4 m-auto`} style={{width: "25rem"}}>
        <div className="position-relative">
          {isManager(user.role) && renderedPoint.managerId === user.id &&
            <Button className="position-absolute m-2" style={{right: 0}} variant="primary"
                    type="button" onClick={() => setShowModal(prev => !prev)}>
              Edit
            </Button>
          }
          <iframe title={renderedPoint.id} src={`https://maps.google.com/maps?q=${getMapsUrl()}&z=14&output=embed`} height="450" width="100%"></iframe>
        </div>
        <Card.Body>
          <Card.Title>
            {renderedPoint.name}
          </Card.Title>
          <CardField name="Price" value={renderedPoint.price}/>
          <Card.Subtitle className="text-muted mt-2">
            {">> Client <<"}
          </Card.Subtitle>
          <CardField name="Email" value={renderedPoint.client.firstName}/>
          <CardField name="Phone" value={renderedPoint.client.lastName}/>
          <CardField name="Email" value={renderedPoint.client.email}/>
          <CardField name="Phone" value={renderedPoint.client.phone}/>

          <Card.Subtitle className="text-muted mt-2">
            {">> Address <<"}
          </Card.Subtitle>
          <CardField name="City" value={renderedPoint.address.city}/>
          <CardField name="District" value={renderedPoint.address.district}/>
          <CardField name="Street" value={renderedPoint.address.street}/>
          <CardField name="House number" value={renderedPoint.address.houseNumber}/>
        </Card.Body>
      </Card>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Point</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <PointForm create={false} renderedPoint={renderedPoint} onSuccess={() => {
            setShowModal(false);
            if(onPointUpdate) {
              onPointUpdate();
            }
          }}/>
        </Modal.Body>
      </Modal>
    </>
  );
}