import React from "react";
import { Link } from "react-router-dom";
import Button from "react-bootstrap/Button";

const LandingPage = () => {
  return (
    <>
      <div className="flex flex-col justify-center items-center bg-black min-h-screen">
        <h1 className="mb-8 font-serif text-white text-center">Are you the examiner or examinee?</h1>
        <div className="grid place-items-center">
          <Link to="/examiner-signin">
            <Button variant="light" className="px-3 my-3">Examiner</Button>
          </Link>
          <p className="text-white my-2">OR</p>
          <Link to="/examinee-login">
            <Button variant="light" className="px-3 my-3">Examinee</Button>
          </Link>
        </div>
        <p className="mx-4 my-3 text-sm text-white font-serif">Note: The Examiner will have to login once signed in</p>
        <p className="mx-2 text-white text-sm font-serif">Also only those examinees created by the examiner will be able to login and give exam.</p>
      </div>
    </>
  );
};

export default LandingPage;
