import { useState, type SubmitEvent } from "react";
import { useNavigate } from "react-router-dom";
import { Box } from "../../components/box/Box";
import { Button } from "../../../../components/button/Button";
import { Input } from "../../../../components/input/Input";
import { request } from "../../../../utils/api";

export function PasswordReset() {
  const navigate = useNavigate();
  const [emailSent, setEmailSent] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const sendPasswordResetToken = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    const email = e.currentTarget.email.value;
    await request<void>({
      endpoint: `/api/v1/authentication/send-password-reset-token?email=${email}`,
      method: "PUT",
      onSuccess: () => {
        setErrorMessage("");
        setEmailSent(true);
        setEmail(email);
      },
      onFailure: (message) => {
        console.log(message);
        setErrorMessage(message);
      },
    });
    setIsLoading(false);
  };

  const resetPassword = async (e: SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);
    const code = e.currentTarget.code.value;
    const newPassword = e.currentTarget.newpassword.value;

    await request<void>({
      endpoint: `/api/v1/authentication/reset-password?newPassword=${newPassword}&token=${code}&email=${email}`,
      method: "PUT",
      onSuccess: () => {
        setErrorMessage("");
        navigate("/authentication/login");
      },
      onFailure: (message) => {
        console.log(message);
        setErrorMessage(message);
      },
    });
    setIsLoading(false);
  };

  return (
    <div>
      <Box>
        <h1>Reset password</h1>

        {!emailSent ? (
          <>
            <p>
              Enter your email and we'll send a verification code if it matches
              an existing LinkedIn account.
            </p>
            <form onSubmit={sendPasswordResetToken}>
              <Input label="Email" type="email" id="email" name="email"></Input>
              <p style={{ color: "red" }}>{errorMessage}</p>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? "..." : "Next"}
              </Button>

              <Button
                type="button"
                outline
                onClick={() => navigate("/login")}
                disabled={isLoading}
              >
                {isLoading ? "..." : "Back"}{" "}
              </Button>
            </form>
          </>
        ) : (
          <>
            <p>
              Enter the verification code sent to your email and your new
              password.
            </p>
            <form onSubmit={resetPassword}>
              <Input
                label="Reset code"
                type="text"
                id="code"
                name="code"
              ></Input>
              <Input
                label="New password"
                type="password"
                id="newpassword"
                name="newpassword"
              ></Input>
              <p style={{ color: "red" }}>{errorMessage}</p>
              <Button type="submit" disabled={isLoading}>
                {isLoading ? "..." : "Next"}
              </Button>
              <Button
                type="button"
                outline
                onClick={() => setEmailSent(false)}
                disabled={isLoading}
              >
                {isLoading ? "..." : "Back"}
              </Button>
            </form>
          </>
        )}
      </Box>
    </div>
  );
}
