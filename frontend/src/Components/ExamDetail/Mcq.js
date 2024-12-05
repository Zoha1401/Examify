import React from "react";

const Mcq = ({ mcq }) => {
  return (
    <>
      <div className="flex">
        <div>{mcq.questionText}</div>
        <div className="flex">
          {mcq.options.map((option, index) => (
            <div key={option.optionId || index}>
              {option.optionText}{" "}
              {option.isCorrect && <strong>(Correct)</strong>}
            </div>
          ))}
        </div>
      </div>
    </>
      
  );
};

export default Mcq;
