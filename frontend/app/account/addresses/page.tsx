"use client";
import { useState, useEffect, ChangeEvent, FormEvent } from "react";
import styles from "./page.module.css";
import LayoutContainer from "@/components/LayoutContainer";
import LayoutBox from "@/components/LayoutBox";
import LayoutTitle from "@/components/LayoutTitle";
import LoadingSpinner from "@/components/LoadingSpinner";
import Address from "@/components/Address";
import Dialog from "@/components/Dialog";
import Input from "@/components/Input";
import Button from "@/components/Button";

interface Address {
  title: string;
  customer_name: string;
  address: string;
}

export default function Addresses() {
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [isLoading, setLoading] = useState(true);

  async function getAllAddresses() {
    await fetch("http://localhost:8080/api/v1/addresses", {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setAddresses(data);
        setLoading(false);
      });
  }

  useEffect(() => {
    getAllAddresses();
  }, []);

  const [addAddressDialog, setAddAddressDialog] = useState(false);
  const [addAddressDialogStatus, setAddAddressDialogStatus] = useState(false);

  function openAddAddressDialog() {
    setAddAddressDialogStatus(true);
    setAddAddressDialog(true);
  }

  function closeAddAddressDialog() {
    setAddAddressDialogStatus(false);
    setTimeout(() => setAddAddressDialog(false), 300);
  }

  const [newAddress, setNewAddress] = useState<Address | any>({
    title: "",
    customer_name: "",
    address: "",
  });

  function handleNewAddress(e: ChangeEvent<HTMLInputElement>) {
    const copy = { ...newAddress };
    copy[e.target.name] = e.target.value;
    setNewAddress(copy);
  }

  async function createNewAddress(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const response = await fetch("addresses/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newAddress),
    });

    if (response.status == 200) {
      await getAllAddresses();
      closeAddAddressDialog();
    }
  }

  return (
    <LayoutContainer>
      <LayoutTitle>Addresses</LayoutTitle>
      <LayoutBox minHeight="274px">
        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <div className={styles.row}>
            <button
              className={styles.addAddressBox}
              onClick={openAddAddressDialog}
            ></button>
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
        {addAddressDialog && (
          <Dialog
            title="Add new address"
            close={closeAddAddressDialog}
            status={addAddressDialogStatus}
          >
            <form onSubmit={createNewAddress}>
              <Input
                label="Title"
                type="text"
                name="title"
                value={newAddress.title}
                onChange={handleNewAddress}
              />
              <Input
                label="Customer name"
                type="text"
                name="customer_name"
                value={newAddress.customer_name}
                onChange={handleNewAddress}
              />
              <Input
                label="Address"
                type="text"
                name="address"
                value={newAddress.address}
                onChange={handleNewAddress}
              />
              <Button
                disabled={
                  !newAddress.title ||
                  !newAddress.customer_name ||
                  !newAddress.address
                }
              >
                Create
              </Button>
            </form>
          </Dialog>
        )}
      </LayoutBox>
    </LayoutContainer>
  );
}
