/*=========================================*
 * Sri Krishna Collection
 * Admin Customer JS
 *=========================================*/

document.addEventListener("DOMContentLoaded", function () {

    console.log("Customer Module Loaded");

    /*==============================
        Elements
    ==============================*/

    const modalElement =
        document.getElementById("customerModal");

    const modal =
        (modalElement && window.bootstrap)
            ? new bootstrap.Modal(modalElement)
            : null;

    const form =
        document.getElementById("customerForm");

    const title =
        document.getElementById("customerModalTitle");

    const saveBtn =
        document.getElementById("saveCustomerBtn");

    const customerId =
        document.getElementById("customerId");

    const previewImage =
        document.getElementById("previewImage");

    const imageFile =
        document.getElementById("imageFile");
    const password =
        document.getElementById("password");

    const passwordHelp =
        document.getElementById("passwordHelp");

    /*==============================
        Add Customer
    ==============================*/

    const addCustomerBtn =
        document.getElementById("addCustomerBtn");

    if (addCustomerBtn) {

        addCustomerBtn.addEventListener(
            "click",
            function () {

                // Reset form

                form.reset();

                // Clear ID

                customerId.value = "";

                // Change form action

                form.action =
                    "/admin/customers/save";

                // Change title

                title.innerHTML =
                    "Add Customer";

                // Change button

                saveBtn.innerHTML =
                    '<i class="fa-solid fa-floppy-disk"></i> Save Customer';

                password.required = true;

                password.value = "";

                passwordHelp.innerHTML =
                    "Password is required when adding a customer.";
                // Reset image

                previewImage.src =
                    "https://placehold.co/180x180?text=Preview";

                if (modal) modal.show();

            }
        );

    }


    /*==============================
        Edit Customer
    ==============================*/

    document
        .querySelectorAll(".editCustomer")
        .forEach(function (btn) {

            btn.addEventListener(
                "click",
                function () {

                    /*--------------------------
                        Get Customer Data
                    --------------------------*/

                    const id =
                        this.dataset.id;

                    const firstName =
                        this.dataset.firstname;

                    const lastName =
                        this.dataset.lastname;

                    const email =
                        this.dataset.email;

                    const mobile =
                        this.dataset.mobile;

                    const gender =
                        this.dataset.gender;

                    const address =
                        this.dataset.address;

                    const city =
                        this.dataset.city;

                    const state =
                        this.dataset.state;

                    const pincode =
                        this.dataset.pincode;

                    const image =
                        this.dataset.image;
                    password.value = "";

                    password.required = false;

                    passwordHelp.innerHTML =
                        "Leave empty to keep the existing password.";

                    /*--------------------------
                        Set Form Values
                    --------------------------*/

                    customerId.value =
                        id;

                    document.getElementById(
                        "firstName"
                    ).value =
                        firstName || "";

                    document.getElementById(
                        "lastName"
                    ).value =
                        lastName || "";

                    document.getElementById(
                        "email"
                    ).value =
                        email || "";

                    document.getElementById(
                        "mobile"
                    ).value =
                        mobile || "";

                    document.getElementById(
                        "gender"
                    ).value =
                        gender || "";

                    document.getElementById(
                        "address"
                    ).value =
                        address || "";

                    document.getElementById(
                        "city"
                    ).value =
                        city || "";

                    document.getElementById(
                        "state"
                    ).value =
                        state || "";

                    document.getElementById(
                        "pincode"
                    ).value =
                        pincode || "";


                    /*--------------------------
                        Form Action
                    --------------------------*/

                    form.action =
                        "/admin/customers/update";


                    /*--------------------------
                        Modal Title
                    --------------------------*/

                    title.innerHTML =
                        "Update Customer";


                    /*--------------------------
                        Button
                    --------------------------*/

                    saveBtn.innerHTML =
                        '<i class="fa-solid fa-pen"></i> Update Customer';


                    /*--------------------------
                        Existing Image
                    --------------------------*/

                    if (image) {

                        previewImage.src =
                            "/customers/" + image;

                    } else {

                        previewImage.src =
                            "https://placehold.co/180x180?text=No+Image";

                    }


                    /*--------------------------
                        Clear New Image Selection
                    --------------------------*/

                    imageFile.value = "";


                    /*--------------------------
                        Open Modal
                    --------------------------*/

                    if (modal) modal.show();

                }
            );

        });


    /*==============================
        Image Preview
    ==============================*/

    if (imageFile) {

        imageFile.addEventListener(
            "change",
            function (event) {

                const file =
                    event.target.files[0];

                if (file) {

                    previewImage.src =
                        URL.createObjectURL(file);

                }

            }
        );

    }


    /*==============================
        Search Customer - INSTANT
    ==============================*/

    const searchCustomer = document.getElementById("searchCustomer");

    if (searchCustomer) {
        searchCustomer.addEventListener("input", function () {
            const value = this.value.toLowerCase().trim();
            const rows = document.querySelectorAll("tbody tr[data-customer-row]");
            let visible = 0;

            rows.forEach(function (row) {
                const text = row.textContent.toLowerCase();
                const match = !value || text.includes(value);
                row.style.display = match ? "" : "none";
                if (match) visible++;
            });

            const emptyRow = document.querySelector(".empty-customer-row");
            if (emptyRow) emptyRow.style.display = visible === 0 ? "" : "none";
        });
    }


    /*==============================
        Delete Confirmation
    ==============================*/

    document
        .querySelectorAll(".deleteCustomer")
        .forEach(function (btn) {

            btn.addEventListener(
                "click",
                function (event) {

                    event.preventDefault();

                    const deleteUrl =
                        this.getAttribute("href");

                    Swal.fire({

                        title:
                            "Delete Customer?",

                        text:
                            "This customer will be permanently deleted.",

                        icon:
                            "warning",

                        showCancelButton:
                            true,

                        confirmButtonText:
                            "Yes, Delete",

                        cancelButtonText:
                            "Cancel",

                        confirmButtonColor:
                            "#dc3545",

                        cancelButtonColor:
                            "#6c757d"

                    }).then(function (result) {

                        if (result.isConfirmed) {

                            window.location.href =
                                deleteUrl;

                        }

                    });

                }
            );

        });


    /*==============================
        Modal Reset
    ==============================*/

    if (modalElement) modalElement.addEventListener(
        "hidden.bs.modal",
        function () {

            form.reset();

            customerId.value = "";

            previewImage.src =
                "https://placehold.co/180x180?text=Preview";

        }
    );

});