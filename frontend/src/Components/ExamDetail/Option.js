import React from "react";
import DeleteIcon from '@mui/icons-material/Delete';

const Option = ({ option, mcqId }) => {

  
  return (
    <>
      <div>
        
        
          <div className="mcq-option">
            <span>{option.optionText}</span>
            <input
              type="checkbox"
              checked={option.isCorrect}
            />
           <DeleteIcon/>

          </div>
        </div>
      
    </>
  );
};

export default Option;
