import {useState} from "react";
import {useForm} from "react-hook-form";
import {getRequestForPointForm} from "../utils/ApiHelper";
import Card from "react-bootstrap/Card";
import Alert from "react-bootstrap/Alert";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

export default function PointForm({create = true, onSuccess, renderedPoint}) {
  const [error, setError] = useState(null);
  const [buttonDisabled, setButtonDisabled] = useState(false);
  const [success, setSuccess] = useState(null);

  const defaultValues = renderedPoint ? 
    renderedPoint :
    {
      name: "",
      price: 0,
      client: {
        firstName: "",
        lastName: "",
        email: "",
        phone: "" 
      },
      address: {
        city: "",
        district: "",
        street: "",
        houseNumber: ""
      },
      longitude: "",
      latitude: ""
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
    console.log("SUBMIT>>>", userFromForm)
    setButtonDisabled(true);
    setError(null);
    setSuccess(null);

    getRequestForPointForm(create, userFromForm, renderedPoint ? renderedPoint.id : undefined).then(res => {
      reset({...defaultValues});
      setSuccess("Success!");
      if (onSuccess) {
        onSuccess();
      }
    }).catch(err => {
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError("Unknown error!");
      }
    }).finally(() => {
      setButtonDisabled(false);
    });
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
        <Form.Group className="mb-3" controlId="formBasicName">
          <Form.Label>Name</Form.Label>
          <Form.Control
            type="text"
            placeholder="Name"
            {...register("name", {
              required: "Name is required",
              minLength: {value: 3, message: "Min length is 3"}
            })}
          />
          {errors.name && (
            <Form.Text className="text-danger">
              {errors.name.message}
            </Form.Text>
          )}
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicPrice">
          <Form.Label>Price</Form.Label>
          <Form.Control
            type="text"
            placeholder="Price"
            {...register("price", {
              required: "Price is required",
              pattern: {value: /^\d*$/, message: "Should be a number"}
            })}
          />
          {errors.price && (
            <Form.Text className="text-danger">
              {errors.price.message}
            </Form.Text>
          )}
        </Form.Group>
        <Card>
          <Card.Header>Client</Card.Header>
          <Card.Body>
            <Form.Group className="mb-3" controlId="formBasicFirstName">
              <Form.Label>First name</Form.Label>
              <Form.Control
                type="text"
                placeholder="First name"
                {...register("client.firstName", {
                  required: "First name is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.client && errors.client.firstName && (
                <Form.Text className="text-danger">
                  {errors.client.firstName.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicLastName">
              <Form.Label>Last name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Last name"
                {...register("client.lastName", {
                  required: "Last name is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.client && errors.client.lastName && (
                <Form.Text className="text-danger">
                  {errors.client.lastName.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                {...register("client.email", {required: "Email is required"})}
              />
              {errors.client && errors.client.email && (
                <Form.Text className="text-danger">
                  {errors.client.email.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPhone">
              <Form.Label>Phone</Form.Label>
              <Form.Control
                type="text"
                placeholder="Phone"
                {...register("client.phone", {
                  required: "Phone is required",
                  minLength: {value: 6, message: "Min length is 9"},
                  maxLength: {value: 15, message: "Max length is 15"},
                  pattern: {value: /^\+?\d+$/, message: "Wrong symbols"}
                })}
              />
              {errors.client && errors.client.phone && (
                <Form.Text className="text-danger">
                  {errors.client.phone.message}
                </Form.Text>
              )}
            </Form.Group>
          </Card.Body>
        </Card>

        <Card className="mt-3 mb-3">
          <Card.Header>Address</Card.Header>
          <Card.Body>
            <Form.Group className="mb-3" controlId="formBasicCity">
              <Form.Label>City</Form.Label>
              <Form.Control
                type="text"
                placeholder="City"
                {...register("address.city", {
                  required: "City is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.address && errors.address.city && (
                <Form.Text className="text-danger">
                  {errors.address.city.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicDistrict">
              <Form.Label>District</Form.Label>
              <Form.Control
                type="text"
                placeholder="District"
                {...register("address.district", {
                  required: "District is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.address && errors.address.district && (
                <Form.Text className="text-danger">
                  {errors.address.district.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicStreet">
              <Form.Label>Street</Form.Label>
              <Form.Control
                type="text"
                placeholder="Street"
                {...register("address.street", {
                  required: "Street is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.address && errors.address.street && (
                <Form.Text className="text-danger">
                  {errors.address.street.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicHouseNumber">
              <Form.Label>House number</Form.Label>
              <Form.Control
                type="text"
                placeholder="House number"
                {...register("address.houseNumber", {
                  required: "House number is required",
                  minLength: {value: 1, message: "Min length is 1"}
                })}
              />
              {errors.address && errors.address.houseNumber && (
                <Form.Text className="text-danger">
                  {errors.address.houseNumber.message}
                </Form.Text>
              )}
            </Form.Group>
          </Card.Body>
        </Card>

        <Form.Group className="mb-3" controlId="formBasicLongitude">
          <Form.Label>Longitude</Form.Label>
          <Form.Control
            type="text"
            placeholder="Longitude"
            {...register("longitude", {
              required: "Longitude is required",
              pattern: {value: /^[0-9]{2}.[0-9]{3,7}$/, message: "Wrong format, example: 10.123456"}
            })}
          />
          {errors.longitude && (
            <Form.Text className="text-danger">
              {errors.longitude.message}
            </Form.Text>
          )}
        </Form.Group>

        <Form.Group className="mb-3" controlId="formBasicLatitude">
          <Form.Label>Latitude</Form.Label>
          <Form.Control
            type="text"
            placeholder="Latitude"
            {...register("latitude", {
              required: "Latitude is required",
              pattern: {value: /^[0-9]{2}.[0-9]{3,7}$/, message: "Wrong format"}
            })}
          />
          {errors.latitude && (
            <Form.Text className="text-danger">
              {errors.latitude.message}
            </Form.Text>
          )}
        </Form.Group>

        <Button variant="primary" type="submit" disabled={buttonDisabled}>
          {create ? "Create" : "Edit"}
        </Button>
      </Form>
    </div>
  );
}