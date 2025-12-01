<%-- 
    Document   : list
    Created on : 2 Nov 2025, 9:05:59 AM
    Author     : Dai Minh Nhu - CE190213
--%>
<title>My Profile - Yummy</title>
<%@include file="/WEB-INF/include/headerDashboard.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="col-12 col-lg-9 col-xxl-10 table-section" aria-label="Listing table">
    <div class="content-card shadow-sm">
        <div class="card-header border-0 px-4 py-3 d-flex flex-column flex-md-row gap-3 gap-md-0 justify-content-between align-items-md-center">
            <div>
                <h1 class="section-title mb-1">Menu Item List</h1>
            </div>
            <div class="actions d-flex flex-column flex-md-row gap-2 align-items-md-center justify-content-md-end">
                <div class="filters d-flex flex-wrap gap-2 justify-content-end">

                    <form action="<c:url value="menuitem">
                              <c:param name="page" value="1"/>
                          </c:url>" method="get" class="search-box input-group">
                        <input type="hidden" name="view" value="list"/>
                          <span class="input-group-text"><i class="bi bi-search"></i></span>
                        <input type="search" name="keyword" class="form-control" placeholder="Search by name" value="${param.keyword}">
                    </form>

                    <a class="btn btn-primary add-btn" href="<c:url value="menuitem">
                           <c:param name="view" value="add"/>
                       </c:url>"><i class="bi bi-plus-circle"></i>Add</a>

                </div>
            </div>
        </div>

        <div class="table-responsive px-4 pb-2">
            <table class="table align-middle admin-table">
                <thead>
                    <tr>
                        <th width="5%" scope="col">ID</th>
                        <th width="10%" scope="col">Image</th>
                        <th width="15%" scope="col">Name</th>
                        <th width="15%" scope="col">Category</th>
                        <th width="15%" scope="col">Recipe</th>
                        <th width="10%" scope="col">Price(VND)</th>
                        <th width="20%" scope="col">Description</th>
                        <th width="20%" scope="col" class="text-end">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${menuItemsList == null || empty menuItemsList}">
                            <tr>
                                <td colspan="8" style="color:red;">No menu items found to display</td>
                            </tr>  
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${menuItemsList}" varStatus="loop">
                                <tr>
                                    <td><c:out value="${item.menuItemId}"/></td>
                                    <td><img 
                                            src="<c:out value="${pageContext.request.contextPath}/${item.imageUrl}"/>" 
                                            alt="${item.itemName}" 
                                            style="width: 100px; height: 100px; object-fit: cover; border-radius: 5px;"
                                            onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/menu/NIA.png';"
                                            /></td>
                                    <td><c:out value="${item.itemName}"/></td>
                                    <td><c:out value="${item.category.categoryName}"/></td>
                                    <td><c:out value="${item.recipe.recipeName}"/></td>
                                    <td><c:out value="${item.priceVND}"/></td>
                                    <td><c:out value="${item.description}"/></td>


                                    <td class="text-end">
                                        <div class="action-button-group d-flex justify-content-end gap-2">

                                            <a class="btn btn-outline-secondary btn-icon btn-edit"
                                               title="Edit" aria-label="Edit"
                                               href="<c:url value="menuitem">
                                                   <c:param name="view" value="edit"/>
                                                   <c:param name="id" value="${item.menuItemId}"/>
                                               </c:url>">
                                                <i class="bi bi-pencil"></i>
                                            </a>

                                            <button type="button" class="btn btn-outline-secondary btn-icon btn-delete"
                                                    title="Delete" aria-label="Delete" onclick="showDeletePopup('${item.menuItemId}', '${item.itemName}')">
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

            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item ${((empty param.page) || param.page <= 1)?"disabled":""}">
                        <a class="page-link" href="<c:url value="/menuitem">
                               <c:param name="view" value="list"/>
                               <c:param name="page" value="${param.page - 1}"/>
                           </c:url>" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                        <li class="page-item ${((empty param.page && i == 1) || param.page == i)?"active":""}">
                            <a class="page-link" href="<c:url value="/menuitem">
                                   <c:param name="view" value="list"/>
                                   <c:param name="page" value="${i}"/>
                               </c:url>">${i}</a></li>
                        </c:forEach>
                    <li class="page-item ${(requestScope.totalPages <= param.page || requestScope.totalPages eq 1 )?"disabled":""}">
                        <a class="page-link" href="<c:url value="/menuitem">
                               <c:param name="view" value="list"/>
                               <c:param name="page" value="${(empty param.page)?2:param.page + 1}"/>
                           </c:url>" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</section>

<div class="modal" id="deletePopup" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Confirm Deletion</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-danger">
                <h6 id="idForDeletePopup">Are you sure you want to delete this menu item: <span id="itemNameSpan"></span>?</h6>
                <small class="text-muted">This action cannot be undone.</small>
            </div>
            <form method="post" action="<c:url value="menuitem"/>">
                <input type="hidden" id="hiddenInputIdDelete" name="id" value="">
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function showDeletePopup(itemId, itemName) {
        document.getElementById('hiddenInputIdDelete').value = itemId;
        document.getElementById('itemNameSpan').textContent = itemName;
        var deleteModal = new bootstrap.Modal(document.getElementById('deletePopup'));
        deleteModal.show();
    }
</script>

<%@include file="/WEB-INF/include/footerDashboard.jsp" %>