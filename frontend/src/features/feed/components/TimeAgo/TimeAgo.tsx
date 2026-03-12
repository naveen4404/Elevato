import {
  useEffect,
  useState,
  type HTMLAttributes,
  type HtmlHTMLAttributes,
} from "react";
import { timeAgo } from "../../utils/date";
import classes from "./TimeAgo.module.scss";
import { useLocation } from "react-router-dom";
interface TimeAgoProps extends HTMLAttributes<HTMLDivElement> {
  date: string;
  edited?: boolean;
}

export function TimeAgo({ date, edited, className, ...others }: TimeAgoProps) {
  const [time, setTime] = useState(timeAgo(new Date(date)));
  const location = useLocation();

  useEffect(() => {
    const interval = setInterval(() => {
      setTime(timeAgo(new Date(date)));
    }, 1000);

    return () => clearInterval(interval);
  }, [date]);

  return (
    <div className={`${classes.root} ${className || ""} `} {...others}>
      <span>{time}</span>
      {edited ? <span> . Edited</span> : null}
    </div>
  );
}
