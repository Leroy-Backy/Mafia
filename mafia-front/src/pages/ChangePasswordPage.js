import {useForm} from "react-hook-form";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import Alert from "react-bootstrap/Alert";
import Modal from "react-bootstrap/Modal";
import api from "../utils/Api";
import {useNavigate, useParams} from "react-router-dom";

export default function ChangePasswordPage() {
  
  const [showAlert, setShowAlert] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const {token} = useParams();
  const navigate = useNavigate();
  
  const {
    register,
    formState: {errors},
    handleSubmit,
    getValues
  } = useForm({
    defaultValues: {
      password: "",
      repeatPassword: ""
    }
  });
  
  const doesMatch = (password) => {
    return password === getValues("password") || "Passwords should match";
  }

  const onSubmit = ({password}) => {
    api.post("/api/auth/password/change", {
      token: token,
      newPassword: password
    }).then(res => {
      setShowModal(true);
    }).catch(err => {
      setShowAlert(true);
    });
  }

  return (
    <div>
      <Card className="m-auto mt-4" style={{ width: '25rem'}}>
        <Card.Header style={{ textAlign: "center", lineHeight: 2 }}>
          Authorization
        </Card.Header>
        <Card.Body>
          {showAlert && <Alert variant="danger">
            Error with password changing
          </Alert>}
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Form.Group className="mb-3" controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                {...register("password", {required: "Password is required"})}
              />
              {errors.password && (
                <Form.Text className="text-danger">
                  {errors.password.message}
                </Form.Text>
              )}
            </Form.Group>
            
            <Form.Group className="mb-3" controlId="formBasicRepeatPassword">
              <Form.Label>Repeat password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Repeat password"
                {...register("repeatPassword", {validate: doesMatch})}
              />
              {errors.repeatPassword && (
                <Form.Text className="text-danger">
                  {errors.repeatPassword.message}
                </Form.Text>
              )}
            </Form.Group>
            <Button variant="primary" type="submit">
              Change
            </Button>
          </Form>
        </Card.Body>
      </Card>
      
      <Modal show={showModal}>
        <Modal.Header>
          <Modal.Title>Success!!!</Modal.Title>
        </Modal.Header>
        <Modal.Body>Password was successfully changed!</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => {navigate("/login")}}>
            Login
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}