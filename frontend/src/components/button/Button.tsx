import type { ButtonHTMLAttributes } from "react";
import classes from "./Button.module.scss";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  outline?: boolean;
  size?: "small" | "medium" | "large";
};

export function Button({ outline, size, className, ...options }: ButtonProps) {
  return (
    <button
      {...options}
      className={`${classes.button} ${outline ? classes.outline : ""} ${classes[size || "large"]} ${className}`}
    ></button>
  );
}
