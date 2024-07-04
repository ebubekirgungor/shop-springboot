"use client";
import { useState, useEffect } from "react";
import LayoutContainer from "@/components/LayoutContainer";
import LayoutBox from "@/components/LayoutBox";
import LayoutTitle from "@/components/LayoutTitle";
import LoadingSpinner from "@/components/LoadingSpinner";
import Address from "@/components/Address";

interface Address {
  title: string;
  customer_name: string;
  address: string;
}

export default function Addresses() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [isLoading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/addresses", {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setAddresses(data);
        setLoading(false);
      });
  }, []);

  return (
    <LayoutContainer>
      <LayoutTitle>Addresses</LayoutTitle>
      <LayoutBox minHeight="274px">
        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <div>
            {addresses &&
              addresses.map((address) => (
                <Address
                  title={address.title}
                  customerName={address.customer_name}
                  address={address.address}
                ></Address>
              ))}
          </div>
        )}
      </LayoutBox>
    </LayoutContainer>
  );
}
