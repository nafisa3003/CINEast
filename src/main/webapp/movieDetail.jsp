<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="patterns.TMDbMovieAdapter.Movie, patterns.MovieServiceFacade.Review, java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%
  Movie movie = (Movie) request.getAttribute("movie");
  String pageTitle = (movie != null) ? movie.getTitle() + " — CINÉast" : "CINÉast";
%>
<title><%= pageTitle %></title>
<link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
<style>
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{--bg:#0D0E12;--surface:rgba(25,28,36,0.6);--gold:#E5A93C;--text:#FFFFFF;--muted:#9EA3B0;--radius:8px}
html,body{background:var(--bg);color:var(--text);font-family:'Inter',sans-serif;min-height:100vh}
a{text-decoration:none;color:inherit}

/* ── Backdrop ── */
.backdrop{position:relative;height:460px;overflow:hidden}
.backdrop-img{width:100%;height:100%;object-fit:cover;object-position:center 20%;filter:brightness(.4)}
.backdrop-gradient{position:absolute;inset:0;background:linear-gradient(to top,#0D0E12 0%,rgba(13,14,18,0.3) 60%,transparent 100%)}

/* ── Detail card ── */
.detail-wrap{max-width:1100px;margin:-180px auto 0;padding:0 40px;position:relative;z-index:2}
.detail-card{display:flex;gap:40px;align-items:flex-start}
.poster-col{flex:0 0 240px}
.poster-col img{width:100%;border-radius:12px;box-shadow:0 24px 60px rgba(0,0,0,0.7);display:block}
.info-col{flex:1;padding-top:8px}
.movie-title{font-family:'Bebas Neue',sans-serif;font-size:clamp(28px,4.5vw,52px);
  letter-spacing:2px;line-height:1.05;margin-bottom:12px}
.meta-row{display:flex;align-items:center;gap:16px;flex-wrap:wrap;margin-bottom:24px}
.rating-badge{display:flex;align-items:center;gap:6px;background:rgba(229,169,60,0.15);
  border:1px solid rgba(229,169,60,0.4);border-radius:20px;padding:5px 14px;
  color:var(--gold);font-weight:700;font-size:15px}
.meta-pill{background:rgba(255,255,255,0.08);border-radius:20px;padding:4px 12px;
  font-size:13px;color:var(--muted)}
.overview-label{font-size:11px;letter-spacing:2px;font-weight:600;color:var(--gold);
  text-transform:uppercase;margin-bottom:8px}
.overview{color:var(--muted);line-height:1.8;font-size:15px;max-width:640px;margin-bottom:32px}
.info-grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:16px;margin-bottom:32px}
.info-item{background:var(--surface);backdrop-filter:blur(12px);border-radius:var(--radius);
  padding:14px 18px;border:1px solid rgba(255,255,255,0.07)}
.info-item .label{font-size:11px;color:var(--gold);letter-spacing:1.5px;text-transform:uppercase;margin-bottom:4px;font-weight:600}
.info-item .value{font-size:15px;font-weight:500}

/* ── Reviews ── */
.reviews-section{max-width:1100px;margin:56px auto 0;padding:0 40px 80px}
.reviews-title{font-family:'Bebas Neue',sans-serif;font-size:28px;letter-spacing:2px;margin-bottom:8px}
.divider{height:1px;background:linear-gradient(to right,rgba(229,169,60,0.4),transparent);margin-bottom:32px}
.review-form{background:var(--surface);backdrop-filter:blur(12px);border-radius:12px;
  padding:28px;border:1px solid rgba(255,255,255,0.08);margin-bottom:36px}
.review-form h3{font-size:15px;font-weight:600;margin-bottom:16px;color:var(--muted)}
.review-form textarea{width:100%;min-height:110px;background:rgba(255,255,255,0.06);
  border:1px solid rgba(255,255,255,0.12);border-radius:var(--radius);color:var(--text);
  padding:14px 16px;font-size:15px;resize:vertical;outline:none;font-family:'Inter',sans-serif;
  transition:border-color .2s;line-height:1.6}
.review-form textarea:focus{border-color:var(--gold)}
.btn-submit{margin-top:12px;padding:11px 28px;background:var(--gold);color:#0D0E12;
  border:none;border-radius:var(--radius);font-weight:600;font-size:14px;cursor:pointer;
  transition:opacity .2s;font-family:'Inter',sans-serif}
.btn-submit:hover{opacity:.88}
.review-list{display:flex;flex-direction:column;gap:16px}
.review-card{background:var(--surface);backdrop-filter:blur(12px);border-radius:12px;
  padding:22px 26px;border:1px solid rgba(255,255,255,0.07)}
.review-header{display:flex;align-items:center;gap:10px;margin-bottom:10px}
.review-avatar{width:36px;height:36px;border-radius:50%;background:rgba(229,169,60,0.2);
  border:1px solid rgba(229,169,60,0.4);display:flex;align-items:center;justify-content:center;
  font-weight:700;font-size:14px;color:var(--gold);flex-shrink:0}
.review-author{font-weight:600;font-size:14px}
.review-date{color:var(--muted);font-size:12px}
.review-content{color:var(--muted);font-size:14px;line-height:1.7}
.no-reviews{color:var(--muted);font-size:14px;text-align:center;padding:32px;
  background:var(--surface);border-radius:12px;border:1px solid rgba(255,255,255,0.07)}
.login-prompt{color:var(--muted);font-size:14px;margin-bottom:24px}
.login-prompt a{color:var(--gold)}
</style>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<% if (movie != null) { %>

<%-- Backdrop --%>
<div class="backdrop">
  <% if (movie.getBackdropPath() != null && !movie.getBackdropPath().isEmpty()) { %>
  <img class="backdrop-img" src="<%= movie.getBackdropUrl() %>" alt="<%= movie.getTitle() %>">
  <% } else { %><div class="backdrop-img" style="background:#1a1c25"></div><% } %>
  <div class="backdrop-gradient"></div>
</div>

<%-- Detail card --%>
<div class="detail-wrap">
  <div class="detail-card">
    <div class="poster-col">
      <% if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) { %>
      <img src="<%= movie.getPosterUrl() %>" alt="<%= movie.getTitle() %>">
      <% } %>
    </div>
    <div class="info-col">
      <h1 class="movie-title"><%= movie.getTitle() %></h1>
      <div class="meta-row">
        <span class="rating-badge">
          ★ <%= movie.getRatingFormatted() %>
        </span>
        <span class="meta-pill"><%= movie.getReleaseYear() %></span>
        <% if (movie.getRuntime() > 0) { %>
        <span class="meta-pill"><%= movie.getRuntime() %> min</span>
        <% } %>
      </div>
      <div class="overview-label">Overview</div>
      <p class="overview"><%= movie.getOverview() %></p>
      <div class="info-grid">
        <div class="info-item">
          <div class="label">Release date</div>
          <div class="value"><%= movie.getReleaseDate() %></div>
        </div>
        <div class="info-item">
          <div class="label">Rating</div>
          <div class="value">⭐ <%= movie.getRatingFormatted() %> / 10</div>
        </div>
        <div class="info-item">
          <div class="label">Popularity</div>
          <div class="value"><%= String.format("%.0f", movie.getPopularity()) %> pts</div>
        </div>
        <% if (movie.getRuntime() > 0) { %>
        <div class="info-item">
          <div class="label">Runtime</div>
          <div class="value"><%= movie.getRuntime() %> minutes</div>
        </div>
        <% } %>
      </div>
    </div>
  </div>
</div>

<%-- Reviews --%>
<div class="reviews-section">
  <h2 class="reviews-title">Reviews</h2>
  <div class="divider"></div>
  <c:if test="${not empty sessionScope.username}">
  <div class="review-form">
    <h3>Write your review</h3>
    <form action="ReviewServlet" method="post">
      <input type="hidden" name="movieId" value="<%= movie.getId() %>">
      <textarea name="review" placeholder="Share your thoughts about this film…" required></textarea>
      <button type="submit" class="btn-submit">Post review</button>
    </form>
  </div>
  </c:if>
  <c:if test="${empty sessionScope.username}">
    <p class="login-prompt"><a href="login.jsp">Sign in</a> to write a review.</p>
  </c:if>

  <%
    List<Review> reviews = (List<Review>) request.getAttribute("reviews");
    if (reviews == null || reviews.isEmpty()) { %>
    <div class="no-reviews">No reviews yet. Be the first to share your thoughts!</div>
  <% } else { %>
  <div class="review-list">
    <% for (Review r : reviews) {
         String initial = r.getUsername().length() > 0
             ? String.valueOf(r.getUsername().charAt(0)).toUpperCase() : "?"; %>
    <div class="review-card">
      <div class="review-header">
        <div class="review-avatar"><%= initial %></div>
        <div>
          <div class="review-author"><%= r.getUsername() %></div>
          <% if (!r.getCreatedAt().isEmpty()) { %>
          <div class="review-date"><%= r.getCreatedAt() %></div>
          <% } %>
        </div>
      </div>
      <p class="review-content"><%= r.getContent() %></p>
    </div>
    <% } %>
  </div>
  <% } %>
</div>

<% } else { %>
<div style="text-align:center;padding:80px 32px;color:#9EA3B0">Movie not found.</div>
<% } %>

<jsp:include page="footer.jsp"/>
</body>
</html>
