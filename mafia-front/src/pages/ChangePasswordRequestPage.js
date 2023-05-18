import {useState} from "react";
import {useForm} from "react-hook-form";
import api from "../utils/Api";
import Card from "react-bootstrap/Card";
import Alert from "react-bootstrap/Alert";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

export default function ChangePasswordRequestPage() {
  const [showAlert, setShowAlert] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [buttonDisabled, setButtonDisabled] = useState(false);

  const {
    register,
    formState: {errors},
    handleSubmit
  } = useForm({
    defaultValues: {
      email: "",
    }
  });

  const onSubmit = ({email}) => {
    setButtonDisabled(true);
    api.post(`/api/auth/password/change/${email}`).then(res => {
      setShowModal(true);
    }).catch(err => {
      setShowAlert(true);
    }).finally(() => {
      setButtonDisabled(false);
    })
  }

  return (
    <div>
      <Card className="m-auto mt-4" style={{ width: '25rem'}}>
        <Card.Header style={{ textAlign: "center", lineHeight: 2 }}>
          Authorization
        </Card.Header>
        <Card.Body>
          {showAlert && <Alert variant="danger">
            Error!
          </Alert>}
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                placeholder="email"
                {...register("email", {required: "Email is required"})}
              />
              {errors.email && (
                <Form.Text className="text-danger">
                  {errors.email.message}
                </Form.Text>
              )}
            </Form.Group>
            
            <Button variant="primary" type="submit" disabled={buttonDisabled}>
              Send
            </Button>
          </Form>
        </Card.Body>
      </Card>

      <Modal show={showModal}>
        <Modal.Header>
          <Modal.Title>Success!!!</Modal.Title>
        </Modal.Header>
        <Modal.Body>We sent link for password changing to your email</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {setShowModal(false)}}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}