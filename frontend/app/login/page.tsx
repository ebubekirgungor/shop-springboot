"use client";
import { FormEvent, useState } from "react";
import Box from "@/components/Box";
import Input from "@/components/Input";
import CheckBox from "@/components/CheckBox";
import Button from "@/components/Button";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const router = useRouter();

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const response = await fetch("http://localhost:8080/api/v1/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({
        email: email,
        password: password,
        remember_me: rememberMe,
      }),
    });

    if (response.status == 200) {
      router.push("/");
    }
  }

  return (
    <Box width={"25rem"}>
      <form onSubmit={onSubmit}>
        <Input
          label="E-mail"
          type="text"
          name="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <Input
          label="Password"
          type="password"
          name="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <CheckBox
          label="Remember me"
          id="remember_me"
          name="remember_me"
          checked={rememberMe}
          onChange={(e) => setRememberMe(e.target.checked)}
        />
        <Button disabled={!email || !password}>Sign In</Button>
        <Link
          href="/register"
          style={{ textAlign: "center", fontSize: "14px" }}
        >
          Create Account
        </Link>
      </form>
    </Box>
  );
}
