import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import Alert from "react-bootstrap/Alert";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {taskStatusOptions, taskTypeOptions} from "../utils/consts";
import LoadingSpinner from "./LoadingSpinner";
import Api from "../utils/Api";
import {getRequestForTaskForm} from "../utils/ApiHelper";
import {useAuth} from "../context/AuthProvider";
import {isManager} from "../utils/authUtils";

export default function TaskForm({create = true, onSuccess, renderedTask}) {
  const [error, setError] = useState(null);
  const [buttonDisabled, setButtonDisabled] = useState(false);
  const [success, setSuccess] = useState(null);
  const [guards, setGuards] = useState(null);
  const [points, setPoints] = useState(null);
  const {user} = useAuth();
  
  useEffect(() => {
    if(user) {
      if (create || isManager(user.role)) {
        Api.get("/api/guard").then(res => {
          setGuards(res.data);
        });
        Api.get("/api/point").then(res => {
          setPoints(res.data);
        });
      }
    }
  }, [user, create])
  
  const defaultValues = renderedTask ?
    renderedTask :
    {
      name: "",
      description: "",
      point: {
        id: ""
      },
      guard: {
        id: ""
      },
      taskType: taskTypeOptions[0].value,
      taskStatus: taskStatusOptions[0].value,
      resultDescription: ""
    }

  const {
    register,
    formState: {errors},
    handleSubmit,
    reset
  } = useForm({
    defaultValues: {...defaultValues}
  });

  const onSubmit = (taskFromForm) => {
    taskFromForm.createdAt = undefined;
    setButtonDisabled(true);
    setError(null);
    setSuccess(null);

    getRequestForTaskForm(create, taskFromForm, renderedTask ? renderedTask.id : undefined).then(res => {
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

  return (!create && !isManager(user.role)) || (guards && points) ?
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
        {isManager(user.role) &&
          <>
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

            <Form.Group className="mb-3" controlId="formBasicDesc">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                placeholder="Description"
                {...register("description", {
                  required: "Description is required",
                  minLength: {value: 3, message: "Min length is 3"}
                })}
              />
              {errors.description && (
                <Form.Text className="text-danger">
                  {errors.description.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicType">
              <Form.Label>Task Type</Form.Label>
              <Form.Select
                {...register("taskType", {
                  required: "Task Type is required"
                })}
              >
                {taskTypeOptions.map((opt, idx) => (
                  <option key={idx} value={opt.value}>{opt.label}</option>
                ))}
              </Form.Select>

              {errors.taskType && (
                <Form.Text className="text-danger">
                  {errors.taskType.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicGuard">
              <Form.Label>Guard</Form.Label>
              <Form.Select
                {...register("guard.id", {
                  required: "Guard is required"
                })}
              >
                {
                  guards.map(guard => (
                    <option value={guard.id} key={guard.id}>{guard.lastName} {guard.firstName}</option>
                  ))
                }
              </Form.Select>

              {errors.guard && errors.guard.id && (
                <Form.Text className="text-danger">
                  {errors.guard.id.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicPoint">
              <Form.Label>Point</Form.Label>
              <Form.Select
                {...register("point.id", {
                  required: "Point is required"
                })}
              >
                {
                  points.map(point => (
                    <option value={point.id} key={point.id}>{point.name}</option>
                  ))
                }
              </Form.Select>

              {errors.point && errors.point.id && (
                <Form.Text className="text-danger">
                  {errors.point.id.message}
                </Form.Text>
              )}
            </Form.Group>
          </>
        }

        {!create &&
          <>
            <Form.Group className="mb-3" controlId="formBasicStatus">
              <Form.Label>Task Status</Form.Label>
              <Form.Select
                {...register("taskStatus", {
                  required: "Task Status is required"
                })}
              >
                {taskStatusOptions.map((opt, idx) => (
                  <option key={idx} value={opt.value}>{opt.label}</option>
                ))}
              </Form.Select>

              {errors.taskStatus && (
                <Form.Text className="text-danger">
                  {errors.taskStatus.message}
                </Form.Text>
              )}
            </Form.Group>

            <Form.Group className="mb-3" controlId="formBasicDescRes">
              <Form.Label>Result Description</Form.Label>
              <Form.Control
                as="textarea"
                placeholder="Result Description"
                {...register("resultDescription")}
              />
            </Form.Group>
          </>
        }
        
        <Button variant="primary" type="submit" disabled={buttonDisabled}>
          {create ? "Create" : "Edit"}
        </Button>
      </Form>
    </div>
    :
    <LoadingSpinner/>
}