import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import {useForm} from "react-hook-form";
import {Alert} from "react-bootstrap";
import {useState} from "react";
import {useAuth} from "../context/AuthProvider";

export default function LoginPage() {
  
  const {
    register, 
    formState: {errors}, 
    handleSubmit
  } = useForm({
    defaultValues: {
      email: "",
      password: ""
    }
  });
  
  const [showAlert, setShowAlert] = useState(false);
  const {login} = useAuth();
  
  console.log(errors)
  
  const onSubmit = async ({email, password}) => {
    console.log("SUBMIT>>>", email, password);
    try {
      await login(email, password);
      setShowAlert(false);
    } catch (error) {
      console.log("Error>>>", error)
      setShowAlert(true);
    }
  }
  
  return (
    <div>
      <Card style={{ width: '25rem', margin: "auto", marginTop: "5rem" }}>
        <Card.Body>
          <Card.Title style={{ textAlign: "center", lineHeight: 3 }}>Authorization</Card.Title>
          {showAlert && <Alert variant="danger">
            Login failed, password or email is incorrect
          </Alert>}
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email" 
                placeholder="Enter email"
                {...register("email", {required: "Email is required"})}
              />
              {errors.email && (
                <Form.Text className="text-danger">
                  {errors.email.message}
                </Form.Text>
              )}
            </Form.Group>

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
            <Button variant="primary" type="submit">
              Login
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}