class RecipeEdit {
    constructor() {
        this.ingredients = document.getElementById("ingredients-section");
        this.ingredientsAddButton = document.getElementById("ingredients-add");
        this.categories = document.getElementById("categories-section");
        this.categoriesSelect = document.getElementById("categories-add");
        this.postButton = document.getElementById("post-button");

        this.ingredient = document.getElementById("ingredient");
        this.ingredientNotes = document.getElementById("ingredientNotes");
        this.ingredientQuantity = document.getElementById("ingredientQuantity");
        this.ingredientUnit = document.getElementById("ingredientUnit");

        this.recipeId = document.getElementById("recipeId");
        this.title = document.getElementById("title");
        this.description = document.getElementById("description");
        this.instructions = document.getElementById("instructions");
        this.activeCookingTime = document.getElementById("activeCookingTime");
        this.totalCookingTime = document.getElementById("totalCookingTime");
        this.servings = document.getElementById("servings");
        this.imageUrl = document.getElementById("imageUrl");

        this.selectedIngredients = new Map();
        this.selectedCategories = new Set();
        this.init()
    }

    init() {
        this.initIngredients();
        this.initCategories();

        this.categoriesSelect.addEventListener("change", () => {
            this.addCategory();
        });

        this.ingredientsAddButton.addEventListener("click", () => {
            this.addIngredient();
        })


        this.postButton.addEventListener('click', () => {
            this.post();
        });
    }

    initIngredients() {
        existingIngredients.forEach(ingredient => {
            this.ingredient.value = ingredient["id"];
            this.ingredientQuantity.value = ingredient["quantity"];
            this.ingredientUnit.value = ingredient["unit"];
            this.ingredientNotes.value = ingredient["notes"];
            this.addIngredient();
        });
    }

    initCategories() {
        existingCategories.forEach(category => {
            this.categoriesSelect.value = category;
            this.addCategory();
        });
    }

    addIngredient() {
        const ingredientId = this.ingredient.value;
        const ingredientText = this.ingredient.options[this.ingredient.selectedIndex].textContent;
        const quantity = this.ingredientQuantity.value;
        const unitValue = this.ingredientUnit.value;
        const unitText = this.ingredientUnit.options[this.ingredientUnit.selectedIndex].textContent;
        const notes = this.ingredientNotes.value;

        if (!ingredientId || this.selectedIngredients.has(ingredientId)) return;

        if (!quantity) return;

        this.selectedIngredients.set(ingredientId, {
            id: ingredientId,
            notes: notes,
            quantity: quantity,
            unit: unitValue
        });

        const newField = document.createElement("div");
        newField.className = "ingredient-row";
        newField.innerHTML = `
            <div class="ingredient-field">${ingredientText}</div>
            <div class="ingredient-field">${notes}</div>
            <p>—</p>
            <div class="ingredient-field">${quantity}</div>
            <div class="ingredient-field">${unitText}</div>
            <button class="del-button" type="button">&times;</button>
            <div></div>
        `;

        this.ingredients.appendChild(newField);

        newField.querySelector(".del-button").addEventListener("click", () => {
            this.removeIngredient(ingredientId, newField);
        });

        this.ingredient.selectedIndex = 0;
        this.ingredientNotes.value = '';
        this.ingredientQuantity.value = '';
        this.ingredientUnit.selectedIndex = 0;
    }

    addCategory() {
        const id = this.categoriesSelect.value;
        const displayText = this.categoriesSelect.options[this.categoriesSelect.selectedIndex].textContent;

        if (!id || this.selectedCategories.has(id)) return;

        this.selectedCategories.add(id);

        const newField = document.createElement("div");
        newField.className = "category edit";
        newField.innerHTML = `${displayText}`;

        this.categories.appendChild(newField);

        newField.addEventListener("click", () => {
            this.removeCategory(id, newField);
        });

        this.categoriesSelect.selectedIndex = 0;
    }

    removeIngredient(id, element) {
        this.selectedIngredients.delete(id);
        element.remove();
    }

    removeCategory(id, element) {
        this.selectedCategories.delete(id);
        element.remove();
    }

    post() {
        const isNew = !(this.recipeId && this.recipeId.value);
        const recipeId = isNew ? null : parseInt(this.recipeId.value);

        const recipeData = {
            title: this.title.value,
            description: this.description.value,
            instructions: this.instructions.value,
            activeCookingTime: parseInt(this.activeCookingTime.value),
            totalCookingTime: parseInt(this.totalCookingTime.value),
            servings: parseInt(this.servings.value),
            imageUrl: this.imageUrl.value,
            categories: Array.from(this.selectedCategories).map(catId => parseInt(catId)),
            ingredients: Array.from(this.selectedIngredients.values()).map(ingredient => ({
                id: parseInt(ingredient.id),
                quantity: parseFloat(ingredient.quantity),
                unit: ingredient.unit,
                notes: ingredient.notes || ''
            }))
        };

        const url = isNew
            ? CONTEXT_PATH + '/api/recipes'
            : CONTEXT_PATH + '/api/recipes/' + recipeId;

        fetch(url, {
            method: isNew ? 'POST' : 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-XSRF-TOKEN': getCsrfToken()
            },
            body: JSON.stringify(recipeData)
        }).then(async response => {
            if (response.ok) {
                const savedId = isNew ? (await response.json()).id : recipeId;
                window.location.href = BASE_URL + '/recipes/' + savedId;
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new RecipeEdit();
});