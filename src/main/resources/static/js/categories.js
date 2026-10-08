/*
=========================================================
    SRI KRISHNA COLLECTION
    ADMIN CATEGORY MANAGEMENT
    JAVASCRIPT
=========================================================
*/

document.addEventListener("DOMContentLoaded", function () {

    console.log("Sri Krishna Collection - Admin Categories Loaded");


    /* =====================================================
       ELEMENTS
    ===================================================== */

    const modal =
        document.getElementById("categoryModal");

    const addButton =
        document.getElementById("addCategoryBtn");

    const closeButton =
        document.getElementById("closeCategoryModal");

    const cancelButton =
        document.getElementById("cancelCategoryBtn");

    const form =
        document.getElementById("categoryForm");

    const imageInput =
        document.getElementById("imageFile");

    const previewImage =
        document.getElementById("previewImage");

    const modalTitle =
        document.getElementById("categoryModalTitle");

    const submitButton =
        form
            ? form.querySelector("button[type='submit']")
            : null;


    /* =====================================================
       OPEN MODAL
    ===================================================== */

    function openCategoryModal() {

        if (!modal) {
            return;
        }

        modal.classList.add("show");

        modal.style.display = "flex";

        document.body.classList.add("modal-open");

    }


    /* =====================================================
       CLOSE MODAL
    ===================================================== */

    function closeCategoryModal() {

        if (!modal) {
            return;
        }

        modal.classList.remove("show");

        modal.style.display = "none";

        document.body.classList.remove("modal-open");

    }


    /* =====================================================
       ADD CATEGORY
    ===================================================== */

    if (addButton) {

        addButton.addEventListener(
            "click",
            function () {

                resetForm();

                if (modalTitle) {
                    modalTitle.textContent = "Add Category";
                }

                openCategoryModal();

            }
        );

    }


    /* =====================================================
       CLOSE BUTTON
    ===================================================== */

    if (closeButton) {

        closeButton.addEventListener(
            "click",
            closeCategoryModal
        );

    }


    /* =====================================================
       CANCEL BUTTON
    ===================================================== */

    if (cancelButton) {

        cancelButton.addEventListener(
            "click",
            closeCategoryModal
        );

    }


    /* =====================================================
       CLICK OUTSIDE MODAL
    ===================================================== */

    if (modal) {

        modal.addEventListener(
            "click",
            function (event) {

                if (event.target === modal) {
                    closeCategoryModal();
                }

            }
        );

    }


    /* =====================================================
       ESCAPE KEY
    ===================================================== */

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape") {

                if (
                    modal &&
                    modal.classList.contains("show")
                ) {

                    closeCategoryModal();

                }

            }

        }
    );


    /* =====================================================
       RESET FORM
    ===================================================== */

    function resetForm() {

        if (!form) {
            return;
        }


        form.reset();


        /*
        IMPORTANT:
        Keep hidden category ID empty
        so controller treats it as ADD.
        */

        const idInput =
            form.querySelector("input[name='id']");

        if (idInput) {
            idInput.value = "";
        }


        /*
        Reset image preview
        */

        if (previewImage) {

            previewImage.src =
                "https://placehold.co/150x150?text=Preview";

        }

    }


    /* =====================================================
       IMAGE PREVIEW
    ===================================================== */

    if (imageInput) {

        imageInput.addEventListener(
            "change",
            function () {

                const file =
                    this.files && this.files[0];


                if (!file) {
                    return;
                }


                /* =========================================
                   FILE TYPE
                ========================================= */

                if (!file.type.startsWith("image/")) {

                    alert(
                        "Please select a valid image file."
                    );

                    this.value = "";

                    return;

                }


                /* =========================================
                   MAX FILE SIZE = 5MB
                ========================================= */

                const maxSize =
                    5 * 1024 * 1024;


                if (file.size > maxSize) {

                    alert(
                        "Image size must be less than 5MB."
                    );

                    this.value = "";

                    return;

                }


                /* =========================================
                   PREVIEW
                ========================================= */

                if (previewImage) {

                    const reader =
                        new FileReader();


                    reader.onload =
                        function (event) {

                            previewImage.src =
                                event.target.result;

                        };


                    reader.readAsDataURL(file);

                }

            }
        );

    }


    /* =====================================================
       FORM SUBMIT
    ===================================================== */

    if (form) {

        form.addEventListener(
            "submit",
            function () {

                if (submitButton) {

                    submitButton.disabled = true;

                    submitButton.innerHTML =
                        '<i class="fa-solid fa-spinner fa-spin"></i> Saving...';

                }

            }
        );

    }


    /* =====================================================
       DELETE CATEGORY CONFIRMATION
    ===================================================== */

    const deleteForms =
        document.querySelectorAll(
            ".delete-category-form"
        );


    deleteForms.forEach(function (deleteForm) {

        deleteForm.addEventListener(
            "submit",
            function (event) {

                const confirmed =
                    confirm(
                        "Are you sure you want to delete this category?"
                    );


                if (!confirmed) {

                    event.preventDefault();

                }

            }
        );

    });


    /* =====================================================
       ADMIN CATEGORY SEARCH
    ===================================================== */

    const searchInput =
        document.getElementById(
            "adminCategorySearch"
        );


    if (searchInput) {

        searchInput.addEventListener(
            "input",
            function () {

                const value =
                    this.value
                        .toLowerCase()
                        .trim();


                document
                    .querySelectorAll(
                        ".category-table-row"
                    )
                    .forEach(
                        function (row) {

                            const rowText =
                                row.innerText
                                    .toLowerCase();


                            row.style.display =
                                rowText.includes(value)
                                    ? ""
                                    : "none";

                        }
                    );

            }
        );

    }


    /* =====================================================
       AUTO HIDE ALERT
    ===================================================== */

    const alerts =
        document.querySelectorAll(
            ".alert"
        );


    alerts.forEach(function (alert) {

        setTimeout(
            function () {

                alert.style.opacity = "0";

                setTimeout(
                    function () {

                        alert.remove();

                    },
                    500
                );

            },
            4000
        );

    });

});