import styles from "./page.module.css";
import Box from "@/components/Box";
import Input from "@/components/Input";
import CheckBox from "@/components/CheckBox";
import Button from "@/components/Button";

export default function Login() {
  return (
    <Box width={"25rem"}>
      <form className={styles.form}>
        <Input label="E-mail" type="email" />
        <Input label="Password" type="password" />
        <CheckBox label="Remember me" id="remember_me" />
        <Button>Sign In</Button>
      </form>
    </Box>
  );
}
