import PointForm from "../components/PointForm";
import Card from "react-bootstrap/Card";

export default function CreatePointPage() {
  return (
    <Card className="m-auto mt-4" style={{width: '25rem'}}>
      <Card.Header style={{textAlign: "center", lineHeight: 2}}>
        New Point
      </Card.Header>
      <Card.Body>
        <PointForm/>
      </Card.Body>
    </Card>
  )
}