<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="patterns.TMDbMovieAdapter.Movie"%>
<!DOCTYPE html>
<html lang="en">
...
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CINÉast — Home</title>
<link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Inter:wght@300;400;500;600&display=swap" rel="stylesheet">
<style>
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
:root{--bg:#0D0E12;--surface:rgba(25,28,36,0.6);--gold:#E5A93C;--text:#FFFFFF;--muted:#9EA3B0;--radius:8px}
html,body{background:var(--bg);color:var(--text);font-family:'Inter',sans-serif;min-height:100vh}
a{text-decoration:none;color:inherit}

/* ── Hero ───────────────────────────────────────────── */
.hero{position:relative;height:78vh;min-height:480px;overflow:hidden;display:flex;align-items:flex-end}
.hero-bg{position:absolute;inset:0;background-size:cover;background-position:center top;filter:brightness(0.45)}
.hero-gradient{position:absolute;inset:0;background:linear-gradient(to top,#0D0E12 0%,rgba(13,14,18,0.5) 50%,transparent 100%)}
.hero-content{position:relative;padding:48px 48px 56px;max-width:680px}
.hero-badge{display:inline-flex;align-items:center;gap:6px;background:rgba(229,169,60,0.18);
  border:1px solid rgba(229,169,60,0.4);border-radius:20px;padding:4px 14px;
  font-size:12px;color:var(--gold);letter-spacing:1px;font-weight:600;margin-bottom:20px}
.hero-title{font-family:'Bebas Neue',sans-serif;font-size:clamp(36px,6vw,72px);
  letter-spacing:2px;line-height:1;margin-bottom:16px}
.hero-overview{color:var(--muted);font-size:15px;line-height:1.7;margin-bottom:28px;max-width:480px;
  display:-webkit-box;-webkit-line-clamp:3;-webkit-box-orient:vertical;overflow:hidden}
.hero-meta{display:flex;align-items:center;gap:16px;margin-bottom:32px;flex-wrap:wrap}
.hero-rating{display:flex;align-items:center;gap:6px;color:var(--gold);font-weight:600;font-size:16px}
.hero-year{color:var(--muted);font-size:14px}
.hero-cta{display:inline-flex;align-items:center;gap:10px;padding:14px 32px;
  background:var(--gold);color:#0D0E12;border-radius:var(--radius);font-weight:600;font-size:15px;
  transition:opacity .2s,transform .15s}
.hero-cta:hover{opacity:.9;transform:translateY(-2px)}
.hero-cta svg{width:18px;height:18px}

/* ── Row sections ───────────────────────────────────── */
.section{padding:0 32px;margin-bottom:48px}
.section-header{display:flex;align-items:center;justify-content:space-between;margin-bottom:20px}
.section-title{font-family:'Bebas Neue',sans-serif;font-size:22px;letter-spacing:2px}
.section-title .accent{color:var(--gold)}
.sort-select{background:rgba(255,255,255,0.07);border:1px solid rgba(255,255,255,0.12);
  color:var(--text);border-radius:var(--radius);padding:6px 12px;font-size:13px;
  cursor:pointer;outline:none;font-family:'Inter',sans-serif}

.movie-row{display:flex;gap:16px;overflow-x:auto;padding-bottom:12px;scroll-snap-type:x mandatory}
.movie-row::-webkit-scrollbar{height:4px}
.movie-row::-webkit-scrollbar-thumb{background:rgba(229,169,60,0.3);border-radius:4px}
.movie-row::-webkit-scrollbar-track{background:transparent}

/* ── Poster card ────────────────────────────────────── */
.movie-card{flex:0 0 160px;scroll-snap-align:start;position:relative;border-radius:var(--radius);
  overflow:hidden;cursor:pointer;transition:transform .25s,box-shadow .25s}
.movie-card:hover{transform:scale(1.06);box-shadow:0 16px 40px rgba(0,0,0,0.6)}
.movie-card img{width:100%;aspect-ratio:2/3;object-fit:cover;display:block}
.movie-card-overlay{position:absolute;inset:0;background:linear-gradient(to top,rgba(13,14,18,0.95) 0%,rgba(13,14,18,0.3) 50%,transparent 100%);
  opacity:0;transition:opacity .25s;display:flex;flex-direction:column;justify-content:flex-end;padding:14px}
.movie-card:hover .movie-card-overlay{opacity:1}
.card-title{font-size:13px;font-weight:600;line-height:1.3;margin-bottom:6px;
  display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}
.card-meta{display:flex;align-items:center;gap:8px}
.card-rating{color:var(--gold);font-size:12px;font-weight:600;display:flex;align-items:center;gap:3px}
.card-year{color:var(--muted);font-size:11px}
.card-no-poster{width:100%;aspect-ratio:2/3;background:#1a1c25;display:flex;align-items:center;
  justify-content:center;color:var(--muted);font-size:11px}
</style>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<%-- Hero: first trending movie --%>
<%
  List<Movie> trending = (List<Movie>) request.getAttribute("trendingMovies");
  Movie hero = (trending != null && !trending.isEmpty()) ? trending.get(0) : null;
%>
<% if (hero != null) { %>
<a href="MovieServlet?movieId=<%= hero.getId() %>">
<div class="hero">
  <div class="hero-bg" style="background-image:url('<%= hero.getBackdropUrl() %>')"></div>
  <div class="hero-gradient"></div>
  <div class="hero-content">
    <div class="hero-badge">
      <svg viewBox="0 0 24 24" fill="currentColor" width="12" height="12"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
      Trending this week
    </div>
    <h1 class="hero-title"><%= hero.getTitle() %></h1>
    <p class="hero-overview"><%= hero.getOverview() %></p>
    <div class="hero-meta">
      <span class="hero-rating">
        <svg viewBox="0 0 24 24" fill="currentColor" width="16" height="16"><path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/></svg>
        <%= hero.getRatingFormatted() %>
      </span>
      <span class="hero-year"><%= hero.getReleaseYear() %></span>
    </div>
    <span class="hero-cta">
      <svg viewBox="0 0 24 24" fill="currentColor"><path d="M8 5v14l11-7z"/></svg>
      View movie
    </span>
  </div>
</div>
</a>
<% } %>

<%-- Trending row --%>
<div class="section" style="margin-top:32px">
  <div class="section-header">
    <h2 class="section-title"><span class="accent">Trending</span> this week</h2>
  </div>
  <div class="movie-row">
    <% if (trending != null) { for (Movie m : trending) { %>
    <a href="MovieServlet?movieId=<%= m.getId() %>" class="movie-card">
      <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty()) { %>
      <img src="<%= m.getPosterUrl() %>" alt="<%= m.getTitle() %>" loading="lazy">
      <% } else { %><div class="card-no-poster">No image</div><% } %>
      <div class="movie-card-overlay">
        <div class="card-title"><%= m.getTitle() %></div>
        <div class="card-meta">
          <span class="card-rating">★ <%= m.getRatingFormatted() %></span>
          <span class="card-year"><%= m.getReleaseYear() %></span>
        </div>
      </div>
    </a>
    <% } } %>
  </div>
</div>

<%-- Genre rows --%>
<%
  String[][] genreInfo = {
    {"actionMovies",   "Action"},
    {"comedyMovies",   "Comedy"},
    {"thrillerMovies", "Thriller"},
    {"scifiMovies",    "Sci-Fi"}
  };
%>
<% for (String[] genre : genreInfo) {
     List<Movie> gMovies = (List<Movie>) request.getAttribute(genre[0]);
     if (gMovies == null || gMovies.isEmpty()) continue; %>
<div class="section">
  <div class="section-header">
    <h2 class="section-title"><%= genre[1] %></h2>
  </div>
  <div class="movie-row">
    <% for (Movie m : gMovies) { %>
    <a href="MovieServlet?movieId=<%= m.getId() %>" class="movie-card">
      <% if (m.getPosterPath() != null && !m.getPosterPath().isEmpty()) { %>
      <img src="<%= m.getPosterUrl() %>" alt="<%= m.getTitle() %>" loading="lazy">
      <% } else { %><div class="card-no-poster">No image</div><% } %>
      <div class="movie-card-overlay">
        <div class="card-title"><%= m.getTitle() %></div>
        <div class="card-meta">
          <span class="card-rating">★ <%= m.getRatingFormatted() %></span>
          <span class="card-year"><%= m.getReleaseYear() %></span>
        </div>
      </div>
    </a>
    <% } %>
  </div>
</div>
<% } %>

<jsp:include page="footer.jsp"/>
</body>
</html>
