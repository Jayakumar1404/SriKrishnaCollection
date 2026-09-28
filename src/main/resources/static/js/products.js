document.addEventListener("DOMContentLoaded", function () {

    const searchInput =
        document.getElementById("productSearch");

    const productGrid =
        document.getElementById("productGrid");

    const searchEmpty =
        document.getElementById("searchEmpty");

    const sortSelect =
        document.getElementById("sortProducts");


    /* =====================================================
       SEARCH PRODUCTS
       ===================================================== */

    function filterProducts() {

        const searchValue =
            searchInput
                ? searchInput.value.toLowerCase().trim()
                : "";

        const products =
            document.querySelectorAll(".product-wrapper");

        let visibleProducts = 0;


        products.forEach(function (product) {

            const name =
                (product.dataset.name || "")
                    .toLowerCase();

            const category =
                (product.dataset.category || "")
                    .toLowerCase();


            /*
             * IMPORTANT:
             *
             * Category filtering is NOT handled here.
             *
             * Spring Boot handles category filtering:
             *
             * /Krishna/product
             *
             * /Krishna/product?category=Preminum%20Top
             *
             * JavaScript only searches the products
             * already returned by the backend.
             */

            const searchMatch =
                searchValue === "" ||
                name.includes(searchValue) ||
                category.includes(searchValue);


            if (searchMatch) {

                product.style.display = "";

                visibleProducts++;

            } else {

                product.style.display = "none";

            }

        });


        /*
         * Show "No Products Found" only when:
         *
         * 1. Products exist
         * 2. Search text is entered
         * 3. Nothing matches
         */

        if (searchEmpty) {

            if (
                products.length > 0 &&
                searchValue !== "" &&
                visibleProducts === 0
            ) {

                searchEmpty.classList.add("show");

            } else {

                searchEmpty.classList.remove("show");

            }

        }

    }


    /* =====================================================
       SEARCH EVENT
       ===================================================== */

    if (searchInput) {

        searchInput.addEventListener(
            "input",
            filterProducts
        );

    }


    /* =====================================================
       SORT PRODUCTS
       ===================================================== */

    function sortProducts() {

        if (!sortSelect || !productGrid) {
            return;
        }


        const sortValue =
            sortSelect.value;


        const products =
            Array.from(
                productGrid.querySelectorAll(
                    ".product-wrapper"
                )
            );


        products.sort(function (a, b) {

            const priceA =
                Number(
                    a.dataset.price || 0
                );

            const priceB =
                Number(
                    b.dataset.price || 0
                );


            const nameA =
                (a.dataset.name || "")
                    .toLowerCase();

            const nameB =
                (b.dataset.name || "")
                    .toLowerCase();


            /* PRICE LOW TO HIGH */

            if (sortValue === "low") {

                return priceA - priceB;

            }


            /* PRICE HIGH TO LOW */

            if (sortValue === "high") {

                return priceB - priceA;

            }


            /* NAME */

            if (sortValue === "name") {

                return nameA.localeCompare(nameB);

            }


            return 0;

        });


        products.forEach(function (product) {

            productGrid.appendChild(product);

        });

    }


    /* =====================================================
       SORT EVENT
       ===================================================== */

    if (sortSelect) {

        sortSelect.addEventListener(
            "change",
            sortProducts
        );

    }


    /* =====================================================
       SEARCH OVERLAY
       ===================================================== */

    window.openSearch = function () {

        const overlay =
            document.getElementById(
                "searchOverlay"
            );


        if (!overlay) {
            return;
        }


        overlay.classList.add("show");


        const overlayInput =
            document.getElementById(
                "overlaySearch"
            );


        if (overlayInput) {

            setTimeout(function () {

                overlayInput.focus();

            }, 200);

        }

    };


    /* =====================================================
       CLOSE SEARCH
       ===================================================== */

    window.closeSearch = function () {

        const overlay =
            document.getElementById(
                "searchOverlay"
            );


        if (overlay) {

            overlay.classList.remove("show");

        }

    };


    /* =====================================================
       OVERLAY SEARCH
       ===================================================== */

    const overlaySearch =
        document.getElementById(
            "overlaySearch"
        );


    if (overlaySearch) {

        overlaySearch.addEventListener(
            "input",
            function () {

                if (searchInput) {

                    searchInput.value =
                        overlaySearch.value;

                    filterProducts();

                }

            }
        );

    }


    /* =====================================================
       MOBILE MENU
       ===================================================== */

    window.toggleMenu = function () {

        const menu =
            document.querySelector(
                ".nav-links"
            );


        if (menu) {

            menu.classList.toggle("show");

        }

    };


    /* =====================================================
       SEARCH OVERLAY CLOSE
       ===================================================== */

    const overlay =
        document.getElementById(
            "searchOverlay"
        );


    if (overlay) {

        overlay.addEventListener(
            "click",
            function (event) {

                if (event.target === overlay) {

                    closeSearch();

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

                closeSearch();

            }

        }
    );


    /* =====================================================
       WISHLIST UI
       ===================================================== */

    document
        .querySelectorAll(".wishlist-action")
        .forEach(function (button) {

            button.addEventListener(
                "click",
                function () {

                    const icon =
                        button.querySelector("i");


                    if (!icon) {
                        return;
                    }


                    /*
                     * Only update visual state.
                     *
                     * The form submission is still
                     * handled by the backend.
                     */

                    if (
                        icon.classList.contains(
                            "fa-regular"
                        )
                    ) {

                        icon.classList.remove(
                            "fa-regular"
                        );

                        icon.classList.add(
                            "fa-solid"
                        );

                    } else {

                        icon.classList.remove(
                            "fa-solid"
                        );

                        icon.classList.add(
                            "fa-regular"
                        );

                    }

                }
            );

        });


    /* =====================================================
       PRODUCT CARD ANIMATION DELAY
       ===================================================== */

    const cards =
        document.querySelectorAll(
            ".product-wrapper"
        );


    cards.forEach(function (card, index) {

        card.style.animationDelay =
            (index * 0.08) + "s";

    });


    /* =====================================================
       INITIAL SEARCH
       ===================================================== */

    filterProducts();

});