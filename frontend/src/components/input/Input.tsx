import type { InputHTMLAttributes } from "react";
import classes from "./Input.module.scss";

type InputProps = InputHTMLAttributes<HTMLInputElement> & {
  label?: string;
  size?: "small" | "medium" | "large";
};

export function Input({ label, size, ...options }: InputProps) {
  return (
    <div className={`${classes.root} ${classes[size || "large"]}`}>
      <label>{label}</label>
      <input {...options} />
    </div>
  );
}
