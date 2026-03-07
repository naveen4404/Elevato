import { useState, type SubmitEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Box } from "../../components/box/Box";
import { Button } from "../../../../components/button/Button";
import { Input } from "../../../../components/input/Input";
import { Seperator } from "../../components/seperator/Seperator";
import { useAuthentication } from "../../contexts/AuthenticationContextProvider";
import classes from "./Login.module.scss";

export function Login() {
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuthentication();
  const doLogin = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);
    const email = e.currentTarget.email.value;
    const password = e.currentTarget.password.value;
    try {
      await login(email, password);
      const destination = location.state?.from || "/";
      navigate(destination);
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("Unknown error occured");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={classes.root}>
      <Box>
        <h1>Sign in</h1>
        <p>Stay updated on your professional world.</p>
        <form onSubmit={doLogin}>
          <Input
            label="Email"
            type="email"
            id="email"
            required
            onFocus={() => setErrorMessage("")}
          ></Input>
          <Input
            label="Password"
            type="password"
            id="password"
            required
            onFocus={() => setErrorMessage("")}
          ></Input>
          {errorMessage && <div className={classes.error}>{errorMessage}</div>}
          <Button type="submit" outline={false} disabled={isLoading}>
            {isLoading ? "Signing in..." : "sign in"}
          </Button>
        </form>
        <Link to="/authentication/request-password-reset">
          Forgot password?
        </Link>
      </Box>
      <Seperator>Or</Seperator>
      <div className={classes.register}>
        New to Elevato? {<Link to="/authentication/signup">Join now</Link>}
      </div>
    </div>
  );
}
