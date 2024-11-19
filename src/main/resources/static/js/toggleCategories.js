function toggleSubCategories(button) {
    const subCategoriesDiv = button.parentElement.nextElementSibling;
    if (subCategoriesDiv) {
        if (subCategoriesDiv.style.display === "none") {
            subCategoriesDiv.style.display = "block";
            button.textContent = "-";
        } else {
            subCategoriesDiv.style.display = "none";
            button.textContent = "+";
        }
    }
}