document.addEventListener("DOMContentLoaded", function () {

    console.log("=================================");
    console.log("ADMIN CATEGORY JS LOADED");
    console.log("=================================");


    // =====================================================
    // ELEMENTS
    // =====================================================

    const addCategoryBtn =
        document.getElementById("addCategoryBtn");

    const emptyAddCategoryBtn =
        document.getElementById("emptyAddCategoryBtn");

    const categoryModal =
        document.getElementById("categoryModal");

    const closeCategoryModalBtn =
        document.getElementById(
            "closeCategoryModalBtn"
        );

    const categoryForm =
        document.getElementById("categoryForm");

    const searchInput =
        document.getElementById("searchCategory");

    const tableBody =
        document.getElementById(
            "categoryTableBody"
        );

    const imageInput =
        document.getElementById("imageFile");

    const previewImage =
        document.getElementById("previewImage");


    console.log(
        "Add Category Button:",
        addCategoryBtn
    );

    console.log(
        "Category Modal:",
        categoryModal
    );


    // =====================================================
    // OPEN MODAL
    // =====================================================

    function openCategoryModal() {

        console.log(
            "Opening Category Modal..."
        );


        if (!categoryModal) {

            console.error(
                "ERROR: categoryModal not found!"
            );

            return;
        }


        categoryModal.classList.add("show");

        categoryModal.style.display = "flex";

        document.body.style.overflow =
            "hidden";


        // Focus category name

        setTimeout(function () {

            const categoryName =
                document.getElementById(
                    "categoryName"
                );

            if (categoryName) {

                categoryName.focus();

            }

        }, 150);
    }


    // =====================================================
    // CLOSE MODAL
    // =====================================================

    function closeCategoryModal() {

        if (!categoryModal) {

            return;
        }


        categoryModal.classList.remove(
            "show"
        );

        categoryModal.style.display =
            "none";

        document.body.style.overflow =
            "";
    }


    // =====================================================
    // ADD CATEGORY BUTTON
    // =====================================================

    if (addCategoryBtn) {

        addCategoryBtn.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                openCategoryModal();

            }
        );

    } else {

        console.error(
            "ERROR: addCategoryBtn not found!"
        );

    }


    // =====================================================
    // EMPTY STATE ADD BUTTON
    // =====================================================

    if (emptyAddCategoryBtn) {

        emptyAddCategoryBtn.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                openCategoryModal();

            }
        );

    }


    // =====================================================
    // CLOSE BUTTON
    // =====================================================

    if (closeCategoryModalBtn) {

        closeCategoryModalBtn.addEventListener(
            "click",
            function () {

                closeCategoryModal();

            }
        );

    }


    // =====================================================
    // CLICK OUTSIDE MODAL
    // =====================================================

    if (categoryModal) {

        categoryModal.addEventListener(
            "click",
            function (event) {

                if (
                    event.target ===
                    categoryModal
                ) {

                    closeCategoryModal();

                }

            }
        );

    }


    // =====================================================
    // ESCAPE KEY
    // =====================================================

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape"
            ) {

                closeCategoryModal();

            }

        }
    );


    // =====================================================
    // SEARCH
    // =====================================================

    if (
        searchInput &&
        tableBody
    ) {

        searchInput.addEventListener(
            "input",
            function () {

                const searchValue =
                    this.value
                        .toLowerCase()
                        .trim();


                const rows =
                    tableBody.querySelectorAll(
                        "tr[data-category-row]"
                    );


                rows.forEach(
                    function (row) {

                        const rowText =
                            row.innerText
                                .toLowerCase();


                        if (
                            rowText.includes(
                                searchValue
                            )
                        ) {

                            row.style.display =
                                "";

                        } else {

                            row.style.display =
                                "none";

                        }

                    }
                );

            }
        );

    }


    // =====================================================
    // IMAGE PREVIEW
    // =====================================================

    if (
        imageInput &&
        previewImage
    ) {

        imageInput.addEventListener(
            "change",
            function () {

                const file =
                    this.files[0];


                if (!file) {

                    return;
                }


                // Validate image

                if (
                    !file.type.startsWith(
                        "image/"
                    )
                ) {

                    alert(
                        "Please select a valid image file."
                    );

                    this.value = "";

                    return;
                }


                // Maximum 5 MB

                const maxSize =
                    5 * 1024 * 1024;


                if (
                    file.size > maxSize
                ) {

                    alert(
                        "Image size must be less than 5 MB."
                    );

                    this.value = "";

                    return;
                }


                const reader =
                    new FileReader();


                reader.onload =
                    function (event) {

                        previewImage.src =
                            event.target.result;

                    };


                reader.readAsDataURL(file);

            }
        );

    }


    // =====================================================
    // DELETE CONFIRMATION
    // =====================================================

    const deleteForms =
        document.querySelectorAll(
            ".delete-category-form"
        );


    deleteForms.forEach(
        function (form) {

            form.addEventListener(
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

        }
    );


    // =====================================================
    // FORM SUBMIT PROTECTION
    // =====================================================

    if (categoryForm) {

        categoryForm.addEventListener(
            "submit",
            function () {

                const submitButton =
                    categoryForm.querySelector(
                        ".category-save-button"
                    );


                if (submitButton) {

                    submitButton.disabled =
                        true;

                    submitButton.style.opacity =
                        "0.7";

                    submitButton.style.cursor =
                        "not-allowed";


                    const text =
                        submitButton.querySelector(
                            "span"
                        );


                    if (text) {

                        text.textContent =
                            "Saving...";

                    }

                }

            }
        );

    }


    // =====================================================
    // AUTO CLOSE SUCCESS / ERROR ALERT
    // =====================================================

    const alerts =
        document.querySelectorAll(
            ".alert-message"
        );


    alerts.forEach(
        function (alert) {

            setTimeout(
                function () {

                    alert.style.opacity =
                        "0";

                    alert.style.transition =
                        "opacity .4s ease";


                    setTimeout(
                        function () {

                            alert.remove();

                        },
                        400
                    );

                },
                5000
            );

        }
    );


});