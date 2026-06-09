function getCsrfToken() {
    const match = document.cookie.match(/XSRF-TOKEN=([^;]+)/);
    return match ? decodeURIComponent(match[1]) : null;
}

function getRecipeIdFromPath() {
    const segments = location.pathname.split('/');
    const idx = segments.indexOf('recipes');
    return segments[idx + 1];
}

async function deleteRecipe(id) {
    const result = confirm("Вы уверены, что хотите удалить этот рецепт?");

    if (result) {
        const response = await fetch(CONTEXT_PATH + '/api/recipes/' + id, {
            method: 'DELETE',
            headers: { 'X-XSRF-TOKEN': getCsrfToken() }
        });

        if (response.ok) {
            window.location.href = (new URL(BASE_URL + "/profile")).toString();
        }
    }
}

async function deleteReview(id) {
    const result = confirm("Вы уверены, что хотите удалить отзыв?");

    if (result) {
        const recipeId = getRecipeIdFromPath();
        const response = await fetch(CONTEXT_PATH + '/api/recipes/' + recipeId + '/reviews/' + id, {
            method: 'DELETE',
            headers: { 'X-XSRF-TOKEN': getCsrfToken() }
        });

        if (response.ok) {
            location.reload();
        }
    }
}

async function logout() {
    const result = confirm("Вы уверены, что хотите выйти?");

    if (result) {
        await fetch(CONTEXT_PATH + '/logout', {
            method: 'POST',
            headers: { 'X-XSRF-TOKEN': getCsrfToken() }
        });
        window.location.href = BASE_URL + '/';
    }
}

function cancel() {
    history.back();
}

function goTo(path, params = {}) {
    const url = new URL(BASE_URL + path);

    Object.keys(params).forEach(key => {
        url.searchParams.set(key, params[key]);
    });

    window.location.href = url.toString();
}

async function addToFavorites() {
    const recipeId = getRecipeIdFromPath();
    await fetch(CONTEXT_PATH + '/api/recipes/' + recipeId + '/favorites', {
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': getCsrfToken() }
    });
    location.reload();
}

async function removeFromFavorites() {
    const recipeId = getRecipeIdFromPath();
    await fetch(CONTEXT_PATH + '/api/recipes/' + recipeId + '/favorites', {
        method: 'DELETE',
        headers: { 'X-XSRF-TOKEN': getCsrfToken() }
    });
    location.reload();
}

let escapeHandler = null;

function showPopup() {
    const blockout = document.getElementById("blockout");
    const body = document.getElementById("body");

    body.classList.add("no-scroll");
    blockout.classList.remove("none");

    escapeHandler = (e) => {
        if (e.key === 'Escape') closePopup();
    };
    document.addEventListener('keydown', escapeHandler);
}

function closePopup() {
    const blockout = document.getElementById("blockout");
    const body = document.getElementById("body");

    body.classList.remove("no-scroll");
    blockout.classList.add("none");

    if (escapeHandler) {
        document.removeEventListener('keydown', escapeHandler);
        escapeHandler = null;
    }
}

function editReview() {
    const userReview = document.getElementById("review-user");
    const editReview = document.getElementById("review-edit");

    if (userReview.classList.contains("none")) {
        userReview.classList.remove("none");
        editReview.classList.add("none");
    } else {
        userReview.classList.add("none");
        editReview.classList.remove("none");
    }
}

function setRating(rating) {
    const ratingInput = document.getElementById("rating");

    ratingInput.value = rating;

    for (let i = 1; i <= rating; i++) {
        const star = document.getElementById(`star-${i}`);
        star.classList.remove("text-subtle");
    }

    for (let i = rating + 1; i <= 5; i++) {
        const star = document.getElementById(`star-${i}`);
        star.classList.add("text-subtle");
    }
}

async function uploadImage() {
    const file = document.getElementById("image").files[0];
    const type = document.getElementById("type").value;

    if (!file) {
        return;
    }

    const formData = new FormData();
    formData.append('image', file);

    const response = await fetch(CONTEXT_PATH + '/api/image/' + type, {
        method: 'POST',
        headers: { 'X-XSRF-TOKEN': getCsrfToken() },
        body: formData
    });

    const result = await response.json();

    if (result && result.url) {
        const imageUrl = document.getElementById("imageUrl");
        imageUrl.value = result.url;
    }
}

async function submitReview() {
    const recipeId = getRecipeIdFromPath();
    const reviewId = document.getElementById('reviewId')?.value;
    const rating = parseInt(document.getElementById('rating').value);
    const comment = document.getElementById('comment').value;

    const isNew = !reviewId;
    const url = isNew
        ? CONTEXT_PATH + '/api/recipes/' + recipeId + '/reviews'
        : CONTEXT_PATH + '/api/recipes/' + recipeId + '/reviews/' + reviewId;

    const response = await fetch(url, {
        method: isNew ? 'POST' : 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-XSRF-TOKEN': getCsrfToken()
        },
        body: JSON.stringify({ rating, comment })
    });

    if (response.ok) {
        location.reload();
    }
}

function changeReview() {
    const textInput = document.getElementById('comment');
    const reviewFooter = document.getElementById('review-footer');

    if (textInput.value.length > 0) {
        reviewFooter.classList.remove("none");
    } else {
        reviewFooter.classList.add("none");
    }
}
