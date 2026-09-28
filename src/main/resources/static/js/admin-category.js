document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("adminCategorySearch");
    const tableBody = document.getElementById("categoryTableBody");

    if (searchInput && tableBody) {
        const filterRows = function () {
            const value = searchInput.value.toLowerCase().trim();
            const rows = tableBody.querySelectorAll("tr[data-category-row]");
            let visible = 0;

            rows.forEach(function (row) {
                const text = row.textContent.toLowerCase();
                const match = !value || text.includes(value);
                row.style.display = match ? "" : "none";
                if (match) visible++;
            });

            const emptyRow = tableBody.querySelector(".empty-category-row");
            if (emptyRow) emptyRow.style.display = visible === 0 ? "" : "none";
        };

        searchInput.addEventListener("input", filterRows);
        filterRows();
    }

    const imageFile = document.getElementById("imageFile");
    const previewImage = document.getElementById("previewImage");

    if (imageFile && previewImage) {
        imageFile.addEventListener("change", function () {
            const file = this.files && this.files[0];
            if (!file) return;
            const reader = new FileReader();
            reader.onload = function (event) {
                previewImage.src = event.target.result;
                previewImage.style.display = "inline-block";
            };
            reader.readAsDataURL(file);
        });
    }
});
