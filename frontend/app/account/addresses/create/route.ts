export const fetchCache = "default-no-store";

import { NextRequest } from "next/server";

export async function POST(req: NextRequest) {
  const body = await req.json();

  const res = await fetch("http://localhost:8080/api/v1/addresses", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Cookie: "jwt=" + req.cookies.get("jwt")?.value,
    },
    body: JSON.stringify(body),
  });

  return Response.json(await res.json());
}
