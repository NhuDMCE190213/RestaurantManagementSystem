<%-- /WEB-INF/ingredient/list.jsp --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Ingredient List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">
                    <form action="<c:url value='ingredient'><c:param name='page' value='1'/></c:url>" method="get" class="search-box input-group">
                            <div class="search-box input-group">
                                <span class="input-group-text"><i class="bi bi-search"></i></span>
                                <input type="search" name="keyword" class="form-control" 
                                       placeholder="Search by name"
                                       value="${param.keyword != null ? param.keyword : (requestScope.keyword != null ? requestScope.keyword : '')}">
                        </div>
                    </form>
                    <a class="btn btn-primary add-btn" href="<c:url value="ingredient">
                           <c:param name="view" value="add"/>
                       </c:url>"><i class="bi bi-plus-circle"></i> Add</a>

                </div>
            </div>
        </div>
        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th scope="col" width="5%">Id</th>
                        <th scope="col" width="30%">Name</th>
                        <th scope="col" width="5%">Unit</th>
                        <th scope="col" width="15%">Type</th>
                        <th scope="col" width="15%">Status</th>
                        <th scope="col" width="10%" class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${ingredientsList == null || empty ingredientsList}">
                            <tr>
                                <td colspan="11" style="color:red;">No data to display</td>
                            </tr> 
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="ingredient" items="${ingredientsList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${ingredient.ingredientId}"/></td>
                                    <td><c:out value="${ingredient.ingredientName}"/></td>
                                    <td><c:out value="${ingredient.unit}"/></td>
                                    <td><c:out value="${ingredient.type.typeName}"/></td>
                                    <td><c:out value="${ingredient.status}"/></td>
                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">
                                            <c:url var="edit" value="ingredient">
                                                <c:param name="view" value="edit"/>
                                                <c:param name="id" value="${ingredient.ingredientId}"/>
                                            </c:url>
                                            <a class="btn btn-outline-secondary btn-icon btn-edit" 
                                               title="Edit"
                                               aria-label="Edit"
                                               href="${edit}">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete" 
                                                    title="Delete" 
                                                    aria-label="Delete" 
                                                    onclick="showDeletePopup('${ingredient.ingredientId}')">
                                                <i class="bi bi-x-circle"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
            <nav aria-label="Page navigation">
                <c:if test="${searchKeyword == null || searchKeyword.isEmpty()}">
                    <ul class="pagination">
                        <%-- Previous Link --%>
                        <li class="page-item ${((empty param.page) || (param.page <= 1)) ? 'disabled' : ''}">
                            <a class="page-link" 
                               href="<c:url value='ingredient'>
                                   <c:param name='page' value='${(param.page - 1)}'/>
                               </c:url>" 
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>

                        <%-- Page Numbers --%>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <li class="page-item ${i == (param.page != null ? param.page : 1) ? 'active' : ''}">
                                <a class="page-link" href="<c:url value='ingredient'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                                </li>
                        </c:forEach>

                        <%-- Next Link --%>
                        <li class="page-item ${(totalPages <= param.page || totalPages eq 1) ? 'disabled' : ''}">
                            <a class="page-link" 
                               href="<c:url value='ingredient'>
                                   <c:param name='page' value='${(empty param.page) ? 2 : param.page + 1}'/>
                               </c:url>" 
                               aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </c:if>
            </nav>
        </div>




    </div>
</section>
</div>
</div>
</main>

<div class="modal" id="deletePopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger">
                <h6 id="idForDeletePopup">Are you sure you want to delete this ingredient?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value='ingredient'/>">
                <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%-- Move script and closing tags here from original page to complete the structure --%>
<script src="<%=request.getContextPath()%>/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/vendor/aos/aos.js"></script>

<script src="<%=request.getContextPath()%>/assets/js/main.js"></script>

<c:if test="${not empty param.status}">
    <div class="modal" id="exampleModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Action <c:choose><c:when test="${param.status == 'success'}">Successful</c:when><c:otherwise>Fail</c:otherwise></c:choose></h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                    <c:choose>
                        <c:when test="${param.status eq 'success'}">
                            <p style="color: green"> Delete the Ingredient <c:out value="${param.lastAction}"/> successfully.</p>
                        </c:when>
                        <c:otherwise>
                            <p style="color: red">Failed to <c:out value="${param.lastAction}"/> the ingredient. Please check the information.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script>
                                                        var myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
                                                        myModal.show();
    </script>
</c:if>
<script>
    function showDeletePopup(id) {
        document.getElementById("hiddenInputIdDelete").value = id;
        document.getElementById("idForDeletePopup").textContent = "Are you sure you want to delete the ingredient with id = " + id + "?";
        var myModal = new bootstrap.Modal(document.getElementById('deletePopup'));
        myModal.show();
    }
</script>
</html>