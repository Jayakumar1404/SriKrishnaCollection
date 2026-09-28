/* =========================================================
   SRI KRISHNA COLLECTION
   CUSTOMER CATEGORIES
   JAVASCRIPT
========================================================= */

document.addEventListener("DOMContentLoaded", function () {

    console.log("Sri Krishna Collection - Categories Loaded");


    /* =====================================================
       CATEGORY CARDS
    ===================================================== */

    const cards =
        document.querySelectorAll(".category-card");


    /* =====================================================
       REVEAL ANIMATION
    ===================================================== */

    if ("IntersectionObserver" in window) {

        const observer =
            new IntersectionObserver(
                function (entries, observer) {

                    entries.forEach(function (entry) {

                        if (entry.isIntersecting) {

                            entry.target.classList.add(
                                "revealed"
                            );

                            observer.unobserve(
                                entry.target
                            );

                        }

                    });

                },
                {
                    threshold: 0.12
                }
            );


        cards.forEach(function (card) {

            observer.observe(card);

        });

    } else {

        cards.forEach(function (card) {

            card.classList.add("revealed");

        });

    }


    /* =====================================================
       VIEW PRODUCTS
    ===================================================== */

    const buttons =
        document.querySelectorAll(".category-button");


    buttons.forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const card =
                    this.closest(".category-card");


                if (!card) {

                    return;

                }


                const title =
                    card.querySelector("h3");


                if (!title) {

                    return;

                }


                const categoryName =
                    title.textContent.trim();


                if (!categoryName) {

                    return;

                }


                /* =========================================
                   SWEET ALERT
                ========================================= */

                if (typeof Swal !== "undefined") {

                    Swal.fire({

                        icon: "success",

                        title: categoryName,

                        text: "Opening Products...",

                        timer: 1100,

                        showConfirmButton: false,

                        background: "#faf7ef",

                        color: "#111111",

                        iconColor: "#c9a227"

                    }).then(function () {

                        openProducts(
                            categoryName
                        );

                    });

                } else {

                    openProducts(
                        categoryName
                    );

                }

            }
        );

    });


    /* =====================================================
       OPEN PRODUCTS
    ===================================================== */

function openProducts(categoryName) {

    window.location.href =
        "/Krishna/product?category="
        + encodeURIComponent(categoryName);

}



    /* =====================================================
       CATEGORY SEARCH
    ===================================================== */

    const searchInput =
        document.getElementById(
            "categorySearch"
        );


    const searchButton =
        document.getElementById(
            "searchButton"
        );


    const noSearchResults =
        document.getElementById(
            "noSearchResults"
        );


    const clearSearch =
        document.getElementById(
            "clearSearch"
        );


    function performSearch() {

        if (!searchInput) {

            return;

        }


        const searchValue =
            searchInput.value
                .trim()
                .toLowerCase();


        let visibleCount = 0;


        cards.forEach(function (card) {

            const title =
                card.querySelector("h3");


            const description =
                card.querySelector("p");


            const categoryName =
                title
                    ? title.textContent
                        .trim()
                        .toLowerCase()
                    : "";


            const categoryDescription =
                description
                    ? description.textContent
                        .trim()
                        .toLowerCase()
                    : "";


            const matches =
                searchValue === ""
                ||
                categoryName.includes(
                    searchValue
                )
                ||
                categoryDescription.includes(
                    searchValue
                );


            if (matches) {

                card.classList.remove(
                    "search-hidden"
                );

                visibleCount++;

            } else {

                card.classList.add(
                    "search-hidden"
                );

            }

        });


        if (
            noSearchResults
            &&
            searchValue !== ""
            &&
            visibleCount === 0
        ) {

            noSearchResults.classList.remove(
                "d-none"
            );

        } else if (noSearchResults) {

            noSearchResults.classList.add(
                "d-none"
            );

        }

    }


    if (searchInput) {

        searchInput.addEventListener(
            "input",
            performSearch
        );


        searchInput.addEventListener(
            "keydown",
            function (event) {

                if (event.key === "Enter") {

                    event.preventDefault();

                    performSearch();

                }

            }
        );

    }


    if (searchButton) {

        searchButton.addEventListener(
            "click",
            performSearch
        );

    }


    /* =====================================================
       CLEAR SEARCH
    ===================================================== */

    if (clearSearch) {

        clearSearch.addEventListener(
            "click",
            function () {

                if (searchInput) {

                    searchInput.value = "";

                }


                cards.forEach(function (card) {

                    card.classList.remove(
                        "search-hidden"
                    );

                });


                if (noSearchResults) {

                    noSearchResults.classList.add(
                        "d-none"
                    );

                }


                if (searchInput) {

                    searchInput.focus();

                }

            }
        );

    }


    /* =====================================================
       SCROLL TO TOP
    ===================================================== */

    const topButton =
        document.getElementById(
            "scrollTop"
        );


    function updateScrollButton() {

        if (!topButton) {

            return;

        }


        if (window.scrollY > 300) {

            topButton.classList.add(
                "show"
            );

        } else {

            topButton.classList.remove(
                "show"
            );

        }

    }


    window.addEventListener(
        "scroll",
        updateScrollButton,
        {
            passive: true
        }
    );


    updateScrollButton();


    if (topButton) {

        topButton.addEventListener(
            "click",
            function () {

                window.scrollTo({

                    top: 0,

                    behavior: "smooth"

                });

            }
        );

    }


    /* =====================================================
       IMAGE ERROR HANDLING
    ===================================================== */

    const images =
        document.querySelectorAll(
            ".category-image"
        );


    images.forEach(function (image) {

        image.addEventListener(
            "error",
            function () {

                this.classList.add(
                    "image-error"
                );

                this.alt =
                    "Category image unavailable";

            }
        );

    });


    /* =====================================================
       SEARCH BOX SHORTCUT
       CTRL + K
    ===================================================== */

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                (event.ctrlKey || event.metaKey)
                &&
                event.key.toLowerCase() === "k"
            ) {

                event.preventDefault();

                if (searchInput) {

                    searchInput.focus();

                }

            }

        }
    );

});

// =====================================================
// ADMIN CATEGORY TABLE - INSTANT SEARCH
// =====================================================
document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("adminCategorySearch");
    if (!input) return;

    input.addEventListener("input", function () {
        const value = this.value.toLowerCase().trim();
        document.querySelectorAll(".category-table-row").forEach(function (row) {
            row.style.display = row.innerText.toLowerCase().includes(value) ? "" : "none";
        });
    });
});
