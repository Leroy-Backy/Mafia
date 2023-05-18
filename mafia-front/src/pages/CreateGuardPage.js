import {useForm} from "react-hook-form";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import {Alert} from "react-bootstrap";
import api from "../utils/Api";

export default function CreateGuardPage() {
  
  const [error, setError] = useState(null);
  const [buttonDisabled, setButtonDisabled] = useState(false);
  const [success, setSuccess] = useState(null);
  
  const defaultValues = {
    firstName: "",
    lastName: "",
    email: "",
    phone: ""
  }
  
  const {
    register,
    formState: {errors},
    handleSubmit,
    reset
  } = useForm({
    defaultValues: {...defaultValues}
  });

  const onSubmit = (user) => {
    setButtonDisabled(true);
    setError(null);
    setSuccess(null);
    api.post("/api/guard", user).then(res => {
      reset({...defaultValues});
      setSuccess("Success!");
    }).catch(err => {
      if(err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError("Unknow error!");
      }
    }).finally(() => {
      setButtonDisabled(false);
    })
  }
  
  return (
    <div>
      <Card className="m-auto mt-4" style={{ width: '25rem'}}>
        <Card.Header style={{ textAlign: "center", lineHeight: 2 }}>
          New Guard
        </Card.Header>
        <Card.Body>
          {error &&
            <Alert variant="danger">
              {error}
            </Alert>
          }
          {success &&
            <Alert variant="success">
              {success}
            </Alert>
          }
          <Form onSubmit={handleSubmit(onSubmit)}>
            <Form.Group className="mb-3" controlId="formBasicFirstName">
              <Form.Label>First name</Form.Label>
              <Form.Control
                type="text"
                placeholder="First name"
                {...register("firstName", {required: "First name is required", minLength: {value: 3, message: "Min length is 3"}})}
              />
              {errors.firstName && (
                <Form.Text className="text-danger">
                  {errors.firstName.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicLastName">
              <Form.Label>Last name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Last name"
                {...register("lastName", {required: "Last name is required", minLength: {value: 3, message: "Min length is 3"}})}
              />
              {errors.lastName && (
                <Form.Text className="text-danger">
                  {errors.lastName.message}
                </Form.Text>
              )}
            </Form.Group>

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

            <Form.Group className="mb-3" controlId="formBasicPhone">
              <Form.Label>Phone</Form.Label>
              <Form.Control
                type="text"
                placeholder="Phone"
                {...register("phone", {required: "Phone is required", minLength: {value: 9, message: "Min length is 9"}})}
              />
              {errors.phone && (
                <Form.Text className="text-danger">
                  {errors.phone.message}
                </Form.Text>
              )}
            </Form.Group>
            
            <Button variant="primary" type="submit" disabled={buttonDisabled}>
              Create
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}