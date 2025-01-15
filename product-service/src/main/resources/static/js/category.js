//Gestion des category
function deleteCategory(id) {
    console.log("Suppression de la catégorie avec ID :", categoryId);
if (confirm('Are you sure you want to delete this product?')) {
    fetch(`/api/v1/categories/${id}`, {
        method: 'DELETE',
        headers: {
        'Content-Type': 'application/json'
        }
        }).then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                showError('Error deleting product');
            }
        })
.catch(error => {
    console.error('Error:', error);
    showError('Error deleting product');
    });
}
}