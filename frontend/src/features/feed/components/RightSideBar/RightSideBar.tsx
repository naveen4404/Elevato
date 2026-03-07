import { Button } from "../../../../components/button/Button";
import classes from "./RightSideBar.module.scss";

export function RightSideBar() {
  return (
    <div className={classes.root}>
      <h3>Add to your feed</h3>
      <div className={classes.items}>
        <div className={classes.item}>
          <img
            src="https://t3.ftcdn.net/jpg/06/53/05/06/360_F_653050611_zjw73tRk6GII71af6GKgTD9VLJZ2byWm.jpg"
            alt=""
            className={classes.avatar}
          />
          <div className={classes.content}>
            <div className={classes.name}>Anil Kumar</div>
            <div className={classes.title}>
              Student at Vishnu Institute of Technology
            </div>
            <Button size="medium" outline className={classes.button}>
              + Follow
            </Button>
          </div>
        </div>
        <div className={classes.item}>
          <img
            src="https://static.vecteezy.com/system/resources/thumbnails/045/783/165/small_2x/confident-indian-businessman-in-modern-office-environment-professional-profile-branding-corporate-headshot-photo.jpg"
            alt=""
            className={classes.avatar}
          />
          <div className={classes.content}>
            <div className={classes.name}>Hari Krishna</div>
            <div className={classes.title}>
              Student at Vishnu Institute of Technology
            </div>
            <Button size="medium" outline className={classes.button}>
              + Follow
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
