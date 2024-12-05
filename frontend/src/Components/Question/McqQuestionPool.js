import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button } from "react-bootstrap";

const McqQuestionPool = () => {
    const examId=useParams();
  return (
    <div>
      <Link to="/addMcqQuestion"><Button>Add new MCQ</Button></Link>
    </div>
  );
}

export default McqQuestionPool;
