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
  id: number | null;
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

  type DialogType = "POST" | "PUT" | "DELETE";

  const [dialogType, setDialogType] = useState<DialogType>();
  const [dialog, setDialog] = useState(false);
  const [dialogStatus, setDialogStatus] = useState(false);

  function openDialog(type: DialogType, address: Address) {
    setDialogType(type);
    setNewAddress(address);
    setDialogStatus(true);
    setDialog(true);
  }

  function closeDialog() {
    setDialogStatus(false);
    setTimeout(() => setDialog(false), 300);
  }

  const [newAddress, setNewAddress] = useState<Address | any>({
    id: null,
    title: "",
    customer_name: "",
    address: "",
  });

  function handleNewAddress(e: ChangeEvent<HTMLInputElement>) {
    const copy = { ...newAddress };
    copy[e.target.name] = e.target.value;
    setNewAddress(copy);
  }

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const response = await fetch("addresses/actions", {
      method: dialogType,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newAddress),
    });

    if (response.status == 200) {
      await getAllAddresses();
      closeDialog();
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
              onClick={() =>
                openDialog("POST", {
                  id: null,
                  title: "",
                  customer_name: "",
                  address: "",
                })
              }
            ></button>
            {addresses &&
              addresses.map((address) => (
                <Address
                  title={address.title}
                  customerName={address.customer_name}
                  address={address.address}
                  editButton={() => openDialog("PUT", address)}
                  deleteButton={() => openDialog("DELETE", address)}
                ></Address>
              ))}
          </div>
        )}
        {dialog && (
          <Dialog
            title={
              {
                POST: "Add new address",
                PUT: "Edit address",
                DELETE: "Delete address",
              }[dialogType!]
            }
            close={closeDialog}
            status={dialogStatus}
          >
            <form onSubmit={onSubmit}>
              {dialogType != "DELETE" ? (
                <>
                  <Input
                    label="Title"
                    type="text"
                    name="title"
                    value={newAddress.title}
                    required
                    onChange={handleNewAddress}
                  />
                  <Input
                    label="Customer name"
                    type="text"
                    name="customer_name"
                    value={newAddress.customer_name}
                    required
                    onChange={handleNewAddress}
                  />
                  <Input
                    label="Address"
                    type="text"
                    name="address"
                    value={newAddress.address}
                    required
                    onChange={handleNewAddress}
                  />
                </>
              ) : (
                <div style={{ textAlign: "center" }}>Are you sure?</div>
              )}
              <Button>
                {
                  {
                    POST: "Create",
                    PUT: "Update",
                    DELETE: "Delete",
                  }[dialogType!]
                }
              </Button>
            </form>
          </Dialog>
        )}
      </LayoutBox>
    </LayoutContainer>
  );
}
