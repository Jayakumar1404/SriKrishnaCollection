/*=========================================*
 * Sri Krishna Collection
 * Admin Product JS
 *=========================================*/

document.addEventListener("DOMContentLoaded", function () {

    console.log("Product Module Loaded");


    /*=========================================
        MULTIPLE IMAGE PREVIEW
    =========================================*/

    const imageInput =
        document.getElementById("imageFiles");

    const previewContainer =
        document.getElementById("imagePreviewContainer");


    if (imageInput && previewContainer) {

        imageInput.addEventListener("change", function () {

            previewContainer.innerHTML = "";

            const files = Array.from(this.files);


            /* Maximum 5 images */

            if (files.length > 5) {

                alert(
                    "You can upload maximum 5 images."
                );

                this.value = "";

                return;
            }


            files.forEach(function (file, index) {

                /* Check image */

                if (!file.type.startsWith("image/")) {

                    return;
                }


                const reader =
                    new FileReader();


                reader.onload = function (event) {

                    /* Image wrapper */

                    const wrapper =
                        document.createElement("div");

                    wrapper.className =
                        "product-image-preview";

                    wrapper.style.width = "150px";

                    wrapper.style.position =
                        "relative";


                    /* Image */

                    const image =
                        document.createElement("img");

                    image.src =
                        event.target.result;

                    image.className =
                        "img-thumbnail";

                    image.style.width =
                        "150px";

                    image.style.height =
                        "150px";

                    image.style.objectFit =
                        "cover";


                    /* First image = Primary */

                    if (index === 0) {

                        const badge =
                            document.createElement("span");

                        badge.innerText =
                            "PRIMARY";

                        badge.style.position =
                            "absolute";

                        badge.style.top = "5px";

                        badge.style.left = "5px";

                        badge.style.background =
                            "#198754";

                        badge.style.color =
                            "white";

                        badge.style.padding =
                            "4px 7px";

                        badge.style.fontSize =
                            "11px";

                        badge.style.fontWeight =
                            "bold";

                        badge.style.borderRadius =
                            "5px";

                        wrapper.appendChild(
                            badge
                        );
                    }


                    wrapper.appendChild(
                        image
                    );


                    previewContainer.appendChild(
                        wrapper
                    );

                };


                reader.readAsDataURL(file);

            });

        });

    }


    /*=========================================
        PRODUCT SEARCH
    =========================================*/

    const search =
        document.getElementById(
            "searchProduct"
        );


    if (search) {

        search.addEventListener(
            "keyup",
            function () {

                const value =
                    this.value.toLowerCase();


                document
                    .querySelectorAll(
                        "tbody tr"
                    )
                    .forEach(function (row) {

                        row.style.display =
                            row.innerText
                                .toLowerCase()
                                .includes(value)
                                ? ""
                                : "none";

                    });

            }
        );

    }


    /*=========================================
        DELETE CONFIRMATION
    =========================================*/

    document
        .querySelectorAll(".deleteProduct")
        .forEach(function (btn) {

            btn.addEventListener(
                "click",
                function (e) {

                    const confirmed =
                        confirm(
                            "Are you sure you want to delete this product?"
                        );


                    if (!confirmed) {

                        e.preventDefault();

                    }

                }
            );

        });

});
document
    .querySelectorAll(".deleteProductImage")
    .forEach(function (btn) {

        btn.addEventListener("click", function (e) {

            const confirmed = confirm(
                "Are you sure you want to delete this image?"
            );

            if (!confirmed) {
                e.preventDefault();
            }

        });

    });