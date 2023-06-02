import Card from "react-bootstrap/Card";
import TaskForm from "../components/TaskForm";

export default function CreateTaskPage() {
  return (
    <Card className="m-auto mt-4" style={{width: '25rem'}}>
      <Card.Header style={{textAlign: "center", lineHeight: 2}}>
        New Task
      </Card.Header>
      <Card.Body>
        <TaskForm/>
      </Card.Body>
    </Card>
  );
}