import { useNavigate } from "react-router-dom";
import { useAuthentication } from "../../../authentication/contexts/AuthenticationContextProvider";
import { LeftSideBar } from "../../components/LeftSideBar/LeftSideBar";
import { RightSideBar } from "../../components/RightSideBar/RightSideBar";
import classes from "./Feed.module.scss";
import { useEffect, useState } from "react";
import { Button } from "../../../../components/button/Button";
import { PostCard, type Post } from "../../components/Post/PostCard";
import { Modal } from "../../components/Modal/Modal";
import { request } from "../../../../utils/api";

export function Feed() {
  const navigate = useNavigate();
  const { user } = useAuthentication();
  const [showPostingModal, setShowPostingModal] = useState(false);
  const [posts, setPosts] = useState<Post[]>([]);
  const [error, setError] = useState("");
  const [feedContent, setFeedContent] = useState<"all" | "connections">(
    "connections",
  );

  useEffect(() => {
    const getFeed = async () => {
      await request<Post[]>({
        endpoint:
          "/api/v1/feed" + (feedContent === "connections" ? "" : "/posts"),
        onSuccess: (data) => setPosts(data),
        onFailure: (error) => setError(error),
      });
    };
    getFeed();
  }, [feedContent]);

  const handlePost = async (content: string, picture: string) => {
    await request<Post>({
      endpoint: "/api/v1/feed/posts",
      method: "POST",
      body: JSON.stringify({ content, picture }),
      onSuccess: (data: Post) => setPosts([data, ...posts]),
      onFailure: (error) => setError(error),
    });
  };

  return (
    <div className={classes.root}>
      <main className={classes.content}>
        <div className={classes.left}>
          <LeftSideBar />
        </div>
        <div className={classes.center}>
          <div className={classes.posting}>
            <button
              onClick={() => {
                navigate(`/profile/${user?.id}`);
              }}
            >
              <img
                className={`${classes.top} ${classes.avatar}`}
                src={user?.profilePicture || "/avatar.jpg"}
                alt="avatar"
              />
            </button>

            <Button
              outline
              onClick={() => {
                setShowPostingModal(true);
              }}
            >
              Start a post
            </Button>
            <Modal
              title="Creating a post"
              onSubmit={handlePost}
              showModal={showPostingModal}
              setShowModal={setShowPostingModal}
            />
          </div>
          {error && <p className={classes.error}>error</p>}
          <div className={classes.header}>
            <button
              className={feedContent === "all" ? classes.active : ""}
              onClick={() => {
                setFeedContent("all");
              }}
            >
              All
            </button>

            <button
              className={feedContent === "connections" ? classes.active : ""}
              onClick={() => {
                setFeedContent("connections");
              }}
            >
              Feed
            </button>
          </div>
          <div className={classes.feed}>
            {posts.map((post) => {
              return <PostCard key={post.id} post={post} setPosts={setPosts} />;
            })}
          </div>
        </div>

        <div className={classes.right}>
          <RightSideBar />
        </div>
      </main>
    </div>
  );
}
