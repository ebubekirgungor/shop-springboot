import { FC } from "react";
import styles from "./Address.module.css";

interface Props {
  title: string;
  customerName: string;
  address: string;
}

const Address: FC<Props> = ({ title, address, customerName }) => {
  return (
    <div className={styles.box}>
      <div className={styles.title}>{title}</div>
      <div className={styles.customerName}>{customerName}</div>
      <div className={styles.address}>{address}</div>
    </div>
  );
};

export default Address;
