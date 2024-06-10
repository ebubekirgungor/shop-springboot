import { FC } from "react";
import styles from "./Input.module.css";

interface Props {
  label?: string;
  type?: string;
}

const Input: FC<Props> = ({ label, ...props }) => {
  return (
    <label>
      {label}
      <input
        className={styles.input}
        autoComplete="off"
        autoCorrect="off"
        autoCapitalize="off"
        spellCheck="false"
        type="text"
        {...props}
      />
    </label>
  );
};

export default Input;
