<%@ tag description="header" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<header class="fr page-header">
    <div class="container fr header-container">
<%--        <div class="fr icon-text-container pointer" onclick="goTo('/')">--%>
<%--            <i class="fa-solid fa-utensils fa-3x logo"></i>--%>
<%--            <h1>Вкусный букварь</h1>--%>
<%--        </div>--%>
        <t:logo/>

        <ul class="fr header-navigation">
            <c:choose>
                <c:when test="${not empty currentUser}">
                    <li>
                        <a onclick="goTo('/recipes/new')">
                            <i class="fa-solid fa-pencil fa-3x"></i>
                        </a>
                    </li>
                    <li>
                        <a onclick="goTo('/profile', {'section': 'favorites'})">
                            <i class="fa-solid fa-heart fa-3x"></i>
                        </a>
                    </li>
                    <li>
                        <a onclick="goTo('/profile')">
                            <t:img-user size="fa-3x" url="${currentUser.avatarUrl}"/>
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a onclick="goTo('/login')">
                            <i class="fa-solid fa-arrow-right-to-bracket fa-3x"></i>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
            <li>
                <a id="sidebar-button">
                    <i class="fa-solid fa-bars fa-3x"></i>
                </a>
            </li>
        </ul>
    </div>
</header>
