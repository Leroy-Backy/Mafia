import UserForm from "../components/UserForm";
import Card from "react-bootstrap/Card";

export default function CreateGuardPage() {
  return (
    <div>
      <Card className="m-auto mt-4" style={{width: '25rem'}}>
        <Card.Header style={{textAlign: "center", lineHeight: 2}}>
          New Guard
        </Card.Header>
        <Card.Body>
          <UserForm/>
        </Card.Body>
      </Card>
    </div>
  );
}