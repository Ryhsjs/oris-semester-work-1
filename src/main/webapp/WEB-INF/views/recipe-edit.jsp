<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%--@elvariable id="recipe" type="ru.itis.flavorful_book.dto.RecipeDTO"--%>
<t:main title="${not empty recipe ? recipe.title().concat(' (Изменение)') : 'Написание рецепта'}">

    <input type="hidden" id="recipeId" value="${recipe.id()}">
    <input type="hidden" id="imageUrl" value="${recipe.imageUrl()}">
    <input type="hidden" id="type" value="recipe">

    <header>
        <div class="fr header-container mb">
            <button class="plain-button" onclick="cancel()">Назад</button>
            <c:choose>
                <c:when test="${not empty recipe}">
                    <button class="del-button" onclick="deleteRecipe(${recipe.id()})">Удалить</button>
                </c:when>
                <c:otherwise>
                    <div></div>
                </c:otherwise>
            </c:choose>
        </div>
        <section class="fr recipe-start">
            <div class="fc icon-text-container">
                <figure class="recipe-info-figure">
                    <t:img-recipe url="${not empty recipe ? recipe.imageUrl() : null}"/>
                </figure>
                <label>
                    <input type="file" accept="image/*" name="recipeImage" id="image" onchange="uploadImage()">
                </label>
            </div>

            <div class="stats-grid">
                <label for="title" style="grid-area: title">
                    <input class="title-input" name="title" id="title" type="text" required
                           placeholder="Введите название"
                           value="${not empty recipe ? recipe.title() : ''}">
                </label>
                <p style="grid-area: tctp">Общее время готовки (мин):</p>
                <label for="totalCookingTime" style="grid-area: tctv">
                    <input class="full-width" type="number" name="totalCookingTime"
                           id="totalCookingTime" min="0" max="1440" required
                           value="${not empty recipe ? recipe.totalCookingTime() : ''}">
                </label>

                <p style="grid-area: actp">Активное время готовки (мин):</p>
                <label for="activeCookingTime" style="grid-area: actv">
                    <input class="full-width" type="number" name="activeCookingTime"
                           id="activeCookingTime" min="0" max="1440" required
                           value="${not empty recipe ? recipe.activeCookingTime() : ''}">
                </label>

                <p style="grid-area: sp">Количество порций:</p>
                <label for="servings" style="grid-area: sv">
                    <input class="full-width" type="number" name="servings"
                           id="servings" min="0" max="100" required
                           value="${not empty recipe ? recipe.servings() : ''}">
                </label>
            </div>
        </section>
    </header>

    <section>
        <h2>Категории</h2>
        <div class="fr categories" id="categories-section">
            <label for="categories-add">
                <select class="category" id="categories-add">
                    <option value="0">Добавить</option>
                    <hr>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}"><c:out value="${category.name}"/></option>
                    </c:forEach>
                </select>
            </label>
            <script>
                const existingCategories = <c:choose>
                    <c:when test="${not empty recipe}">${recipe.categories()}</c:when>
                        <c:otherwise>[]
                </c:otherwise>
                </c:choose>
            </script>
        </div>
    </section>

    <section>
        <h2>Описание</h2>
        <label for="description">
            <textarea rows="5" name="description" id="description"><c:if
                    test="${not empty recipe}"><c:out value="${recipe.description()}"/></c:if></textarea>
        </label>
    </section>

    <section class="ingredients">
        <h2>Ингредиенты</h2>
        <div class="ingredients-grid" id="ingredients-section">
            <div class="ingredient-row">
                <select name="ingredient" id="ingredient">
                    <option value="0">Продукт</option>
                    <hr>
                    <c:forEach var="ingredient" items="${ingredients}">
                        <option value="${ingredient.id}"><c:out value="${ingredient.name}"/></option>
                    </c:forEach>
                </select>
                <label>
                    <input name="ingredientNotes" id="ingredientNotes" type="text" placeholder="Примечание">
                </label>
                <p>—</p>
                <input name="ingredientQuantity" id="ingredientQuantity" min="0" type="number">
                <select name="ingredientUnit" id="ingredientUnit">
                    <c:forEach items="${units}" var="unit">
                        <option value="${unit.toString()}"><c:out value="${unit.unit}"/></option>
                    </c:forEach>
                </select>
                <button class="filled-button" id="ingredients-add" type="button">+</button>
                <div></div>
            </div>
        </div>

        <script>
            const existingIngredients = [
                <c:if test="${not empty recipe}">
                <c:forEach var="ingredient" items="${recipe.ingredients()}">
                {
                    "id": ${ingredient.id()},
                    "quantity": ${ingredient.quantity()},
                    "unit": "${ingredient.unit()}",
                    "notes": "${ingredient.notes()}"
                },
                </c:forEach>
                </c:if>
            ]
        </script>
    </section>

    <section>
        <h2>Рецепт</h2>
        <label for="instructions">
            <textarea rows="15" name="instructions" id="instructions" required><c:if
                    test="${not empty recipe}"><c:out value="${recipe.instructions()}"/></c:if></textarea>
        </label>
    </section>

    <footer class="fr">
        <div class="foot">
            <button class="plain-button" type="button" onclick="cancel()">Отмена</button>
            <button class="filled-button" type="button" id="post-button">Сохранить</button>
        </div>
    </footer>
</t:main>
<script src="${pageContext.servletContext.contextPath}/js/RecipeEdit.js"></script>
