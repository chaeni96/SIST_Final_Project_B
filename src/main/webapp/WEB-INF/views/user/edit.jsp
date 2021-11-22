<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:import url="../temp/head.jsp"></c:import>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/profileEdit.css" type="text/css">
</head>
<body>
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="userVO"/>
</sec:authorize>
	<c:import url="../temp/nav.jsp"></c:import>
	<div id="react-root">
		<section class="edit-section">
			<div>
			</div>
			<main class="edit-main" role="main">
				<div class="profile-edit">
					<ul class="edit-ul">
						<li>
							<a class="edit-list selected" href="/gram/account/edit" tabindex="0">프로필 편집</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/account/password/change/" tabindex="0">비밀번호 변경</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/account/manage_access/" tabindex="0">앱 및 웹사이트</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/emails/settings/" tabindex="0">이메일 및 SMS</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/push/web/settings/" tabindex="0">푸시 알림</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/accounts/contact_history/" tabindex="0">연락처 관리</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/account/privacy_and_security/" tabindex="0">개인정보 및 보안</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/session/login_activity/" tabindex="0">로그인 활동</a>
						</li>
						<li>
							<a class="edit-list not-selected" href="/emails/emails_sent/" tabindex="0">Instagram에서 보낸 이메일</a>
						</li>
						<div class="switch-space">
							<div class="pro-account">
								<div class="switch-account ">
									<a class="switch-address" href="/accounts/convert_to_professional_account/" tabindex="0">프로페셔널 계정으로 전환</a>
								</div>
							</div>
						</div>
					</ul>
					<article class="edit-object">
						<div class="basics">
							<div class="photo-update-1">
								<div class="icon-outline">
									<button class="icon-btn" title="프로필 사진 추가">
											<img alt="프로필 사진 추가" class="input-icon" src="${pageContext.request.contextPath}/static/icons/user.jpg">
										<c:if test="${not empty userVO.fileName}">
											<img alt="프로필 사진 추가" class="input-icon" src="${pageContext.request.contextPath}/static/icons/user.jpg">									
										</c:if>
									</button>
									<div>
										<form enctype="multipart/form-data" action="./edit/fileUpdate" method="POST" role="presentation">
											<input accept="image/jpeg,image/png" class="profile-photo" type="file" name="file" id="file">
										</form>
									</div>
								</div>
							</div>
							<div class="photo-update-2">
								<h1 class="my-username" title="user_424">${userVO.username}</h1>
								<button class="profile-button" type="button">프로필 사진 바꾸기</button>
								<div>
									<form enctype="multipart/form-data" method="POST" action="./edit/fileUpdate" role="presentation">
										<input accept="image/jpeg,image/png" class="profile-photo" type="file" id="file">
									</form>
								</div>
							</div>
						</div>
						<form class="update-form" action="./edit" method="POST">
							<div class="update-data">
								<aside class="data-name">
									<label for="nickname">닉네임</label>
								</aside>
								<div class="input-change">
									<div class="pChange" style="width: 100%; max-width: 355px;">
										<input aria-required="false" id="nickname" placeholder="닉네임" type="text" class="input-update" value="${userVO.nickname}">
										<div class="pInfo" style="width: 100%; max-width: 355px;">
											<div class="info-text">
												사람들이 회원님의 닉네임을 사용하여 회원님의 계정을 찾을 수 있도록 도와주세요.
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="update-data">
								<aside class="data-name">
									<label for="username">사용자 이름</label>
								</aside>
								<div class="input-change">
									<div class="pChange" style="width: 100%; max-width: 355px;">
										<input aria-required="true" id="username" placeholder="사용자 이름" type="text" class="readonly-data" readonly="readonly"  value="${userVO.username }">
									</div>
								</div>
							</div>
							<div class="update-data">
								<aside class="data-name">
									<label for="website">웹사이트</label>
								</aside>
								<div class="input-change">
									<div class="pChange" style="width: 100%; max-width: 355px;">
										<input aria-required="false" id="website" placeholder="웹사이트" type="url" class="input-update" value="${userVO.website}">
									</div>
								</div>
							</div>
							<div class="update-data">
								<aside class="data-name">
									<label for="introduction">소개</label>
								</aside>
								<div class="input-change">
									<textarea class="input-introduction input-update" id="introduction">${userVO.introduction }</textarea>
								</div>
							</div>
							<div class="update-data">
								<aside class="data-name">
									<label for="email">이메일</label>
								</aside>
								<div class="input-change">
									<div class="pChange" style="width: 100%; max-width: 355px;">
										<input aria-required="false" id="email" placeholder="이메일" type="text" class="input-update" value="${userVO.email}">
									</div>
								</div>
							</div>
							<div class="update-data">
								<aside class="data-name">
									<label for="phone">전화번호</label>
								</aside>
								<div class="input-change">
									<div class="pChange" style="width: 100%; max-width: 355px;">
										<input aria-required="false" id="phone" placeholder="전화번호" type="text" class="input-update" value="${userVO.phone }">
									</div>
								</div>
							</div>
							<div class="btn-space">
								<button class="update-btn" type="button" disabled="disabled">제출</button>
							</div>
						</form>
					</article>
				</div>
			</main>
		<c:import url="../temp/footer.jsp"></c:import>
		</section>
	</div>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/profileEdit.js"></script>
</body>
</html>