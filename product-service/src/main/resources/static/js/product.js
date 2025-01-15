//Gestion des images
document.addEventListener('DOMContentLoaded', function() {
    initializeImageUpload();
    initializeFormValidation();
    
   
});
// Configuration de l'upload d'images
function initializeImageUpload() {
const dropzone = document.getElementById('imageDropzone');
const previewContainer = document.getElementById('image-previews');
if (!dropzone) return;
// Prévenir le comportement par défaut du navigateur
['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
dropzone.addEventListener(eventName, preventDefaults, false);
});
function preventDefaults(e) {
e.preventDefault();
e.stopPropagation();
}
// Gestion du drag & drop
['dragenter', 'dragover'].forEach(eventName => {
dropzone.addEventListener(eventName, highlight, false);
});
['dragleave', 'drop'].forEach(eventName => {
dropzone.addEventListener(eventName, unhighlight, false);
});
function highlight(e) {
dropzone.classList.add('border-primary');
}
function unhighlight(e) {
dropzone.classList.remove('border-primary');
}
// Gestion du drop
dropzone.addEventListener('drop', handleDrop, false);
function handleDrop(e) {
const dt = e.dataTransfer;
const files = dt.files;
handleFiles(files);
}
// Gestion de la sélection de fichiers
const fileInput = dropzone.querySelector('input[type="file"]');
fileInput.addEventListener('change', function(e) {
handleFiles(this.files);
});
function handleFiles(files) {
const validFiles = validateFiles(files);
validFiles.forEach(previewFile);
}
// Validation des fichiers
function validateFiles(files) {
const maxFiles = 5;
const maxSize = 5 * 1024 * 1024; // 5MB
const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
const validFiles = [];
if (files.length > maxFiles) {
showError(`Maximum ${maxFiles} images allowed`);
return validFiles;
}
for (let file of files) {
if (!allowedTypes.includes(file.type)) {
showError(`Invalid file type: ${file.name}`);
continue;
}
if (file.size > maxSize) {
showError(`File too large: ${file.name}`);
continue;
}
validFiles.push(file);
}
return validFiles;
}
// Prévisualisation des images
function previewFile(file) {
const reader = new FileReader();
reader.readAsDataURL(file);
reader.onloadend = function() {
const preview = createPreviewElement(reader.result, file.name);
previewContainer.appendChild(preview);
}
}
function createPreviewElement(src, filename) {
const div = document.createElement('div');
div.className = 'col-md-3';
div.innerHTML =
`
<div class="position-relative">
<img src="${src}" class="img-thumbnail" alt="${filename}">
<button type="button" class="btn btn-danger btn-sm
position-absolute top-0 end-0 m-1"
onclick="this.parentElement.parentElement.remove()">
<i class="fas fa-times"></i>
</button>
</div>
`
;
return div;
}
}
// Validation du formulaire
function initializeFormValidation() {
const form = document.querySelector('form');
if (!form) return;
form.addEventListener('submit', function(e) {
if (!validateForm()) {
e.preventDefault();
}
});
function validateForm() {
let isValid = true;
// Validation du prix
const price = document.querySelector('input[name="price"]');
if (price && parseFloat(price.value) <= 0) {
showError('Price must be greater than 0');
isValid = false;
}
// Validation du stock
const stock =
document.querySelector('input[name="stockQuantity"]');
if (stock && parseInt(stock.value) < 0) {
showError('Stock cannot be negative');
isValid = false;
}
return isValid;
}
}
// Gestion des produits
function deleteProduct(id) {
if (confirm('Are you sure you want to delete this product?')) {
fetch(`/api/products/${id}`
, {
method: 'DELETE',
headers: {
'Content-Type': 'application/json'
}
})
.then(response => {
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
// Mise à jour du stock
function updateStock(id, quantity) {
fetch(`/api/products/${id}/stock`, {
method: 'PATCH',
headers: {
'Content-Type': 'application/json'
},
body: JSON.stringify({ quantity: quantity })
})
.then(response => {
if (response.ok) {
window.location.reload();
} else {
showError('Error updating stock');
}
})
.catch(error => {
console.error('Error:', error);
showError('Error updating stock');
});
}
// Utilitaires
function showError(message) {
const alertDiv = document.createElement('div');
alertDiv.className = 'alert alert-danger alert-dismissible fade show';
alertDiv.innerHTML =
`
${message}
<button type="button" class="btn-close" data-bs-
dismiss="alert"></button>
`
;
const container = document.querySelector('.container');
container.insertBefore(alertDiv, container.firstChild);
setTimeout(() => {
alertDiv.remove();
}, 5000);
}
function showSuccess(message) {
const alertDiv = document.createElement('div');
alertDiv.className = 'alert alert-success alert-dismissible fade show';
alertDiv.innerHTML =
`
${message}
<button type="button" class="btn-close" data-bs-
dismiss="alert"></button>
`
;
const container = document.querySelector('.container');
container.insertBefore(alertDiv, container.firstChild);
setTimeout(() => {
alertDiv.remove();
}, 3000);
}


