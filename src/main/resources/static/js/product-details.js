document.addEventListener("DOMContentLoaded",()=>{

    console.log("Product Details Loaded");

    document.querySelector(".btn-warning")
    .addEventListener("click",()=>{

        Swal.fire({

            icon:"success",

            title:"Added to Cart",

            text:"Product added successfully!",

            timer:1500,

            showConfirmButton:false

        });

    });

    document.querySelector(".btn-danger")
    .addEventListener("click",()=>{

        Swal.fire({

            icon:"success",

            title:"Wishlist",

            text:"Added to Wishlist!",

            timer:1500,

            showConfirmButton:false

        });

    });

    document.querySelector(".btn-success")
    .addEventListener("click",()=>{

        Swal.fire({

            icon:"success",

            title:"Redirecting",

            text:"Proceeding to Checkout",

            timer:1500,

            showConfirmButton:false

        });

    });

});