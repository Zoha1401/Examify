import React from "react";

const TestCase = ({ testcase, programmingQuestionId}) => {
  return (
    <>
      <div>{testcase.input}</div>
      <div>{testcase.expectedOutput}</div>
    </>
  );
};

export default TestCase;
