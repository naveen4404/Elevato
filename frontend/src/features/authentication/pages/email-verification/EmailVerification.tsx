import { useState, type SubmitEvent } from "react";
import { useNavigate } from "react-router-dom";
import { Box } from "../../components/box/Box";
import { Button } from "../../../../components/button/Button";
import { Input } from "../../../../components/input/Input";
import classes from "./EmailVerification.module.scss";
import { request } from "../../../../utils/api";
import { useAuthentication } from "../../contexts/AuthenticationContextProvider";
export function EmailVerification() {
  const [message, setMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const { user, setUser } = useAuthentication();
  const sendEmailVerificationToken = async () => {
    setMessage("");
    await request<void>({
      endpoint: "/api/v1/authentication/send-email-verification-token",
      onSuccess: () => {
        setErrorMessage("");
        setMessage("Code sent successfully. Please check your email.");
      },
      onFailure: (message) => {
        setErrorMessage(message);
      },
    });
    setIsLoading(false);
  };

  const validateEmail = async (code: string) => {
    setMessage("");
    await request<void>({
      endpoint: `/api/v1/authentication/validate-email-verification-token?token=${code}`,
      onSuccess: () => {
        setErrorMessage("");
        setUser({ ...user!, emailVerified: true });
        navigate("/");
      },
      onFailure: (message) => {
        setErrorMessage(message);
      },
    });
    setIsLoading(false);
  };

  const handleValidateEmail = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    const code = e.currentTarget.code.value;
    await validateEmail(code);
  };

  return (
    <div className={classes.root}>
      <Box>
        <h1>Verify your email</h1>
        <p>
          Only one step left to complete your registration. Verify your email
          address.
        </p>
        <form onSubmit={handleValidateEmail}>
          <Input
            label="Verification code"
            type="text"
            name="code"
            id="code"
          ></Input>
          {message && <p className={classes.success}>{message}</p>}
          {errorMessage && <p className={classes.error}>{errorMessage}</p>}
          <Button type="submit" disabled={isLoading}>
            {isLoading ? "..." : "Validate email"}
          </Button>
          <Button
            type="button"
            outline
            onClick={() => {
              sendEmailVerificationToken();
            }}
          >
            {isLoading ? "..." : "Send again"}
          </Button>
        </form>
      </Box>
    </div>
  );
}
