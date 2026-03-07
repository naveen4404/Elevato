import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Input } from "../../../../components/input/Input";
import {
  useAuthentication,
  type User,
} from "../../../authentication/contexts/AuthenticationContextProvider";
import { TimeAgo } from "../TimeAgo/TimeAgo";
import classes from "./CommentCard.module.scss";

export interface Comment {
  id: number;
  content: string;
  user: User;
  createdAt: string;
  updatedAt?: string;
}

interface CommentProps {
  comment: Comment;
  deleteComment: (commentId: number) => Promise<void>;
  editComment: (commentId: number, content: string) => Promise<void>;
}

export function CommentCard({
  comment,
  deleteComment,
  editComment,
}: CommentProps) {
  const navigate = useNavigate();
  const [showActions, setShowActions] = useState(false);
  const [editing, setEditing] = useState(false);
  const [commentContent, setCommentContent] = useState(comment.content);
  const { user } = useAuthentication();
  return (
    <div key={comment.id} className={classes.root}>
      {!editing ? (
        <>
          <div className={classes.header}>
            <button
              onClick={() => {
                navigate(`/profile/${comment.user.id}`);
              }}
              className={classes.user}
            >
              <img
                className={classes.avatar}
                src={comment.user.profilePicture || "/avatar.png"}
                alt=""
              />
              <div>
                <div className={classes.name}>
                  {comment.user.firstName + " " + comment.user.lastName}
                </div>
                <div className={classes.title}>
                  {"student" + " at " + comment.user.college}
                </div>
                <div className={classes.date}>
                  <TimeAgo
                    date={comment.createdAt}
                    edited={!!comment.updatedAt}
                  />
                </div>
              </div>
            </button>
            {comment.user.id == user?.id && (
              <button
                title="actions button"
                className={`${classes.action} ${showActions ? classes.active : ""}`}
                onClick={() => setShowActions(!showActions)}
              >
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 512">
                  <path d="M64 360a56 56 0 1 0 0 112 56 56 0 1 0 0-112zm0-160a56 56 0 1 0 0 112 56 56 0 1 0 0-112zM120 96A56 56 0 1 0 8 96a56 56 0 1 0 112 0z" />
                </svg>
              </button>
            )}

            {showActions && (
              <div className={classes.actions}>
                <button onClick={() => setEditing(true)}>Edit</button>
                <button onClick={() => deleteComment(comment.id)}>
                  Delete
                </button>
              </div>
            )}
          </div>
          <div className={classes.content}>{comment.content}</div>
        </>
      ) : (
        <form
          onSubmit={async (e) => {
            e.preventDefault();
            await editComment(comment.id, commentContent);
            setEditing(false);
            setShowActions(false);
          }}
        >
          <Input
            type="text"
            value={commentContent}
            onChange={(e) => {
              setCommentContent(e.target.value);
            }}
            placeholder="Edit your comment"
          />
        </form>
      )}
    </div>
  );
}
