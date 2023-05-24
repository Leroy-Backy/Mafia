import {useForm} from "react-hook-form";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {useState} from "react";
import Alert from "react-bootstrap/Alert";
import {getRequestForUserForm} from "../utils/ApiHelper";
import {useAuth} from "../context/AuthProvider";
import {isManager} from "../utils/authUtils";
import {phoneRegex} from "../utils/consts";

export default function UserForm({create = true, guard = true, onSuccess, renderedUser}) {

  const [error, setError] = useState(null);
  const [buttonDisabled, setButtonDisabled] = useState(false);
  const [success, setSuccess] = useState(null);
  const {user} = useAuth();

  const defaultValues = renderedUser ? renderedUser : guard ?
    {
      firstName: "",
      lastName: "",
      email: "",
      phone: ""
    } :
    {
      firstName: "",
      lastName: "",
      email: "",
      phone: "",
      city: "",
      district: "",
    }

  const {
    register,
    formState: {errors},
    handleSubmit,
    reset
  } = useForm({
    defaultValues: {...defaultValues}
  });

  const onSubmit = (userFromForm) => {
    setButtonDisabled(true);
    setError(null);
    setSuccess(null);

    getRequestForUserForm(create, !guard, userFromForm, isManager(user.role), renderedUser ? renderedUser.id : undefined).then(res => {
      reset({...defaultValues});
      setSuccess("Success!");
      if (onSuccess) {
        onSuccess();
      }
    }).catch(err => {
      if (err.response && err.response.data) {
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
            {...register("firstName", {
              required: "First name is required",
              minLength: {value: 3, message: "Min length is 3"}
            })}
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
            {...register("lastName", {
              required: "Last name is required",
              minLength: {value: 3, message: "Min length is 3"}
            })}
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
            {...register("phone", {
              required: "Phone is required",
              minLength: {value: 6, message: "Min length is 9"},
              maxLength: {value: 15, message: "Max length is 15"},
              pattern: {value: phoneRegex, message: "Wrong symbols"}
            })}
          />
          {errors.phone && (
            <Form.Text className="text-danger">
              {errors.phone.message}
            </Form.Text>
          )}
        </Form.Group>

        {!guard &&
          <>
            <Form.Group className="mb-3" controlId="formBasiccity">
              <Form.Label>City</Form.Label>
              <Form.Control
                type="text"
                placeholder="City"
                {...register("city", {
                  required: "City is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.city && (
                <Form.Text className="text-danger">
                  {errors.city.message}
                </Form.Text>
              )}
            </Form.Group>
            <Form.Group className="mb-3" controlId="formBasicDistrict">
              <Form.Label>District</Form.Label>
              <Form.Control
                type="text"
                placeholder="District"
                {...register("district", {
                  required: "District is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.district && (
                <Form.Text className="text-danger">
                  {errors.district.message}
                </Form.Text>
              )}
            </Form.Group>
          </>
        }


        <Button variant="primary" type="submit" disabled={buttonDisabled}>
          {create ? "Create" : "Edit"}
        </Button>
      </Form>
    </div>
  );
}