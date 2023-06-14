import {useEffect, useState} from "react";
import api from "../utils/Api";
import {Link} from "react-router-dom";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";

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
    <div>
      <h1 style={{textAlign: "center"}}>Reports</h1>

      <Form className="mt-4">
        <Form.Control type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)}>
        </Form.Control>
        <Form.Control type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)}>
        </Form.Control>
        <Button 
          onClick={generatePdf}
          disabled={!(startDate && startDate !== '' && endDate && endDate !== '')}
        >
          generate
        </Button>
      </Form>
      
      {reports.map(rep => (
        <Card key={rep}>
          <Link to={"http://localhost:8080/api/report/download/" + rep}>{rep}</Link>
        </Card>
      ))}
    </div>
  );
}