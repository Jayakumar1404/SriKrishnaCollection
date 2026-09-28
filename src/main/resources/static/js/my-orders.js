document.addEventListener("DOMContentLoaded",()=>{

console.log("My Orders Loaded");

document.querySelectorAll(".viewBtn")

.forEach(btn=>{

btn.addEventListener("click",()=>{

Swal.fire({

icon:"info",

title:"Order Details",

text:"Order details page will open."

});

});

});

document.querySelectorAll(".cancelBtn")

.forEach(btn=>{

btn.addEventListener("click",()=>{

Swal.fire({

title:"Cancel Order?",

text:"Do you want to cancel this order?",

icon:"warning",

showCancelButton:true,

confirmButtonText:"Yes"

}).then((result)=>{

if(result.isConfirmed){

Swal.fire({

icon:"success",

title:"Order Cancelled"

});

}

});

});

});

});