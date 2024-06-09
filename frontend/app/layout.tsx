import type { Metadata } from "next";
import { Poppins } from "next/font/google";
import "./globals.css";
import styles from "./layout.module.css";

const poppins = Poppins({ subsets: ["latin"], weight: ["300", "400", "500"] });

export const metadata: Metadata = {
  title: "Shop",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={poppins.className}>
        <nav className={styles.nav}>
          <div className={styles.logo}>asd</div>
          <div className={styles.search}>
            <input></input>
          </div>
          <div className={styles.nav_buttons}>asd</div>
        </nav>
        {children}
      </body>
    </html>
  );
}
