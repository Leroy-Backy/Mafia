import {useEffect, useState} from "react";
import api from "../utils/Api";
import {Link} from "react-router-dom";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import * as Icon from "react-bootstrap-icons";

export default function ReportsPage() {
  const [reports, setReports] = useState([]);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [rerender, setRerender] = useState(false);
  
  useEffect(() => {
    api.get(`/api/report`).then(res => {
      setReports(res.data);
    });
  }, [rerender]);
  
  useEffect(() => {
    console.log("SYTART DATE>>>", startDate)
  }, [startDate])
  
  const generatePdf = () => {
    api.post(`/api/report?from=${startDate}&to=${endDate}`).then(res => {
      setRerender(prev => !prev);
    }).catch(() => {
      alert("ERROR!")
    });
  }
  
  return (
    <div style={{marginBottom: 15, marginTop: 15}}>
      <h1 style={{textAlign: "center"}}>Reports</h1>

      <Form className="mt-4">
        
        <Row style={{textAlign: "center"}}>
          <Col>
            <Form.Control type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)}>
            </Form.Control>
          </Col>
          <Col>
            <Form.Control type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)}>
            </Form.Control>
          </Col>
          <Col>
            <Button
              style={{width: "100%"}}
              onClick={generatePdf}
              disabled={!(startDate && startDate !== '' && endDate && endDate !== '')}
            >
              generate
            </Button>
          </Col>
         
        </Row>
      </Form>
      
      {reports.map(rep => (
        <Card style={{marginTop: 10}} key={rep}>
          <Card.Body>
            <Icon.FileEarmarkPlus size={30} style={{marginRight: 15}}/>
            <Link style={{textDecoration: "none"}} to={"http://localhost:8080/api/report/download/" + rep}>{rep}</Link>
          </Card.Body>
        </Card>
      ))}
    </div>
  );
}