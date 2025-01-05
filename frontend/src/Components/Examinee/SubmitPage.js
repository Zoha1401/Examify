import React from "react";

const SubmitPage = () => {
  localStorage.removeItem("token");
  return (
    <div className="items-center mt-3 p-6 text-center font-semibold shadow-lg bg-gray-100">
      Your exam is submitted.
    </div>
  );
};

export default SubmitPage;
