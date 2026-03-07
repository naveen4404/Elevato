import { useState, type SubmitEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Box } from "../../components/box/Box";
import { Button } from "../../../../components/button/Button";
import { Input } from "../../../../components/input/Input";
import { Seperator } from "../../components/seperator/Seperator";
import { useAuthentication } from "../../contexts/AuthenticationContextProvider";
import classes from "./Signup.module.scss";

export function Signup() {
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { signup } = useAuthentication();

  const handleSignUp = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);
    const email = e.currentTarget.email.value;
    const password = e.currentTarget.password.value;
    try {
      await signup(email, password);
      navigate("/");
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("Something went wrong");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={classes.root}>
      <Box>
        <h1>Sign up</h1>
        <p>Make the most of your professional life.</p>
        <form onSubmit={handleSignUp}>
          <Input
            label="Email"
            type="email"
            id="email"
            name="email"
            onFocus={() => {
              setErrorMessage("");
            }}
          ></Input>
          <Input
            label="Password"
            type="password"
            id="password"
            name="password"
          ></Input>
          {errorMessage && <p className={classes.error}>{errorMessage}</p>}
          <p className={classes.disclaimer}>
            By clicking Agree & Join or Continue, you agree to Elevato's{" "}
            <a href="">User Agreement</a>, <a href="">Privacy Policy</a>, and{" "}
            <a href="">Cookie Policy</a>.
          </p>
          <Button outline={false} type="submit" disabled={isLoading}>
            {isLoading ? "Signing in..." : "Agree & join"}
          </Button>
        </form>
        <Seperator>Or</Seperator>
        <div className={classes.register}>
          Already on Elevato? <Link to="/authentication/login">Sign in</Link>
        </div>
      </Box>
    </div>
  );
}
