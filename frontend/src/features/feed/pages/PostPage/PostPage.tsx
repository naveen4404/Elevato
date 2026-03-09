import { useParams } from "react-router-dom";
import { PostCard, type Post } from "../../components/Post/PostCard";
import classes from "./PostPage.module.scss";
import { useEffect, useState } from "react";
import { request } from "../../../../utils/api";
import { LeftSideBar } from "../../components/LeftSideBar/LeftSideBar";
import { RightSideBar } from "../../components/RightSideBar/RightSideBar";

export function PostPage() {
  const [posts, setPosts] = useState<Post[]>([]);
  const { id } = useParams();

  useEffect(() => {
    request<Post>({
      endpoint: `/api/v1/feed/posts/${id}`,
      onSuccess: (post) => setPosts([post]),
      onFailure: (error) => console.log(error),
    });
  }, [id]);

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSideBar />
      </div>
      <div className={classes.center}>
        {posts.length > 0 && <PostCard setPosts={setPosts} post={posts[0]} />}
      </div>
      <div className={classes.right}>
        <RightSideBar />
      </div>
    </div>
  );
}
