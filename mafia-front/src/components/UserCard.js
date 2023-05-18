import Card from "react-bootstrap/Card";
import managerImg from "../images/manager.jpeg"
import guardImg from "../images/guard.jpg"

export default function UserCard({full = false, manager = false, user}) {
  return (
    <Card className={`mt-4 ${full ? "m-auto" : ""}`} style={{width: full ? "25rem" : "25rem"}}>
      <Card.Img variant="top" src={manager ? managerImg : guardImg}/>
      <Card.Body>
        <Card.Title>
          {user.firstName} {user.lastName}
        </Card.Title>
        <div className="d-flex flex-wrap">
          <div className="user-label">Email:</div>
          <div style={{flex: "1"}}>{user.email}</div>
        </div>
        <div className="d-flex">
          <div className="user-label">Phone:</div>
          <div>{user.phone}</div>
        </div>
        {user.city &&
          <div className="d-flex">
            <div className="user-label">City:</div>
            <div>{user.city}</div>
          </div>
        }
        {user.district &&
          <div className="d-flex">
            <div className="user-label">District:</div>
            <div>{user.district}</div>
          </div>
        }
      </Card.Body>
    </Card>
  );
}