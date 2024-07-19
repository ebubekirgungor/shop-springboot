"use client";
import { useState, useEffect, ChangeEvent, FormEvent } from "react";
import styles from "./page.module.css";
import LayoutContainer from "@/components/LayoutContainer";
import LayoutBox from "@/components/LayoutBox";
import LayoutTitle from "@/components/LayoutTitle";
import LoadingSpinner from "@/components/LoadingSpinner";
import Dialog from "@/components/Dialog";
import Input from "@/components/Input";
import Button from "@/components/Button";
import Icon from "@/components/Icon";

interface Category {
  id: number | null;
  title: string;
  url: string;
  filters: string[];
  image: string;
}

export default function Categories() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setLoading] = useState(true);

  async function getAllCategories() {
    await fetch("http://localhost/api/v1/categories", {
      credentials: "include",
    })
      .then((response) => response.json())
      .then((data) => {
        setCategories(data);
        setLoading(false);
      });
  }

  useEffect(() => {
    getAllCategories();
  }, []);

  type DialogType = "POST" | "PUT" | "DELETE";

  const [dialogType, setDialogType] = useState<DialogType>();
  const [dialog, setDialog] = useState(false);
  const [dialogStatus, setDialogStatus] = useState(false);

  function openDialog(type: DialogType, category: Category) {
    setDialogType(type);
    setNewCategory(category);
    setDialogStatus(true);
    setDialog(true);
  }

  function closeDialog() {
    setDialogStatus(false);
    setTimeout(() => setDialog(false), 300);
  }

  const [newCategory, setNewCategory] = useState<Category>({
    id: null,
    title: "",
    url: "",
    filters: [],
    image: "",
  });

  const [newFilter, setNewFilter] = useState("");

  function handleNewFilter() {
    const copy = { ...newCategory };
    copy["filters"].push(newFilter);
    setNewCategory(copy);
    setNewFilter("");
  }

  function deleteFilter(index: number) {
    const copy = { ...newCategory };
    copy["filters"].splice(index, 1);
    setNewCategory(copy);
  }

  function handleNewCategory(e: ChangeEvent<HTMLInputElement>) {
    const copy = { ...newCategory } as any;
    copy[e.target.name] = e.target.value;
    setNewCategory(copy);
  }

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const response = await fetch("categories/actions", {
      method: dialogType,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newCategory),
    });

    if (response.status == 200) {
      await getAllCategories();
      closeDialog();
    }
  }

  return (
    <LayoutContainer>
      <LayoutTitle>Categories</LayoutTitle>
      <LayoutBox minHeight="274px">
        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <div className={styles.row}>
            <button
              className={styles.addBox}
              onClick={() =>
                openDialog("POST", {
                  id: null,
                  title: "",
                  url: "",
                  filters: [],
                  image: "",
                })
              }
            ></button>
            {categories &&
              categories.map((category) => (
                <div className={styles.categoryBox}>
                  <div className={styles.actions}>
                    <button
                      className={styles.button}
                      onClick={() => openDialog("PUT", category)}
                    >
                      <Icon name="edit" />
                    </button>
                    <button
                      className={styles.button}
                      onClick={() => openDialog("DELETE", category)}
                    >
                      <Icon name="delete" />
                    </button>
                  </div>
                  <img src={"/images/categories/" + category.image} />
                  <div className={styles.title}>{category.title}</div>
                </div>
              ))}
          </div>
        )}
        {dialog && (
          <Dialog
            title={
              {
                POST: "Add new category",
                PUT: "Edit category",
                DELETE: "Delete category",
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
                    value={newCategory.title}
                    required
                    onChange={handleNewCategory}
                  />
                  <div className={styles.filtersContainer}>
                    <Input
                      label="Filters"
                      type="text"
                      name="newfilter"
                      value={newFilter}
                      onChange={(e) => setNewFilter(e.target.value)}
                    />
                    <button
                      className={styles.addFilterButton}
                      type="button"
                      onClick={handleNewFilter}
                      disabled={!newFilter}
                    >
                      Add
                    </button>
                  </div>
                  <div className={styles.filtersRow}>
                    {newCategory.filters &&
                      newCategory.filters.map((filter, index) => (
                        <div className={styles.filter}>
                          {filter}
                          <button
                            onClick={() => deleteFilter(index)}
                            type="button"
                          >
                            <Icon name="close" />
                          </button>
                        </div>
                      ))}
                  </div>
                </>
              ) : (
                <div style={{ textAlign: "center" }}>Are you sure?</div>
              )}
              <Button disabled={!newCategory.title}>
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
