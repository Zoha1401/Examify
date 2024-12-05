import React from 'react';
import TestCase from './TestCase';

const ProgrammingQuestion = ({pq}) => {
  return (
    <>
    <div className='flex'>
        <h1>{pq.programmingQuestionText}</h1>
        <div className="flex">
          {pq.testCases && pq.testCases.map((t)=>(
            <TestCase key={t.testcaseId} testcase={t} programmingQuestionId={pq.programmingQuestionId}/> 
           
          ))}
        </div>
    </div>
    </>
  );
}

export default ProgrammingQuestion;
